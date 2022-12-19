package net.xdclass.service.Impl;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.constant.CacheKey;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.exception.BizException;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.model.LoginUser;
import net.xdclass.request.CartItemRequest;
import net.xdclass.service.CartService;
import net.xdclass.service.ProductService;
import net.xdclass.vo.CartItemVO;
import net.xdclass.vo.ProductVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartServiceImpl implements CartService {


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ProductService productService;

    /**
     * 添加商品到购物车
     * @param cartItemRequest
     */
    @Override
    public void addToCat(CartItemRequest cartItemRequest) {

        long productId = cartItemRequest.getProductId();
        int buyNum = cartItemRequest.getBuyNum();

        //获取购物车
        BoundHashOperations<String ,Object,Object> myCart = getMyCartOps();
        Object cacheObj = myCart.get(productId);
        String result = "";

        if (cacheObj != null){
            result = (String)cacheObj;
        }

        if (StringUtils.isBlank(result)){
            //不存在则新建一个商品
            CartItemVO cartItemVO = new CartItemVO();
            ProductVO productVO = productService.findDetailById(productId);
            if (productVO == null){
                throw new BizException(BizCodeEnum.CART_FAIL);
            }

            cartItemVO.setAmount(productVO.getAmount());
            cartItemVO.setBuyNum(buyNum);
            cartItemVO.setProductId(productId);
            cartItemVO.setProductImg(productVO.getCoverImg());
            cartItemVO.setProductTitle(productVO.getTitle());

            myCart.put(productId, JSON.toJSONString(cartItemVO));
        }else {
            //存在商品，修改数量
            CartItemVO cartItemVO = JSON.parseObject(result,CartItemVO.class);
            cartItemVO.setBuyNum(cartItemVO.getBuyNum() + buyNum);
            myCart.put(productId,JSON.toJSONString(cartItemVO));
        }
    }


    /**
     * 抽取我的购物车通用方法
     * @return
     */
    private BoundHashOperations<String,Object,Object> getMyCartOps(){

        String cartKey = getCartKey();
        return redisTemplate.boundHashOps(cartKey);
    }


    /**
     * 购物车key
     * @return
     */
    private String getCartKey(){

        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        String cartKey = String.format(CacheKey.CART_KEY,loginUser.getId());
        return cartKey;
    }
}