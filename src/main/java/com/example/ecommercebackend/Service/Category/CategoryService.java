package com.example.ecommercebackend.Service.Category;

import com.example.ecommercebackend.Exceptions.CategoryAlreadyExistException;
import com.example.ecommercebackend.Exceptions.CategoryNotFoundException;
import com.example.ecommercebackend.Model.Category;
import com.example.ecommercebackend.Repository.Category.CategoryRepository;
import com.example.ecommercebackend.Requests.AddCategoryRequest;
import com.example.ecommercebackend.Service.Product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService{

    private CategoryRepository categoryRepository;

    @Autowired
    CategoryService(CategoryRepository categoryRepository) {this.categoryRepository=categoryRepository;}

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()->new CategoryNotFoundException("Category Not Found"));
    }

    @Override
    public Category findByName(String name) {

        Category category=categoryRepository.findByName(name);

        if(category==null){throw new CategoryNotFoundException("Category Not Found");}
        return category;

    }

    @Override
    public Category addCategory(AddCategoryRequest request) {
        Category category=categoryRepository.findByName(request.getName().trim());
        if(category!=null){throw new CategoryAlreadyExistException(request.getName()+" "+"Already Exist");}
        return categoryRepository.save(new Category(request.getName().trim()));
    }


    @Override
    public Category updateCategory(Category category,Long id) {
        return Optional.ofNullable(findById(id)).map(oldCategory->{
            oldCategory.setName(category.getName().trim());
            return categoryRepository.save(oldCategory);
        }).orElseThrow(() -> new CategoryNotFoundException("Category Not Found"));
    }



    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void deleteCategoryById(Long id) {
        Category category=categoryRepository.findById(id).orElseThrow(()->new CategoryNotFoundException("Category Not Found"));
        categoryRepository.delete(category);
    }
}
