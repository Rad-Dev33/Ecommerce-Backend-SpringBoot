package com.example.ecommercebackend.Requests;

import com.example.ecommercebackend.Model.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class AddCategoryRequest {

   @NotBlank(message = "Category Name is required")
    private  String name;
    private List<Product> products;

}
