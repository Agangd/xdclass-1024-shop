package net.xdclass.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.service.CouponRecordService;
import net.xdclass.util.JsonData;
import net.xdclass.vo.CouponRecordVO;
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
@Api(tags = "优惠券记录模块")
@RestController
@RequestMapping("/api/coupon_record/v1")
public class CouponRecordController {


    @Autowired
    private CouponRecordService couponRecordService;

    /**
     * 分页查询个人优惠券
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("分页查询个人优惠券")
    @GetMapping("page")
    public JsonData page(@ApiParam("当前页") @RequestParam(value = "page" ,defaultValue = "1") int page ,
                         @ApiParam("每页显示多少条") @RequestParam(value = "size" , defaultValue = "20") int size){

        Map<String,Object> pageResult = couponRecordService.page(page,size);

        return JsonData.buildSuccess(pageResult);
    }


    @ApiOperation("查询优惠记录详情")
    @GetMapping("detail/{record_id}")
    public JsonData getCouponRecordDetail(@PathVariable("record_id") long recordId){

        CouponRecordVO recordVO = couponRecordService.findById(recordId);

        return recordVO == null ? JsonData.buildResult(BizCodeEnum.COUPON_NO_EXITS) : JsonData.buildSuccess(recordVO);
    }
}

