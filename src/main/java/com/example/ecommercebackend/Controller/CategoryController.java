package com.example.ecommercebackend.Controller;

import com.example.ecommercebackend.Exceptions.CategoryAlreadyExistException;
import com.example.ecommercebackend.Exceptions.CategoryNotFoundException;
import com.example.ecommercebackend.Model.Category;
import com.example.ecommercebackend.Requests.AddCategoryRequest;
import com.example.ecommercebackend.Response.ApiResponse;
import com.example.ecommercebackend.Service.Category.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/categorys")
@EnableMethodSecurity(prePostEnabled = true)
public class CategoryController {

    private ICategoryService categoryService;

    @Autowired
    public void setCategoryService(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@Valid @RequestBody AddCategoryRequest request){
        try{
            Category category1=categoryService.addCategory(request);
            return ResponseEntity.ok(new ApiResponse("Category Added Sucessfully",category1));
        }
        catch(CategoryAlreadyExistException e){
           return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("category/{categoryId}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long categoryId){
        try{
            Category category=categoryService.findById(categoryId);
            categoryService.deleteCategoryById(category.getId());
            return ResponseEntity.ok(new ApiResponse("Category Deleted Sucessfully",category));


        } catch ( CategoryNotFoundException e) {
           return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("category/{categoryId}/update")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody Category category,@PathVariable Long categoryId){

        try{
            Category category1=categoryService.updateCategory(category,categoryId);
            return ResponseEntity.ok(new ApiResponse("Category Updated Sucessfully",category1));
        }
        catch(CategoryNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories(){

        try {
            List<Category> categories=categoryService.findAll();
            return ResponseEntity.ok(new ApiResponse("Fetched All the Category",categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/category/{categoryId}/id")
    public ResponseEntity<ApiResponse>  getCategoryById(@PathVariable Long categoryId){
        try {
            Category category=categoryService.findById(categoryId);
            return  ResponseEntity.ok(new ApiResponse("Category Found",category));
        }
        catch(CategoryNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/category/{name}/name")
    public ResponseEntity<ApiResponse>  getCategoryByName(@PathVariable String name){
        try {
            Category category=categoryService.findByName(name.trim());
            return  ResponseEntity.ok(new ApiResponse("Category Found",category));
        }
        catch( CategoryNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    




}
