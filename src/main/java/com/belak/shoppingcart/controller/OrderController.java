package com.belak.shoppingcart.controller;


import com.belak.shoppingcart.dto.OrderDto;
import com.belak.shoppingcart.exception.ResourceNotFoundException;
import com.belak.shoppingcart.model.Order;
import com.belak.shoppingcart.response.ApiResponse;
import com.belak.shoppingcart.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService ;

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder( @RequestParam Long userId)
    {
        try {
            Order order = orderService.placeOrder(userId);
            OrderDto orderDto = orderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("Item Order Success !",orderDto));
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error occured", e.getMessage()));
        }
    }

    @GetMapping("/{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById( @PathVariable Long orderId)
    {
        try {
            OrderDto order = orderService.getOrder(orderId);

            return  ResponseEntity.ok(new ApiResponse("Item Order Success !", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error Occured", e.getMessage()));
        }

    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<ApiResponse> getUserOrders( @PathVariable Long userId)
    {
        try {
            List<OrderDto> orders = orderService.getUserOrders(userId);
            return  ResponseEntity.ok(new ApiResponse("Item Order Success !", orders));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error Occured", e.getMessage()));
        }

    }
}
