package com.belak.shoppingcart.repository;

import com.belak.shoppingcart.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository  extends JpaRepository<CartItem,Long> {
    void deleteAllByCartId(Long id);
}
