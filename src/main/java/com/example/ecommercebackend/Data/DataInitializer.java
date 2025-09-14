package com.example.ecommercebackend.Data;

import com.example.ecommercebackend.Model.User;
import com.example.ecommercebackend.Repository.UserRepository.UserRepository;
import com.example.ecommercebackend.Service.User.UserService;
import jdk.jfr.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
//@RequiredArgsConstructor
//public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public void onApplicationEvent(ApplicationReadyEvent event) {
//        createUserIfNotExist();
//    }
//
//    public  void createUserIfNotExist(){
//        for(int i=0;i<10;i++){
//
//            if(userRepository.existsByEmail("user"+i+"@gmail.com")) continue;
//            User user = new User();
//            user.setFirstname("user"+i);
//            user.setLastname("userlast"+i);
//            user.setEmail("user"+i+"@gmail.com");
//            user.setPassword(new BCryptPasswordEncoder().encode("12345"));
//            userRepository.save(user);
//
//        }
//    }
//}
