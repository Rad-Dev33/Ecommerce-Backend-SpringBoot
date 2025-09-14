package com.example.ecommercebackend.Service.User;

import com.example.ecommercebackend.Dto.UserDto;
import com.example.ecommercebackend.Model.User;
import com.example.ecommercebackend.Requests.CreateUserRequest;
import com.example.ecommercebackend.Requests.UpdateUserRequest;

public interface IUserService {

    User getUserById(Long userId);
    User createUser(CreateUserRequest request);

    User UpdateUser(UpdateUserRequest request, Long userId);

    void deleteUser(Long userId);

    UserDto convertUsertoDto(User user);

    User getUserByEmail(String email);
}
