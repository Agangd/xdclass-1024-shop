package net.xdclass.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public class CartVO {

    /**
     * 购物项
     */
    @JsonProperty("cart_items")
    private List<CartItemVO> cartItems;

    /**
     * 购买总件数
     */
    @JsonProperty("total_num")
    private Integer totalNum;

    /**
     * 购物车总价格
     */
    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    /**
     * 购物车实际支付价格
     */
    @JsonProperty("real_pay_price")
    private BigDecimal realPayPrice;

    public Integer getTotalNum() {
        if (this.cartItems != null){
            int total = cartItems.stream().mapToInt(CartItemVO::getBuyNum).sum();
            return total;
        }
        return 0;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal amount = new BigDecimal("0");
        if (this.cartItems != null){
            for (CartItemVO cartItemVO : cartItems){
                BigDecimal itemAmount  = cartItemVO.getTotalAmount();
                amount = amount.add(itemAmount);
            }
        }
        return amount;
    }

    public BigDecimal getRealPayPrice() {
        BigDecimal amount = new BigDecimal("0");
        if (this.cartItems != null){
            for (CartItemVO cartItemVO : cartItems){
                BigDecimal itemAmount  = cartItemVO.getTotalAmount();
                amount = amount.add(itemAmount);
            }
        }
        return amount;
    }

    public List<CartItemVO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemVO> cartItems) {
        this.cartItems = cartItems;
    }
}
