package com.example.ecommercebackend.Dto;

import com.example.ecommercebackend.Model.Category;
import com.example.ecommercebackend.Model.Image;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
public class ProductDto {

    private long id;
    private String name;
    private String brand;
    private String description;
    private double price;
    private int inventory;
    private List<ImageDto> images;
    private Category category;

}
