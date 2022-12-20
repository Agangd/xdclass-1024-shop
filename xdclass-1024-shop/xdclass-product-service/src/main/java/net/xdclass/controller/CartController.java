package net.xdclass.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.xdclass.request.CartItemRequest;
import net.xdclass.service.CartService;
import net.xdclass.util.JsonData;
import net.xdclass.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "购物车")
@RequestMapping("/api/cart/v1")
@RestController
public class CartController {

    @Autowired
    private CartService cartService;


    @ApiOperation("添加到购物车")
    @PostMapping("add")
    public JsonData addToCart(@ApiParam("购物项") @RequestBody CartItemRequest cartItemRequest){

        cartService.addToCat(cartItemRequest);

        return JsonData.buildSuccess();
    }


    @ApiOperation("清空购物车")
    @DeleteMapping("clean")
    public JsonData cleanMyCart(){

        cartService.clear();
        return JsonData.buildSuccess("清空购物车成功");
    }


    @ApiOperation("查看我的购物车")
    @GetMapping("mycart")
    public JsonData findMyCart(){

        CartVO cartVO = cartService.getMyCart();

        return JsonData.buildSuccess(cartVO);
    }

}
