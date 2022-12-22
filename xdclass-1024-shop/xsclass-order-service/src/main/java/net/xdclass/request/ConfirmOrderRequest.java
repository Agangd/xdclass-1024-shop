package net.xdclass.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ConfirmOrderRequest {

    /**
     * 购物车使用的优惠券
     *
     * 注：传空或小于0，则不适用优惠券
     */
    @JsonProperty("coupon_record_id")
    private Long couponRecordId;

    /**
     * 最终购买的商品列表
     * 传递id,购买数量从购物车中获取
     */
    @JsonProperty("product_id_list")
    private List<Long> productIdList;

    /**
     * 支付方式
     */
    @JsonProperty("pay_type")
    private String payType;

    /**
     * 端类型
     */
    @JsonProperty("client_type")
    private String clientType;

    /**
     * 收获地址id
     */
    @JsonProperty("address_id")
    private String addressID;

    /**
     * 总价格，前端传递价格，后端需要验价
     */
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    /**
     * 实际支付价格
     * 如果用了优惠券，则是减去优惠券的价格
     * 如果没有，的等于总价格
     */
    @JsonProperty("real_pay_amount")
    private BigDecimal realPayAmount;

    /**
     * 防重令牌
     */
    private String token;
}
