package net.xdclass.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.xdclass.exception.BizException;
import net.xdclass.model.AddressDO;
import net.xdclass.service.AddressService;
import net.xdclass.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 电商-公司收发货地址表 前端控制器
 * </p>
 *
 * @author 阿刚
 * @since 2022-11-22
 */
@Api(tags = "收获地址模块")
@RestController
@RequestMapping("/api/address/v1/")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @ApiOperation("根据id查找地址详情")
    @GetMapping("find/{address_id}")
    public Object detai(
            @ApiParam(value = "地址id", required = true)
            @PathVariable("address_id") Long addressId) {
        AddressDO addressDO = addressService.detai(addressId);

//        if (addressId == 1){
//            throw new BizException(-1,"测试自定义异常");
//        }

        return JsonData.buildSuccess(addressDO);
    }
}

