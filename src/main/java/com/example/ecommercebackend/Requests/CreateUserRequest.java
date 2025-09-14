package com.example.ecommercebackend.Requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

@Data
public class CreateUserRequest {

    @NotNull(message = "FirstName Cannot Be Empty")
    @Size(min = 3, max = 20,message = "Name Should be At Least 3 Characters")
    @Pattern(regexp = "^(?i)[A-Za-z]+$",message = "Enter a Valid FirstName")
    private String firstname;

    @NotNull(message = "LastName Cannot Be Empty")
    @Size(min = 3, max = 20,message = "LastName Should be At Least 3 Characters")
    @Pattern(regexp = "^(?i)[A-Za-z]+$",message = "Enter a Valid LastName")
    private String lastname;


    @Email
    private String email;

    @NotNull(message = "password cannot Be Empty")
    @Size(min = 6,message = "Password should be at least 6 Characters")
    private String password;
}
