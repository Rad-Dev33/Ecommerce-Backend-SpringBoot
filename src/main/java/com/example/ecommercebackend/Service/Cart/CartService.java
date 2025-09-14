package com.example.ecommercebackend.Service.Cart;

import com.example.ecommercebackend.Dto.CartDto;
import com.example.ecommercebackend.Exceptions.ResourceNotFoundException;
import com.example.ecommercebackend.Model.Cart;
import com.example.ecommercebackend.Repository.Cart.CartRepository;
import com.example.ecommercebackend.Repository.CartItem.CartItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private AtomicLong counter = new AtomicLong(0);
    private ModelMapper modelMapper;


    @Autowired
    public CartService(CartRepository cartRepository,CartItemRepository cartItemRepository,ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.modelMapper=modelMapper;
    }


    @Override
    public Cart GetCart(Long cartId) {
        Cart cart=cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFoundException("Cart Not found"));
        cart.updateTotalAmount();
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long cartId) {
        Cart cart=GetCart(cartId);
        cart.getCartitem().clear();
        cart.setTotalamount(new BigDecimal("0"));

    }

    @Override
    public BigDecimal GetTotalPrice(Long cartId) {
        Cart cart=GetCart(cartId);
        cart.updateTotalAmount();
        return cart.getTotalamount();
    }

    @Override
    public Cart initializeNewCart(){
        Cart cart=new Cart();
       return cartRepository.save(cart);

    }

    @Override
    public Cart getCartByUserId(Long userid) {
        return cartRepository.findByUserId(userid);
    }

    @Override
    public CartDto convertCarttoDto(Cart cart) {
        return modelMapper.map(cart,CartDto.class);
    }
}
