package com.example.ecommercebackend.Repository.CartItem;

import com.example.ecommercebackend.Model.Cart;
import com.example.ecommercebackend.Model.CartItem;
import com.example.ecommercebackend.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    void deleteAllByCartId(Long cartId);
    Optional<CartItem> getCartItemsByCartIdAndProductId(Long cartid, Long productid);

    CartItem getCartItemsByCartId(Long id);
}
