package com.example.ecommercebackend.Service.Category;

import com.example.ecommercebackend.Model.Category;
import com.example.ecommercebackend.Requests.AddCategoryRequest;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    Category findById(Long id);
    Category findByName(String name);
    Category addCategory(AddCategoryRequest category);
    Category updateCategory(Category category,Long id);
    List<Category> findAll();
    void deleteCategoryById(Long id);

}
