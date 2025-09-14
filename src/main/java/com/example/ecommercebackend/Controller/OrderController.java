package com.example.ecommercebackend.Controller;

import com.example.ecommercebackend.Dto.OrderDto;
import com.example.ecommercebackend.Exceptions.ResourceNotFoundException;
import com.example.ecommercebackend.Model.Order;
import com.example.ecommercebackend.Response.ApiResponse;
import com.example.ecommercebackend.Service.Order.IOrderService;
import com.example.ecommercebackend.Service.Order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/orders")
@EnableMethodSecurity(prePostEnabled = true)
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("order/{UserId}")
    public ResponseEntity<ApiResponse> createOrder(@PathVariable Long UserId){
        try {
            Order order = orderService.placeorder(UserId);
            OrderDto orderDto=orderService.convertToOrderDto(order);
            return ResponseEntity.ok().body(new ApiResponse("Order Placed successfully", orderDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Order  failed", e.getMessage()));
        }

    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId){
        try {
            OrderDto order=orderService.getorder(orderId);
            return ResponseEntity.ok().body(new ApiResponse("Order Found successfully", order));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("{userId}/user/order")
    public ResponseEntity<ApiResponse> getUserOrder(@PathVariable Long userId){
        try {
            List<OrderDto> orders=orderService.getUserorders(userId);
            return ResponseEntity.ok().body(new ApiResponse("Orders Found successfully", orders));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("{orderId}/delete")
    public ResponseEntity<ApiResponse> deleteOrder(@PathVariable Long orderId){
        try{
            orderService.deleteorder(orderId);
            return ResponseEntity.ok().body(new ApiResponse("Order Deleted Successfully", orderId));
        }
        catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

}
