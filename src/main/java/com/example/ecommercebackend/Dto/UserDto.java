package com.example.ecommercebackend.Dto;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private List<OrderDto> order;
    private CartDto cart;
}
