package com.example.ecommercebackend.Dto;

import com.example.ecommercebackend.Enums.OrderStatus;
import com.example.ecommercebackend.Model.OrderItem;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class OrderDto {

    private Long orderId;
    private Long userid;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private List<OrderItemDto> orderItems ;


}
