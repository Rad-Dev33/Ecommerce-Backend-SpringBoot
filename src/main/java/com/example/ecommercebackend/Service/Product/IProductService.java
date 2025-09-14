package com.example.ecommercebackend.Service.Product;

import com.example.ecommercebackend.Dto.ProductDto;
import com.example.ecommercebackend.Model.Product;
import com.example.ecommercebackend.Requests.AddProductRequest;
import com.example.ecommercebackend.Requests.UpdateProductRequest;

import java.util.List;

public interface IProductService {

    ProductDto addProduct(AddProductRequest request);
    Product getProductById(long id);
    void deleteProductById(long id);
    public Product updateProduct(UpdateProductRequest request, long id);
    List<Product> getAllProducts();
    List<Product> getAllProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category,String brand);
    List<Product> getProductsByCategoryAndName(String category,String name);



    List<Product> getProductsByName(String name);
    long countProductsByBrandAndName(String brand, String name);


    List<ProductDto> getProductsDto(List<Product> products);

    ProductDto convertToProductto(Product product);
}
