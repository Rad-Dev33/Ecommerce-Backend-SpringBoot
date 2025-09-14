package com.example.ecommercebackend.Repository.Product;

import com.example.ecommercebackend.Model.Category;
import com.example.ecommercebackend.Model.Product;
import com.example.ecommercebackend.Requests.AddProductRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByCategoryName(String categoryName);
    List<Product> findByBrand(String brand);
    List<Product> findByCategoryNameAndBrand(String category, String brand);
    List<Product> findByCategoryNameAndName(String categoryname, String name);
    List<Product> findByName(String name);
    long countProductsByBrandAndName(String brand, String name);


}
