package com.example.ecommercebackend.Controller;

import com.example.ecommercebackend.Exceptions.ResourceNotFoundException;
import com.example.ecommercebackend.Model.Cart;
import com.example.ecommercebackend.Response.ApiResponse;
import com.example.ecommercebackend.Service.Cart.ICartService;
import com.example.ecommercebackend.Service.Product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;


@RestController
@RequestMapping("${api.prefix}/cart")
public class CartController {
    public final ICartService cartService;


    @Autowired
    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{cartId}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable  Long cartId){
        try {
            Cart cart=cartService.GetCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Success",cart));
        } catch (ResourceNotFoundException e) {
           return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),NOT_FOUND));
        }

    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable  Long cartId){

        try {
            cartService.clearCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Cart cleared",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),NOT_FOUND));
        }
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{cartId}/cart/totalprice")
    public ResponseEntity<ApiResponse> getTotalAmountOfCart(@PathVariable  Long cartId){
        try {
            BigDecimal total=cartService.GetTotalPrice(cartId);
            return ResponseEntity.ok(new ApiResponse("Sucess",total));
        } catch (ResourceNotFoundException e) {
            return   ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }


}
