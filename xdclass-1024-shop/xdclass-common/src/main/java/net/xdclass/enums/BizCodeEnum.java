package net.xdclass.enums;

import lombok.Getter;

/**
 * 枚举类，统一状态码和错误信息
 * 共6位，前三位表示服务，后三位表示接口
 * 例如 商品服务210，购物车是220，用户服务是230，403表示权限
 */
public enum BizCodeEnum {
    /**
     * 通⽤操作码
     */
    OPS_REPEAT(110001, "᯿复操作"),
    /**
     * 购物⻋
     */
    CART_FAIL(220001, "添加购物⻋失败"),
    /**
     * 验证码
     */
    CODE_TO_ERROR(240001, "接收号码不合规"),
    CODE_LIMITED(240002, "验证码发送过快"),
    CODE_ERROR(240003, "验证码错误"),
    CODE_CAPTCHA(240101, "图形验证码错误"),
    /**
     * 账号
     */
    ACCOUNT_REPEAT(250001, "账号已经存在"),
    ACCOUNT_UNREGISTER(250002, "账号不存在"),
    ACCOUNT_PWD_ERROR(250003, "账号或者密码错误"),
    /**
     * 优惠券
     */
    COUPON_CONDITION_ERROR(270001, "优惠券条件错误"),
    COUPON_UNAVAILABLE(270002, "没有可⽤的优惠券"),
    COUPON_NO_EXITS(270003, "优惠券不存在"),
    COUPON_NO_STOCK(270005, "优惠券库存不⾜"),
    COUPON_OUT_OF_LIMIT(270006, "优惠券领取超过限制 次数"),
    COUPON_OUT_OF_TIME(270407, "优惠券不在领取时间范围"),
    COUPON_GET_FAIL(270407, "优惠券领取失败"),
    COUPON_RECORD_LOCK_FAIL(270409, "优惠券锁定失败"),
    /**
     * 订单
     */
    ORDER_CONFIRM_COUPON_FAIL(280001, "创建订单-优惠券使⽤失败,不满⾜价格条件"),
    ORDER_CONFIRM_PRICE_FAIL(280002, "创建订单-验 价失败"),
    ORDER_CONFIRM_LOCK_PRODUCT_FAIL(280003, "创建订单-商品库存不⾜锁定失败"),

    ORDER_CONFIRM_ADD_STOCK_TASK_FAIL(280004, "创建订单-新增商品库存锁定任务"),
    ORDER_CONFIRM_TOKEN_NOT_EXIST(280008, "订单令牌缺少"),
    ORDER_CONFIRM_TOKEN_EQUAL_FAIL(280009, "订单令牌不正确"),
    ORDER_CONFIRM_NOT_EXIST(280010, "订单不存在"),

    ORDER_CONFIRM_CART_ITEM_NOT_EXIST(280011, "购物⻋商品项不存在"),
    /**
     * 收货地址
     */
    ADDRESS_ADD_FAIL(290001, "新增收货地址失败"),
    ADDRESS_DEL_FAIL(290002, "删除收货地址失败"),
    ADDRESS_NO_EXITS(290003, "地址不存在"),
    /**
     * ⽀付
     */
    PAY_ORDER_FAIL(300001, "创建⽀付订单失败"),
    PAY_ORDER_CALLBACK_SIGN_FAIL(300002, "⽀付订单回调验证签失败"),
    PAY_ORDER_CALLBACK_NOT_SUCCESS(300003, "创建⽀付订单失败"),
    PAY_ORDER_NOT_EXIST(300005, "订单不存在"),
    PAY_ORDER_STATE_ERROR(300006, "订单状态不正常"),
    PAY_ORDER_PAY_TIMEOUT(300007, "订单⽀付超时"),
    /**
     * 流控操作
     */
    CONTROL_FLOW(500101, "限流控制"),
    CONTROL_DEGRADE(500201, "降级控制"),
    CONTROL_AUTH(500301, "认证控制"),
    /**
     * ⽂件相关
     */
    FILE_UPLOAD_USER_IMG_FAIL(600101, "⽤户头像⽂件上传失败");


    @Getter
    private String message;

    @Getter
    private int code;

    private BizCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
