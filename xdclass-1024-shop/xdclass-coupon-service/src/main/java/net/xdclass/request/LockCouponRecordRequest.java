package net.xdclass.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "优惠券锁定对象" ,description = "优惠券锁定对象")
@Data
public class LockCouponRecordRequest {


    /**
     * 优惠券记录ID列表
     */
    @ApiModelProperty(value = "优惠券记录ID列表" ,example = "[1,2,3]")
    private List<Long> lockCouponRecordIds;


    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号" ,example = "34566dgfxf455")
    private String OrderOutTradeNo;

}
