package com.example.ecommercebackend.Service.Order;

import com.example.ecommercebackend.Dto.OrderDto;
import com.example.ecommercebackend.Model.Order;

import java.util.List;


public interface IOrderService {
    Order placeorder(Long userid);
    OrderDto getorder(Long orderid);


    List<OrderDto> getUserorders(Long userid);

    void deleteorder(Long orderid);
}
