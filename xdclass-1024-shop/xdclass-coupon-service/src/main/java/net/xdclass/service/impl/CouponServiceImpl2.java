//package net.xdclass.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import lombok.extern.slf4j.Slf4j;
//import net.xdclass.enums.CouponCategoryEnum;
//import net.xdclass.enums.BizCodeEnum;
//import net.xdclass.enums.CouponPublishEnum;
//import net.xdclass.enums.CouponStateEnum;
//import net.xdclass.exception.BizException;
//import net.xdclass.interceptor.LoginInterceptor;
//import net.xdclass.mapper.CouponMapper;
//import net.xdclass.mapper.CouponRecordMapper;
//import net.xdclass.model.CouponDO;
//import net.xdclass.model.CouponRecordDO;
//import net.xdclass.model.LoginUser;
//import net.xdclass.service.CouponService;
//import net.xdclass.util.CommonUtil;
//import net.xdclass.util.JsonData;
//import net.xdclass.vo.CouponVO;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * <p>
// * 服务实现类
// * </p>
// *
// * @author 阿刚
// * @since 2022-12-08
// */
//@Service
//@Slf4j
//public class CouponServiceImpl2 implements CouponService {
//
//
//    @Autowired
//    private CouponMapper couponMapper;
//
//    @Autowired
//    private CouponRecordMapper couponRecordMapper;
//
//
//    @Autowired
//    private RedissonClient redissonClient;
//
//    /**
//     * 分页查询优惠券
//     * @param page
//     * @param size
//     * @return
//     */
//    @Override
//    public Map<String, Object> pageCouponActivity(int page, int size) {
//
//        Page<CouponDO> pageInfo = new Page<>(page, size);
//        IPage<CouponDO> couponDOIPage = couponMapper.selectPage(pageInfo, new QueryWrapper<CouponDO>()
//                .eq("publish", CouponPublishEnum.PUBLISH)
//                .eq("category", CouponCategoryEnum.PROMOTION)
//                .orderByDesc("create_time"));
//
//        Map<String, Object> pageMap = new HashMap<>(3);
//        //总条数
//        pageMap.put("total_record", couponDOIPage.getTotal());
//        //总页数
//        pageMap.put("total_page", pageInfo.getPages());
//        //记录
//        pageMap.put("current_data", couponDOIPage.getRecords().stream().map(obj -> beanProcess(obj)).collect(Collectors.toList()));
//
//        return pageMap;
//    }
//
//    /**
//     * 领取优惠券接口
//     * 1、获取优惠券是否存在
//     * 2、校验优惠券是否可以领取：时间、库存、超过限制
//     * 3、扣减库存
//     * 4、保存领券记录
//     * <p>
//     * 始终要记得，羊毛党思维很厉害，社会工程学 应用的炉火纯青
//     *
//     * @param couponId
//     * @param category
//     * @return
//     */
//    @Override
//    public JsonData addCoupon(Long couponId, CouponCategoryEnum category) {
//
//
//        //获取登录用户信息
//        LoginUser loginUser = LoginInterceptor.threadLocal.get();
//
//        String lockKey = "lock:coupon:" + couponId;
//
//        RLock rLock = redissonClient.getLock(lockKey);
//        //多个线程被阻塞等待释放锁
//        //默认30秒，有watch dog功能自动续期
//        rLock.lock();
//
//        //加锁10秒过期，没有watch dog功能
////        rLock.lock(10,TimeUnit.SECONDS);
//
//        log.info("领券接口加锁成功:{}", Thread.currentThread().getId());
//
//        CouponRecordDO couponRecordDO = new CouponRecordDO();
//
//        try {
//            //执行业务逻辑
//            CouponDO couponDO = couponMapper.selectOne(new QueryWrapper<CouponDO>()
//                    .eq("id", couponId)
//                    .eq("category", category.name())
//                    .eq("publish", CouponPublishEnum.PUBLISH));
//
//            //优惠券是否可以领取
//            this.checkCoupon(couponDO, loginUser.getId());
//
//            //构建领券记录
//            BeanUtils.copyProperties(couponDO, couponRecordDO);
//            couponRecordDO.setCreateTime(new Date());
//            couponRecordDO.setUseState(CouponStateEnum.NEW.name());
//            couponRecordDO.setUserId(loginUser.getId());
//            couponRecordDO.setUserName(loginUser.getName());
//            couponRecordDO.setCouponId(couponId);
//            couponRecordDO.setId(null);
//
//            //扣减库存
//            int rows = couponMapper.reduceStock(couponId);
//
//            if (rows == 1) {
//                //库存扣减成功才保存记录
//                couponRecordMapper.insert(couponRecordDO);
//            } else {
//                log.warn("发放用户券失败：{}，用户：{}", couponDO, loginUser);
//                throw new BizException(BizCodeEnum.COUPON_NO_STOCK);
//            }
//        } finally {
////            rLock.unlock();
//            log.info("解锁成功");
//        }
//
//        return JsonData.buildSuccess(couponRecordDO);
//    }
//
//    /**
//     * 校验是否可以领取
//     *
//     * @param couponDO
//     * @param userId
//     */
//    private void checkCoupon(CouponDO couponDO, Long userId) {
//
//        if (couponDO == null) {
//            throw new BizException(BizCodeEnum.COUPON_NO_EXITS);
//        }
//        //库存是否足够
//        if (couponDO.getStock() <= 0) {
//            throw new BizException(BizCodeEnum.COUPON_NO_STOCK);
//        }
//
//        //判断是否是发布状态
//        if (!couponDO.getPublish().equals(CouponPublishEnum.PUBLISH.name())) {
//            throw new BizException(BizCodeEnum.COUPON_GET_FAIL);
//        }
//        //是否在领取时间范围
//        long time = CommonUtil.getCurrentTimestamp();
//        long start = couponDO.getStartTime().getTime();
//        long end = couponDO.getEndTime().getTime();
//
//        if (time < start || time > end) {
//            throw new BizException(BizCodeEnum.COUPON_OUT_OF_TIME);
//        }
//
//        //用户是否超过限制
//        int recordNum = couponRecordMapper.selectCount(new QueryWrapper<CouponRecordDO>()
//                .eq("coupon_id", couponDO.getId())
//                .eq("user_id", userId));
//
//        if (recordNum >= couponDO.getUserLimit()) {
//            throw new BizException(BizCodeEnum.COUPON_OUT_OF_LIMIT);
//        }
//
//    }
//
//    private CouponVO beanProcess(CouponDO couponDO) {
//        CouponVO couponVO = new CouponVO();
//        BeanUtils.copyProperties(couponDO, couponVO);
//        return couponVO;
//    }
//}