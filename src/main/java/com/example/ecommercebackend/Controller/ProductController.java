package com.example.ecommercebackend.Controller;

import com.example.ecommercebackend.Dto.ProductDto;
import com.example.ecommercebackend.Exceptions.CategoryNotFoundException;
import com.example.ecommercebackend.Exceptions.ProductNotFoundException;
import com.example.ecommercebackend.Model.Product;
import com.example.ecommercebackend.Repository.Product.ProductRepository;
import com.example.ecommercebackend.Requests.AddProductRequest;
import com.example.ecommercebackend.Requests.UpdateProductRequest;
import com.example.ecommercebackend.Response.ApiResponse;
import com.example.ecommercebackend.Service.Product.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@RestController
@RequestMapping("${api.prefix}/products")
@EnableMethodSecurity(prePostEnabled = true)
public class ProductController {

    private final ProductService productService;


    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@Valid @RequestBody AddProductRequest product) {
        try {
            ProductDto product1=productService.addProduct(product);
            return ResponseEntity.ok(new ApiResponse("Product added Sucessful", product1));
        } catch (Exception e ) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/product/{id}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest request,@PathVariable Long id) {
        try {
            Product product=productService.updateProduct(request,id);
            return ResponseEntity.ok(new ApiResponse("Product Updated Sucessful", product));
        } catch (ProductNotFoundException | CategoryNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {

        try {
            Product product=productService.getProductById(id);
            productService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("Product Deleted Sucessful", product));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

    }


    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        return ResponseEntity.ok(new  ApiResponse("All Products Found", productService.getProductsDto(productService.getAllProducts())));
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/product/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {

        try {
            ProductDto product=productService.convertToProductto(productService.getProductById(id));
            return ResponseEntity.ok(new ApiResponse("Product Found", product));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/all/{category}/category")
    public ResponseEntity<ApiResponse> getAllProductsByCategoryName(@PathVariable String category) {
        try {
            List<ProductDto> product=productService.getProductsDto(productService.getAllProductsByCategory(category));
            if(product.isEmpty()){return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products Found",null));}
            return ResponseEntity.ok(new ApiResponse("All Products Found", product));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }

    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/by/{name}/name")
    public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name) {
        try {
            List<ProductDto> products=productService.getProductsDto(productService.getProductsByName(name)) ;
            if(products.isEmpty()) {return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product Not Found", products));}
            return ResponseEntity.ok(new ApiResponse("Product Found", products));
        } catch (Exception e) {
           return  ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/by/{brand}/brand")
    public ResponseEntity<ApiResponse> getProductsByBrandName(@PathVariable String brand) {
        try {
            List<ProductDto> products=productService.getProductsDto(productService.getProductsByBrand(brand)) ;
            if(products.isEmpty()) {return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product Not Found", products));}
            return  ResponseEntity.ok(new ApiResponse("Product Found", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category, @RequestParam  String brand) {
        try {
            List<ProductDto> product=productService.getProductsDto(productService.getProductsByCategoryAndBrand(category,brand)) ;
            if(product.isEmpty()){return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product Not Found",null));}
            return ResponseEntity.ok(new ApiResponse("Products Found", product));
        } catch (Exception e) {
            return  ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/by/category-and-name")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndName(@RequestParam  String category, @RequestParam  String name) {

        try {
            List<ProductDto> product=productService.getProductsDto(productService.getProductsByCategoryAndName(category,name)) ;
            if(product.isEmpty()){return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product Not Found",null));}
            return ResponseEntity.ok(new ApiResponse("Products Found", product));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/by/count/brand-and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            long count=productService.countProductsByBrandAndName(brand,name);
            if(count==0L){return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Category and Products",null));}
            return  ResponseEntity.ok(new ApiResponse("Products Found", count));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }



}
