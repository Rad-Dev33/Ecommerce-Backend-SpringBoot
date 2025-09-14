package com.example.ecommercebackend.Requests;

import com.example.ecommercebackend.Model.Category;
import com.example.ecommercebackend.Model.Image;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class AddProductRequest {


    @NotBlank(message = "NAME IS REQUIRED")
    private String name;

    @NotBlank(message = "BRAND IS REQUIRED")
    private String brand;

    @NotBlank(message = "DESCRIPTION IS REQUIRED")
    private String description;

    @Positive(message = "PRICE MUST BE POSITIVE")
    private double price;

    @Positive(message = "INVENTORY MUST BE POSITIVE")
    private int inventory;

    @NotNull(message = "Category is required")
    @Valid
    private Category category;
}
