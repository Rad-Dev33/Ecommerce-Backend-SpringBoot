package com.example.ecommercebackend.Controller;

import com.example.ecommercebackend.Dto.CartDto;
import com.example.ecommercebackend.Dto.OrderDto;
import com.example.ecommercebackend.Dto.OrderItemDto;
import com.example.ecommercebackend.Dto.UserDto;
import com.example.ecommercebackend.Exceptions.ResourceNotFoundException;
import com.example.ecommercebackend.Model.Cart;
import com.example.ecommercebackend.Model.OrderItem;
import com.example.ecommercebackend.Model.User;
import com.example.ecommercebackend.Repository.UserRepository.UserRepository;
import com.example.ecommercebackend.Requests.CreateUserRequest;
import com.example.ecommercebackend.Requests.LoginUserRequest;
import com.example.ecommercebackend.Requests.UpdateUserRequest;
import com.example.ecommercebackend.Response.ApiResponse;
import com.example.ecommercebackend.Security.Service.AuthService;
import com.example.ecommercebackend.Service.Cart.CartService;
import com.example.ecommercebackend.Service.Cart.ICartService;
import com.example.ecommercebackend.Service.Order.IOrderService;
import com.example.ecommercebackend.Service.Order.OrderService;
import com.example.ecommercebackend.Service.User.IUserService;
import com.example.ecommercebackend.Service.User.UserService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping("${api.prefix}/user")
@EnableMethodSecurity(prePostEnabled = true)
public class UserController {

    private final  UserService userService;
    private final OrderService orderService;
    private final CartService cartService;
    private final AuthService authService;


    @Autowired
    public UserController(UserService userService,OrderService orderService, CartService cartService,AuthService authService) {
        this.userService = userService;
        this.orderService = orderService;
        this.cartService = cartService;
        this.authService = authService;

    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable  Long userId) {
        try {
            User user= userService.getUserById(userId);
            UserDto userdto=userService.convertUsertoDto(user);


//            List<OrderDto> orderDtos= orderService.getUserorders(userId);
//            userdto.setOrder(orderDtos);
//
//
//            Cart cart=cartService.getCartByUserId(userId);
//            CartDto cartDto= cartService.convertCarttoDto(cart);
//            userdto.setCart(cartDto);

            return ResponseEntity.ok().body(new ApiResponse("success", userdto));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("error", e.getMessage()));
        }
    }




    @PostMapping("/create")
    public ResponseEntity<ApiResponse> getUserByEmail(@Valid @RequestBody CreateUserRequest createUserRequest) {


        try {
            User user=userService.createUser(createUserRequest);
            UserDto userdto=userService.convertUsertoDto(user);
            return ResponseEntity.ok().body(new ApiResponse("success", userdto));
        }
        catch (ResourceNotFoundException e) {
           return ResponseEntity.status(CONFLICT).body(new ApiResponse("error", e.getMessage()));
        }

    }





    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@Valid @RequestBody LoginUserRequest userRequest) {

        try {
            String token=authService.verify(userRequest);
            return ResponseEntity.ok().body(new ApiResponse("Success", token));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse("error", e.getMessage()));
        }

    }


    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest updateUserRequest) {
        try {
            User user= userService.UpdateUser(updateUserRequest,userId);
            UserDto userdto=userService.convertUsertoDto(user);
            return ResponseEntity.ok().body(new ApiResponse("success", userdto));
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse("error", e.getMessage()));
        }
    }



    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {

        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok().body(new ApiResponse("success", null));
        } catch (ResourceNotFoundException e) {
           return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse("error", e.getMessage()));
        }

    }

}
