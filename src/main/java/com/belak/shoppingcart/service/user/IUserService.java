package com.belak.shoppingcart.service.user;


import com.belak.shoppingcart.dto.UserDto;
import com.belak.shoppingcart.model.User;
import com.belak.shoppingcart.request.CreateUserRequest;
import com.belak.shoppingcart.request.UserUpdateRequest;

public interface IUserService {

    User getUserById(Long userId);

    User createUser(CreateUserRequest request);

    User updateUser(UserUpdateRequest request , Long userId);

    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);
}
