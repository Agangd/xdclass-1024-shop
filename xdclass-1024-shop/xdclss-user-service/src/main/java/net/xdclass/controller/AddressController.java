package net.xdclass.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.request.AddressRequest;
import net.xdclass.service.AddressService;
import net.xdclass.util.JsonData;
import net.xdclass.vo.AddressVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @ApiOperation("新增收获地址")
    @PostMapping("add")
    public JsonData add(@ApiParam("地址对象") @RequestBody AddressRequest addressRequest) {

        addressService.add(addressRequest);

        return JsonData.buildSuccess();
    }


    /**
     * 根据id查找地址详情
     * @param addressId
     * @return
     */
    @ApiOperation("根据id查找地址详情")
    @GetMapping("find/{address_id}")
    public Object detai(
            @ApiParam(value = "地址id", required = true)
            @PathVariable("address_id") Long addressId) {
        AddressVO addressVO = addressService.detail(addressId);

//        if (addressId == 1){
//            throw new BizException(-1,"测试自定义异常");
//        }

        return addressVO == null ? JsonData.buildResult(BizCodeEnum.ADDRESS_NO_EXITS) : JsonData.buildSuccess(addressVO);
    }


    /**
     * 删除指定收获地址
     * @param addressId
     * @return
     */
    @ApiOperation("删除收获地址")
    @DeleteMapping("del/{address_id}")
    public JsonData del(
            @ApiParam(value = "地址id",required = true)
            @PathVariable("address_id") int addressId){
        int rows = addressService.del(addressId);

        return rows == 1 ? JsonData.buildSuccess() : JsonData.buildResult(BizCodeEnum.ADDRESS_DEL_FAIL);
    }
}

