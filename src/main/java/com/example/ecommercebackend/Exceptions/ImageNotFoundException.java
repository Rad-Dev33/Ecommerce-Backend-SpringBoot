package com.example.ecommercebackend.Exceptions;

import org.antlr.v4.runtime.RuntimeMetaData;

public class ImageNotFoundException extends RuntimeException {
    public ImageNotFoundException(String imageNotFound) {
        super(imageNotFound);
    }
}
