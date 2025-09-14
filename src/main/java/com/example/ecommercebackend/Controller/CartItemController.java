package com.example.ecommercebackend.Controller;

import com.example.ecommercebackend.Exceptions.ProductNotFoundException;
import com.example.ecommercebackend.Exceptions.ResourceNotFoundException;
import com.example.ecommercebackend.Model.Cart;
import com.example.ecommercebackend.Model.CartItem;
import com.example.ecommercebackend.Model.User;
import com.example.ecommercebackend.Repository.Cart.CartRepository;
import com.example.ecommercebackend.Repository.CartItem.CartItemRepository;
import com.example.ecommercebackend.Response.ApiResponse;
import com.example.ecommercebackend.Service.Cart.ICartService;
import com.example.ecommercebackend.Service.CartItem.ICartItemService;
import com.example.ecommercebackend.Service.User.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/cartitem")
@EnableMethodSecurity(prePostEnabled = true)
public class CartItemController {

    private final ICartItemService cartitemservice;
    private final ICartService cartService;
    private final IUserService userService;
    private final CartRepository cartRepository;




    @Autowired
    public CartItemController(ICartItemService cartitemservice, ICartService cartService, IUserService userService, CartRepository cartRepository) {
        this.cartitemservice = cartitemservice;
        this.cartService = cartService;
        this.userService = userService;
        this.cartRepository = cartRepository;

    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("item/add")
    public ResponseEntity<ApiResponse> addItemToCartItem( @RequestParam Long productId,@RequestParam int quantity){
        try {
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

            UserDetails userDetails=(UserDetails) authentication.getPrincipal();
            User user=userService.getUserByEmail(userDetails.getUsername());


            Cart cart= Optional.ofNullable(cartService.getCartByUserId(user.getId())).orElseGet(()->cartService.initializeNewCart());

            if(cart.getUser()==null){cart.setUser(user);}

            cartitemservice.addCartItem(cart,productId,quantity);
            return ResponseEntity.ok(new ApiResponse("Added",null));
        } catch (ResourceNotFoundException | ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("cart/{cartId}/item/{productId}/remove")
    public ResponseEntity<ApiResponse> removeCartItemFromCartItem(@PathVariable  Long cartId, @PathVariable Long productId,Authentication authentication){
        try {
                String user=authentication.getName();

            cartitemservice.removeCartItem(cartId,productId,user);
            return ResponseEntity.ok(new ApiResponse("Removed",null));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @PreAuthorize("hasRole('USER')")
    @PutMapping("item/{cartId}/product/{productId}/update")
    ResponseEntity<ApiResponse> updateTotalAmount(@PathVariable Long cartId,@PathVariable Long productId,@RequestParam int quantity,Authentication authentication){
        try {
            String username=authentication.getName();
            cartitemservice.updateCartItemQuantity(cartId,productId,quantity,username);
            return ResponseEntity.ok(new ApiResponse("Updated",null));
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }



}
