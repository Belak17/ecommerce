package com.belak.shoppingcart.service.order;

import com.belak.shoppingcart.enums.OrderStatus;
import com.belak.shoppingcart.exception.ResourceNotFoundException;
import com.belak.shoppingcart.model.Cart;
import com.belak.shoppingcart.model.Order;
import com.belak.shoppingcart.model.OrderItem;
import com.belak.shoppingcart.model.Product;
import com.belak.shoppingcart.repository.OrderRepository;
import com.belak.shoppingcart.repository.ProductRepository;
import com.belak.shoppingcart.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository ;
    private  final ProductRepository productRepository ;
    private CartService cartService;

    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order,cart);
        order.setOrderItem(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return savedOrder ;
    }

    private Order createOrder (Cart cart)
    {
        Order order = new Order() ;
        order.setOrderStatus(OrderStatus.PENDING);
        // set the user
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDate.now());
        return order ;
    }
    private List<OrderItem> createOrderItems(Order order , Cart cart)
    {
        return cart.getItems()
                .stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    product.setInventory(product.getInventory()- cartItem.getQuantity());
                    productRepository.save(product);

                    return new OrderItem(
                            order ,
                            product ,
                            cartItem.getQuantity(),
                            cartItem.getUnitPrice());
                }).toList() ;
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList)
    {
        return orderItemList
                .stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO , BigDecimal::add);
    }
    @Override
    public Order getOrder(Long orderId) {
        return orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));

    }

    @Override
    public List<Order> getUserOrders(Long userId)
    {
        return orderRepository.findByUserId(userId) ;
    }
}
