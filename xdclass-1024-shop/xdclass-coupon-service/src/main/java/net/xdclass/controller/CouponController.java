package net.xdclass.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.xdclass.config.CouponCategoryEnum;
import net.xdclass.service.CouponService;
import net.xdclass.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 阿刚
 * @since 2022-12-08
 */
@Api("优惠券模块")
@RestController
@RequestMapping("/api/coupon/v1")
public class CouponController {


    @Autowired
    private CouponService couponService;

    @ApiOperation("优惠券分页查询")
    @GetMapping("page_coupon")
    public JsonData pageCouponList(
            @ApiParam(value = "当前页")
            @RequestParam(value = "page",defaultValue = "1") int page,

            @ApiParam(value = "每页显示多少条")
            @RequestParam(value = "size",defaultValue = "5") int size
    ){

        Map<String, Object> pageMap = couponService.pageCouponActivity(page, size);

        return JsonData.buildSuccess(pageMap);
    }


    /**
     * 领取优惠券
     * @param couponId
     * @return
     */
    @ApiOperation("领取优惠券")
    @GetMapping("/add/promotion/{coupon_id}")
    public JsonData addPromotionCoupon(
            @ApiParam(value = "优惠券id" ,required = true)
            @PathVariable("coupon_id")Long couponId){

        JsonData jsonData = couponService.addCoupon(couponId, CouponCategoryEnum.PROMOTION);
        return JsonData.buildSuccess();
    }
}

