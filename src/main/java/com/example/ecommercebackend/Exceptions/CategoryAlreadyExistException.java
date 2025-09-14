package com.example.ecommercebackend.Exceptions;

public class CategoryAlreadyExistException extends RuntimeException {
    public CategoryAlreadyExistException(String s) {
        super(s);
    }
}
