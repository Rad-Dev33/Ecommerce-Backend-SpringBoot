package com.example.ecommercebackend.Service.CartItem;

import com.example.ecommercebackend.Model.Cart;
import com.example.ecommercebackend.Model.CartItem;

public interface ICartItemService {

    void addCartItem(Cart cartId, Long productId, int quantity);
    void removeCartItem(Long cartId,Long productId,String username);
    void updateCartItemQuantity(Long cartId,Long productId,int quantity,String username);

}
