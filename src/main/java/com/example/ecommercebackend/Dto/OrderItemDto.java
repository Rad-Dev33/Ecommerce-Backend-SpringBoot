package com.example.ecommercebackend.Dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long productId;
    private String productName;
    private String Brand;
    private BigDecimal productPrice;
    private int quantity;
}
