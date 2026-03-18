package com.belak.shoppingcart.service.cart;

import com.belak.shoppingcart.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id );
    BigDecimal getTotalPrice(Long id);


}
