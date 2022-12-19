package net.xdclass.service;

import net.xdclass.request.CartItemRequest;

public interface CartService {

    /**
     * 添加商品到购物车
     * @param cartItemRequest
     */
    public void addToCat(CartItemRequest cartItemRequest);
}
