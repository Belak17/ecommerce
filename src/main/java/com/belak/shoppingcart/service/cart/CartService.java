package com.belak.shoppingcart.service.cart;

import com.belak.shoppingcart.dto.CartDto;
import com.belak.shoppingcart.dto.CartItemDto;
import com.belak.shoppingcart.dto.ProductDto;
import com.belak.shoppingcart.exception.ResourceNotFoundException;
import com.belak.shoppingcart.model.Cart;
import com.belak.shoppingcart.model.Product;
import com.belak.shoppingcart.model.User;
import com.belak.shoppingcart.repository.CartItemRepository;
import com.belak.shoppingcart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService implements  ICartService{
    private final CartRepository cartRepository ;
    private final CartItemRepository cartItemRepository ;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);
    @Override
    public Cart getCart(Long id) {
        //BigDecimal totalAmount = cart.getTotalAmount() ;
        //cart.setTotalAmount(totalAmount);
        return cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id) ;
        cart.getItems().clear();
        cartRepository.deleteById(id);


    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);

        return cart.getTotalAmount() ;
    }

    @Override
    public Cart initializeNewCart(User user)
    {
        //Cart newCart = new Cart();
        //return cartRepository.save(newCart).getId();
        // Hibernate gère l'ID
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(() -> {
                    Cart cart = new Cart() ;
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId) ;
    }

    @Override
    public CartDto convertCartToDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setCartId(cart.getId());
        dto.setTotalAmount(cart.getTotalAmount());

        List<CartItemDto> items = cart.getItems()
                .stream()
                .map(item -> {
                    CartItemDto dtoItem = new CartItemDto();
                    dtoItem.setItemId(item.getId());
                    dtoItem.setQuantity(item.getQuantity());
                    dtoItem.setUnitPrice(item.getUnitPrice());

                    // Mapping du produit
                    Product product = item.getProduct();
                    if (product != null) {
                        ProductDto productDto = new ProductDto();
                        productDto.setId(product.getId());
                        productDto.setName(product.getName());
                        productDto.setPrice(product.getPrice());
                        dtoItem.setProduct(productDto);
                    }

                    return dtoItem;
                }).toList();

        dto.setItems(items);

        return dto;
    }

}
