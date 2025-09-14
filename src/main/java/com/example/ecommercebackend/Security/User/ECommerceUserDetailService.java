package com.example.ecommercebackend.Security.User;

import com.example.ecommercebackend.Model.User;
import com.example.ecommercebackend.Repository.UserRepository.UserRepository;
import com.example.ecommercebackend.Service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ECommerceUserDetailService implements UserDetailsService {


    private UserService userService;

    @Autowired
    public ECommerceUserDetailService (UserService userService) {
       this.userService=userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=userService.getUserByEmail(email);
        if(user==null){throw new UsernameNotFoundException("User not found");}
        return new EcommerceUserDetails(user);
    }
}
