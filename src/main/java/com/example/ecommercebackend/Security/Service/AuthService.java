package com.example.ecommercebackend.Security.Service;

import com.example.ecommercebackend.Exceptions.ResourceNotFoundException;
import com.example.ecommercebackend.Model.User;
import com.example.ecommercebackend.Requests.LoginUserRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private AuthenticationManager authenticationManager;
    private JWTService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JWTService jwtService){
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
    }
    public String verify(LoginUserRequest user){
        Authentication auth=authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));
        if(auth.isAuthenticated())return jwtService.generateToken(user.getEmail());
        else throw new ResourceNotFoundException("User not found! Please check your username and password.");

    }
}
