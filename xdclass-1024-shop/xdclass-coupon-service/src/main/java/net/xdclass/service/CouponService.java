package net.xdclass.service;


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

    Map<String,Object> pageCouponActivity(int page , int size);
}
