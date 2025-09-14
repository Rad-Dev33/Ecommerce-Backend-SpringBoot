package com.example.ecommercebackend.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EcommerceCongig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
