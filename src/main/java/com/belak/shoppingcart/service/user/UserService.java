package com.belak.shoppingcart.service.user;

import com.belak.shoppingcart.exception.AlreadyExistsException;
import com.belak.shoppingcart.exception.ResourceNotFoundException;
import com.belak.shoppingcart.model.User;
import com.belak.shoppingcart.repository.UserRepository;
import com.belak.shoppingcart.request.CreateUserRequest;
import com.belak.shoppingcart.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements  IUserService{
    private UserRepository userRepository ;
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
}
