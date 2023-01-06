package net.xdclass.fegin;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import net.xdclass.util.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.spring.web.json.Json;

@FeignClient(name = "xdclass-order-service")
public interface ProductOrderFeignService {


    /**
     * 查询订单状态
     * @param outTradeNo
     * @return
     */
    @GetMapping("api/order/v1/query_state")
    JsonData queryProductOrderState(@RequestParam("out_trade_no") String outTradeNo);
}
