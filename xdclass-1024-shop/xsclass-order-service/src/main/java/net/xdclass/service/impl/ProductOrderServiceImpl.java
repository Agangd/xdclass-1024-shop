package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.model.ProductOrderDO;
import net.xdclass.mapper.ProductOrderMapper;
import net.xdclass.request.ConfirmOrderRequest;
import net.xdclass.service.ProductOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.xdclass.util.JsonData;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 阿刚
 * @since 2022-12-21
 */
@Service
@Slf4j
public class ProductOrderServiceImpl implements ProductOrderService {

    /**
     * 创建订单
     * 防重提交
     * ⽤户微服务-确认收货地址
     * 商品微服务-获取最新购物项和价格
     * 订单验价
     * 优惠券微服务-获取优惠券
     * 验证价格
     * 锁定优惠券
     * 锁定商品库存
     * 创建订单对象
     * 创建⼦订单对象
     * 发送延迟消息-⽤于⾃动关单
     * 创建支付消息-对接三方支付
     * @param request
     * @return
     */
    @Override
    public JsonData confirmOrder(ConfirmOrderRequest request) {
        return null;
    }
}
