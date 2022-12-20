package net.xdclass.service;

import net.xdclass.request.CartItemRequest;
import net.xdclass.vo.CartVO;

public interface CartService {

    /**
     * 添加商品到购物车
     * @param cartItemRequest
     */
    void addToCat(CartItemRequest cartItemRequest);

    /**
     * 清空购物车
     */
    void clear();

    CartVO getMyCart();
}
