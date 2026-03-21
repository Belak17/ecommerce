package com.belak.shoppingcart.service.order;

import com.belak.shoppingcart.dto.OrderDto;
import com.belak.shoppingcart.model.Order;

import java.util.List;

public interface IOrderService {

    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);
}
