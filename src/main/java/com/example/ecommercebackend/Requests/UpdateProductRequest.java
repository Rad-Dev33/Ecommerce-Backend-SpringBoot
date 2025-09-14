package com.example.ecommercebackend.Requests;

import com.example.ecommercebackend.Model.Category;
import lombok.Data;

@Data
public class UpdateProductRequest {


    private String name;
    private String brand;
    private String description;
    private Double price;
    private Integer inventory;
    private Category category;
}
