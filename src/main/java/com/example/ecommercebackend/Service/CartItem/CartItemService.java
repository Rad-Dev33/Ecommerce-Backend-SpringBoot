package com.example.ecommercebackend.Service.CartItem;

import com.example.ecommercebackend.Exceptions.ResourceNotFoundException;
import com.example.ecommercebackend.Model.Cart;
import com.example.ecommercebackend.Model.CartItem;
import com.example.ecommercebackend.Model.Product;
import com.example.ecommercebackend.Model.User;
import com.example.ecommercebackend.Repository.Cart.CartRepository;
import com.example.ecommercebackend.Repository.CartItem.CartItemRepository;
import com.example.ecommercebackend.Repository.UserRepository.UserRepository;
import com.example.ecommercebackend.Service.Cart.CartService;
import com.example.ecommercebackend.Service.Cart.ICartService;
import com.example.ecommercebackend.Service.Product.IProductService;
import com.example.ecommercebackend.Service.Product.ProductService;
import com.example.ecommercebackend.Service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartItemService implements ICartItemService {


    private final CartItemRepository cartItemRepository;
    private final ICartService cartService;
    private final IProductService productService;
    private final CartRepository cartRepository;
    private final UserService userService;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository, ICartService cartService, IProductService productService, CartRepository cartRepository, UserService userService) {
        this.cartItemRepository = cartItemRepository;
        this.cartService=cartService;
        this.productService=productService;
        this.cartRepository = cartRepository;
        this.userService=userService;
    }


    @Override
    public void addCartItem(Cart cart, Long productId, int quantity) {

        Product product=productService.getProductById(productId);


        CartItem cartItems= cartItemRepository.getCartItemsByCartIdAndProductId(cart.getId(),product.getId())
                .orElse(new CartItem()) ;


        if(cartItems.getId()==null){
            cartItems.setCart(cart);
            cartItems.setProduct(product);
            cartItems.setUnitprice(new BigDecimal(product.getPrice()));
            cartItems.setQuantity(quantity);
        }
        else {
            cartItems.setQuantity(cartItems.getQuantity()+quantity);
        }


        cartItems.setTotalprice();
        cart.addItem(cartItems);
        cartItemRepository.save(cartItems);
        cartRepository.save(cart);

    }

    @Override
    public void removeCartItem(Long cartId, Long productId,String username) {
        userService.validateUserCart(username,cartId);
        Cart cart=cartService.GetCart(cartId);
        CartItem item=cartItemRepository.getCartItemsByCartIdAndProductId(cartId,productId).
                orElseThrow(()->new ResourceNotFoundException("Product of the user not found"));
        cart.removeItem(item);
        cartRepository.save(cart);

    }


    @Override
    public void updateCartItemQuantity(Long cartId, Long productId, int quantity,String username) {
        userService.validateUserCart(username,cartId);
        Cart cart=cartService.GetCart(cartId);
        CartItem item=cartItemRepository.getCartItemsByCartIdAndProductId(cartId,productId)
                .orElseThrow(()->new ResourceNotFoundException("Product not found"));

        item.setQuantity(quantity);
        item.setTotalprice();
        cart.updateTotalAmount();
        cartRepository.save(cart);

    }

    public CartItem getCartItem(Long cartId,Long  productId) {
        return cartItemRepository.getCartItemsByCartIdAndProductId(cartId,productId)
                .orElseThrow(()->new ResourceNotFoundException("Product not found"));

    }


}
