package net.xdclass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.config.RabbitMQConfig;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.CouponStateEnum;
import net.xdclass.enums.ProductOrderStateEnum;
import net.xdclass.enums.StockTaskEnum;
import net.xdclass.exception.BizException;
import net.xdclass.fegin.ProductOrderFeignService;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.mapper.CouponTaskMapper;
import net.xdclass.model.CouponRecordMessage;
import net.xdclass.model.CouponTaskDO;
import net.xdclass.model.LoginUser;
import net.xdclass.request.LockCouponRecordRequest;
import net.xdclass.util.JsonData;
import net.xdclass.vo.CouponRecordVO;
import net.xdclass.mapper.CouponRecordMapper;
import net.xdclass.model.CouponRecordDO;
import net.xdclass.service.CouponRecordService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 阿刚
 * @since 2022-12-08
 */
@Service
@Slf4j
public class CouponRecordServiceImpl implements CouponRecordService {


    @Resource
    private CouponRecordMapper couponRecordMapper;

    @Resource
    private CouponTaskMapper couponTaskMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RabbitMQConfig rabbitMQConfig;

    @Resource
    private ProductOrderFeignService orderFeignService;
    /**
     * 分页查询领券记录
     * @param page
     * @param size
     * @return
     */
    @Override
    public Map<String, Object> page(int page, int size) {

        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        QueryWrapper<CouponRecordDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",loginUser.getId()).orderByDesc("create_time");
        //封装分页信息
        Page<CouponRecordDO> pageInfo = new Page<>(page, size);

        IPage<CouponRecordDO> recordIPage = couponRecordMapper.selectPage(pageInfo,queryWrapper);

        Map<String,Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record",recordIPage);
        pageMap.put("total_page",recordIPage.getPages());
        pageMap.put("current_data",recordIPage.getRecords().stream().map(obj -> beanProcess(obj)).collect(Collectors.toList()));
        return pageMap;
    }

    /**
     * 根据id寻找优惠券详情
     * @param recordId
     * @return
     */
    @Override
    public CouponRecordVO findById(long recordId) {

        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        QueryWrapper queryWrapper = new QueryWrapper<CouponRecordDO>().eq("id",recordId).eq("user_id",loginUser.getId());
        CouponRecordDO couponRecordDO = couponRecordMapper.selectOne(queryWrapper);

        if (couponRecordDO == null){
            return null;
        }
        return beanProcess(couponRecordDO);
    }

    /**
     * 锁定优惠券
     *
     * 1)锁定优惠券记录
     * 2)task表插入记录
     * 3)发送延迟消息
     *
     * @param recordRequest
     * @return
     */
    @Override
    public JsonData lockCouponRecords(LockCouponRecordRequest recordRequest) {

        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        String orderOutTradeNo = recordRequest.getOrderOutTradeNo();
        List<Long> lockCouponRecordIds = recordRequest.getLockCouponRecordIds();

        int updateRows = couponRecordMapper.lockUserStateBatch(loginUser.getId(), CouponStateEnum.USED.name(),lockCouponRecordIds);

        List<CouponTaskDO> couponTaskDOList = lockCouponRecordIds.stream().map(obj->{

            CouponTaskDO couponTaskDO = new CouponTaskDO();
            couponTaskDO.setCreateTime(new Date());
            couponTaskDO.setOutTradeNo(orderOutTradeNo);
            couponTaskDO.setCouponRecordId(obj);
            couponTaskDO.setLockState(StockTaskEnum.LOCK.name());
            return couponTaskDO;

        }).collect(Collectors.toList());

        int insertRows = couponTaskMapper.insertBatch(couponTaskDOList);

        log.info("优惠券记录锁定updateRows={}",updateRows);
        log.info("新增优惠券记录task insertRows={}",insertRows);

        if (lockCouponRecordIds.size() == insertRows && insertRows == updateRows){
            //发送延迟消息

            for (CouponTaskDO couponTaskDO : couponTaskDOList){
                CouponRecordMessage couponRecordMessage = new CouponRecordMessage();
                couponRecordMessage.setOutTradeNo(orderOutTradeNo);
                couponRecordMessage.setTaskId(couponTaskDO.getId());

                rabbitTemplate.convertAndSend(rabbitMQConfig.getEventExchange(),rabbitMQConfig.getCouponReleaseDelayRoutingKey(),couponRecordMessage);
                log.info("优惠券锁定消息发送成功 :{}",couponRecordMessage.toString());

            }

            return JsonData.buildSuccess();
        }

        else {
            throw new BizException(BizCodeEnum.COUPON_RECORD_LOCK_FAIL);
        }
    }

    /**
     * 释放优惠券记录
     *
     * 解锁优惠券记录
     * 1）查询task工作单是否存在
     * 2)查询订单状态
     * @param couponRecordMessage
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public boolean releaseCouponRecord(CouponRecordMessage couponRecordMessage) {

        //查询工作单是否存在
        CouponTaskDO taskDO = couponTaskMapper.selectOne(new QueryWrapper<CouponTaskDO>().eq("id",couponRecordMessage.getTaskId()));

        if (taskDO == null){
            log.warn("工作单不存在，消息:{}",couponRecordMessage);
            return true;
        }

        //lock状态才处理
        if(taskDO.getLockState().equalsIgnoreCase(StockTaskEnum.LOCK.name())){

            //查询订单状态
            JsonData jsonData = orderFeignService.queryProductOrderState(couponRecordMessage.getOutTradeNo());
            if (jsonData.getCode() == 0){
                 //正常响应，判断订单状态
                String state = jsonData.getData().toString();
                if (ProductOrderStateEnum.NEW.name().equalsIgnoreCase(state)){
                    //状态都是NEW新建状态，则返回给消息队列，重新投递
                    log.warn("订单状态是NEW,返回给消息队列，重新投递:{}",couponRecordMessage);
                    return false;
                }

                if (ProductOrderStateEnum.PAY.name().equalsIgnoreCase(state)){
                    //如果是已经支付，修改task状态为finish
                    taskDO.setLockState(StockTaskEnum.FINISH.name());
                    couponTaskMapper.update(taskDO,new QueryWrapper<CouponTaskDO>().eq("id",couponRecordMessage.getTaskId()));
                    log.info("订单已经支付，修改库存锁定工作单FINISH状态:{}",couponRecordMessage);
                    return true;
                }

            }

            //订单不存在或订单被取消，确认消息即可，修改task状态未CANCEL,恢复优惠券记录未NEW
            log.warn("订单不存在或订单被取消，确认消息即可，修改task状态未CANCEL,恢复优惠券记录未NEW，message:{}",couponRecordMessage);
            taskDO.setLockState(StockTaskEnum.CANCEL.name());
            couponTaskMapper.update(taskDO,new QueryWrapper<CouponTaskDO>().eq("id",couponRecordMessage.getTaskId()));

            //恢复优惠券记录是new状态
            couponRecordMapper.updateState(taskDO.getCouponRecordId(),CouponStateEnum.NEW.name());
        }else {
            log.warn("工作单状态不是LOCK,state={},消息体={}",taskDO.getLockState(),couponRecordMessage);
        }
        return true;

    }

    //将CouponRecordDO转换成CouponRecordVO的方法
    private CouponRecordVO beanProcess(CouponRecordDO couponRecordDO) {

        CouponRecordVO couponRecordVO = new CouponRecordVO();
        BeanUtils.copyProperties(couponRecordDO,couponRecordVO);
        return couponRecordVO;
    }
}
