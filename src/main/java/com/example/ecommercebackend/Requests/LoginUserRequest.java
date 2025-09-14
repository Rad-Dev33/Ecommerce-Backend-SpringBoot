package com.example.ecommercebackend.Requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginUserRequest {

    @NotNull(message = "Email Field Should Not Be Empty")
    @Email
    private String email;

    @NotNull(message = "Password Should Not Be Empty")
    private String password;

}
