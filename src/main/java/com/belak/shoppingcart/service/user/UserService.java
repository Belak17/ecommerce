package com.belak.shoppingcart.service.user;

import com.belak.shoppingcart.dto.OrderDto;
import com.belak.shoppingcart.dto.UserDto;
import com.belak.shoppingcart.exception.AlreadyExistsException;
import com.belak.shoppingcart.exception.ResourceNotFoundException;
import com.belak.shoppingcart.model.User;
import com.belak.shoppingcart.repository.UserRepository;
import com.belak.shoppingcart.request.CreateUserRequest;
import com.belak.shoppingcart.request.UserUpdateRequest;
import com.belak.shoppingcart.service.cart.CartService;
import com.belak.shoppingcart.service.cart.ICartService;
import com.belak.shoppingcart.service.order.IOrderService;
import com.belak.shoppingcart.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements  IUserService{
    private final  UserRepository userRepository ;
    private  final ModelMapper modelMapper ;
    private final ICartService cartService ;
    private final IOrderService orderService;
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(req ->{
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(request.getPassword());
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new AlreadyExistsException("User Already Exists with this email "+request.getEmail()));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId)
                .map(existingUser ->{
                    existingUser.setFirstName(request.getFirstName());
                    existingUser.setLastName(request.getLastName());
                    return userRepository.save(existingUser);
                }).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

    }

    @Override
    public void deleteUser(Long userId) {
           userRepository.findById(userId).ifPresentOrElse(
                  userRepository ::delete ,() -> {
                       throw new ResourceNotFoundException("User Not Found");
                  });
    }

    public UserDto convertUserToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());

        // Cart
        if(user.getCart() != null) {
            dto.setCart(cartService.convertCartToDto(user.getCart()));
        }

        // Orders
        List<OrderDto> orders = user.getOrder()
                .stream()
                .map(orderService::convertToDto)
                .toList();
        dto.setOrder(orders);

        return dto;
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }
}
