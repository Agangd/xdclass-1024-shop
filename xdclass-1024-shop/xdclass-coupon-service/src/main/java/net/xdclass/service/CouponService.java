package net.xdclass.service;


import net.xdclass.enums.CouponCategoryEnum;
import net.xdclass.util.JsonData;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 阿刚
 * @since 2022-12-08
 */
public interface CouponService{

    /**
     * 分页查询优惠券
     * @param page
     * @param size
     * @return
     */
    Map<String,Object> pageCouponActivity(int page , int size);

    /**
     * 领取优惠券接口
     * @param couponId
     * @param category
     * @return
     */
    JsonData addCoupon(long couponId, CouponCategoryEnum category);
}
