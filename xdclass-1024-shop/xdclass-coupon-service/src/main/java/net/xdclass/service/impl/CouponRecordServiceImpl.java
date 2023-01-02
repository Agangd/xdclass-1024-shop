package net.xdclass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.CouponStateEnum;
import net.xdclass.enums.StockTaskEnum;
import net.xdclass.exception.BizException;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.mapper.CouponTaskMapper;
import net.xdclass.model.CouponTaskDO;
import net.xdclass.model.LoginUser;
import net.xdclass.request.LockCouponRecordRequest;
import net.xdclass.util.JsonData;
import net.xdclass.vo.CouponRecordVO;
import net.xdclass.mapper.CouponRecordMapper;
import net.xdclass.model.CouponRecordDO;
import net.xdclass.service.CouponRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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

        QueryWrapper queryWrapper = new QueryWrapper<CouponRecordDO>().eq("id",recordId)
                .eq("user_id",loginUser.getId());
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
            //发送延迟消息 TODO


            return JsonData.buildSuccess();
        }

        else {
            throw new BizException(BizCodeEnum.COUPON_RECORD_LOCK_FAIL);
        }
    }

    //将CouponRecordDO转换成CouponRecordVO的方法
    private CouponRecordVO beanProcess(CouponRecordDO couponRecordDO) {

        CouponRecordVO couponRecordVO = new CouponRecordVO();
        BeanUtils.copyProperties(couponRecordDO,couponRecordVO);
        return couponRecordVO;
    }
}
