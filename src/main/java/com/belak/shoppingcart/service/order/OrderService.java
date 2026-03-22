package com.belak.shoppingcart.service.order;

import com.belak.shoppingcart.dto.OrderDto;
import com.belak.shoppingcart.dto.OrderItemDto;
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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository ;
    private  final ProductRepository productRepository ;
    private final CartService cartService;
    private final ModelMapper modelMapper ;
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
    public OrderDto getOrder(Long orderId) {
        return orderRepository
                .findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));

    }

    @Override
    public List<OrderDto> getUserOrders(Long userId)
    {
        List<Order> orders=orderRepository.findByUserId(userId) ;
        return orders.stream().map(this:: convertToDto).toList();
    }

    @Override
    public OrderDto convertToDto(Order order)
    {
        OrderDto dto = modelMapper.map(order, OrderDto.class);

        // mapping manuel obligatoire
        List<OrderItemDto> items = order.getOrderItem()
                .stream()
                .map(item -> {
                    OrderItemDto itemDto = new OrderItemDto();
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setPrice(item.getPrice());
                    // AJOUTE cA
                    itemDto.setProductId(item.getProduct().getId());
                    itemDto.setProductName(item.getProduct().getName());
                    itemDto.setProductBrand(item.getProduct().getBrand());
                    return itemDto;
                })
                .toList();

        dto.setOrderItem(items);

        return dto;
    }
}
