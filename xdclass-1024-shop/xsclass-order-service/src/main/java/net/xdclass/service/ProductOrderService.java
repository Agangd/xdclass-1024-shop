package net.xdclass.service;

import net.xdclass.model.ProductOrderDO;
import com.baomidou.mybatisplus.extension.service.IService;
import net.xdclass.request.ConfirmOrderRequest;
import net.xdclass.util.JsonData;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 阿刚
 * @since 2022-12-21
 */
public interface ProductOrderService {


    /**
     * 创建订单
     * @param request
     * @return
     */
    JsonData confirmOrder(ConfirmOrderRequest request);


    /**
     * 查询订单状态
     * @param outTradeNo
     * @return
     */
    String queryProductOrderState(String outTradeNo);
}
