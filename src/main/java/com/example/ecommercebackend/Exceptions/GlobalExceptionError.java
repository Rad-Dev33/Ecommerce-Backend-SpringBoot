package com.example.ecommercebackend.Exceptions;

import com.example.ecommercebackend.Response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionError {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleException(MethodArgumentNotValidException ex) {


        Map<String, String> errorss = new HashMap<>();

       ex.getBindingResult().getFieldErrors().forEach((error) -> {
           errorss.put(error.getField(), error.getDefaultMessage());

       });

       return ResponseEntity.badRequest().body(new ApiResponse("Not Valid",errorss));

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleException(ConstraintViolationException ex) {
        Map<String, String> errorss = new HashMap<>();
        ex.getConstraintViolations().forEach((error) -> {
            errorss.put(error.getPropertyPath().toString(), error.getMessage());
        });

        return ResponseEntity.badRequest().body(new ApiResponse("Not Valid",errorss));
    }





}
