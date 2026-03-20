package com.belak.shoppingcart.service.order;

import com.belak.shoppingcart.model.Order;

import java.util.List;

public interface IOrderService {

    Order placeOrder(Long userId);
    Order getOrder(Long orderId);

    List<Order> getUserOrders(Long userId);
}
