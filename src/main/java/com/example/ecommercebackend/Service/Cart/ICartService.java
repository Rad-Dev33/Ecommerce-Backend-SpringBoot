package com.example.ecommercebackend.Service.Cart;

import com.example.ecommercebackend.Dto.CartDto;
import com.example.ecommercebackend.Model.Cart;

import java.math.BigDecimal;

public interface ICartService {

    Cart GetCart(Long cartId);
    void clearCart(Long cartId);
    BigDecimal GetTotalPrice(Long cartId);

    Cart initializeNewCart();

    Cart getCartByUserId(Long userid);

    CartDto convertCarttoDto(Cart cart);
}
