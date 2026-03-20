package com.belak.shoppingcart.repository;

import com.belak.shoppingcart.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository  extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);
}
