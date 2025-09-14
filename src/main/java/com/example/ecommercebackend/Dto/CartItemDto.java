package com.example.ecommercebackend.Dto;

import com.example.ecommercebackend.Model.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {

    private Long id;
    private int quantity;
    private BigDecimal unitprice;
    private BigDecimal totalprice;
    private ProductDto product;
}
