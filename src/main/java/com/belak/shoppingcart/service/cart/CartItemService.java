package com.belak.shoppingcart.service.cart;

import com.belak.shoppingcart.exception.ResourceNotFoundException;
import com.belak.shoppingcart.model.Cart;
import com.belak.shoppingcart.model.CartItem;
import com.belak.shoppingcart.model.Product;
import com.belak.shoppingcart.repository.CartItemRepository;
import com.belak.shoppingcart.repository.CartRepository;
import com.belak.shoppingcart.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{
    private final CartItemRepository cartItemRepository ;
    private final IProductService productService ;
    private final ICartService cartService ;
    private final CartRepository cartRepository ;
    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        // 1. Get the cart
        Cart cart =cartService.getCart(cartId);
        // 2. Get the product
        Product product= productService.getProductById(productId);
        // 3. check if the product already in the cart
         CartItem cartItem = cart.getItems()
                 .stream()
                 .filter(item -> item.getProduct().getId().equals(productId))
                 .findFirst().orElse(new CartItem());
         if (cartItem.getId() == null)
         {
             cartItem.setCart(cart);
             cartItem.setProduct(product);
             cartItem.setQuantity(quantity);
             cartItem.setUnitPrice(product.getPrice());

         }
         else
         {
             cartItem.setQuantity(cartItem.getQuantity()+quantity);
         }
         cartItem.setTotalPrice();
         cart.addItem(cartItem);
         cartItemRepository.save(cartItem);

        // 4. If yes increase the quantity with the requested quantity
        // If no , initiate a new cartItem entry
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId,productId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);

    }

//    @Override
//    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
//         Cart cart = cartService.getCart(cartId);
//         CartItem itemToUpdate = cart.getItems()
//                 .stream().filter(item -> item.getProduct().getId().equals(productId))
//                 .findFirst().orElseThrow(() -> new ResourceNotFoundException("Product not found"));
//         itemToUpdate.setQuantity(quantity);
//         itemToUpdate.setUnitPrice(itemToUpdate.getProduct().getPrice());
//         itemToUpdate.setTotalPrice();
//         cart.updateTotalAmount();
//         cartItemRepository.save(itemToUpdate);
//         cartRepository.save(cart);
//
//    }
    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getItems()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add) ;
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);

    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId)
    {
        Cart cart = cartService.getCart(cartId);
        return  cart.getItems().stream()
            .filter(item -> item.getProduct()
                    .getId().equals(productId))
            .findFirst().orElseThrow(() -> new ResourceNotFoundException("Item not Found"));
    }
}
