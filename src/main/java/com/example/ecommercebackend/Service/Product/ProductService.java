package com.example.ecommercebackend.Service.Product;

import com.example.ecommercebackend.Dto.ImageDto;
import com.example.ecommercebackend.Dto.ProductDto;
import com.example.ecommercebackend.Exceptions.CategoryNotFoundException;
import com.example.ecommercebackend.Exceptions.ProductNotFoundException;
import com.example.ecommercebackend.Model.Category;
import com.example.ecommercebackend.Model.Image;
import com.example.ecommercebackend.Model.Product;
import com.example.ecommercebackend.Repository.Category.CategoryRepository;
import com.example.ecommercebackend.Repository.Image.ImageRepository;
import com.example.ecommercebackend.Repository.Product.ProductRepository;
import com.example.ecommercebackend.Requests.AddProductRequest;
import com.example.ecommercebackend.Requests.UpdateProductRequest;
import com.example.ecommercebackend.Service.Category.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService{

    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;


    @Autowired
    ProductService(ProductRepository productRepository, CategoryService categoryService, CategoryRepository categoryRepository, ModelMapper modelMapper, ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.imageRepository = imageRepository;
    }




    @Override
    public ProductDto addProduct(AddProductRequest request) {

        if(request.getCategory().getName().isBlank()){throw new IllegalArgumentException("Category name is required");}
        Category category=Optional.ofNullable(categoryService.findByName(request.getCategory().getName()))
                .orElseGet(()->categoryRepository.save(new Category(request.getCategory().getName().trim())));

        request.setCategory(category);
        Product product=createProduct(request,category);
         return convertToProductto(productRepository.save(product));

    }


    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName().trim(),
                request.getBrand().trim(),
                request.getDescription().trim(),
                request.getPrice(),
                request.getInventory(),
                category
        );
    }


    @Override
    public Product getProductById(long id) {
        return productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Product Not Found"));
    }

    @Override
    public void deleteProductById(long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,()->{throw new ProductNotFoundException("Product Not Found");});
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, long id) {

        Product product=productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Product Not Found"));

        Product updated=UpdateexistingProduct(product,request);
        return productRepository.save(updated);


    }

    private Product UpdateexistingProduct(Product product,UpdateProductRequest request) {

        if( request.getCategory()!=null ){
            if(request.getCategory().getName().isBlank()){throw new CategoryNotFoundException("Please Enter Valid Category Name");}
            Category category=categoryService.findByName(request.getCategory().getName().trim());
           product.setCategory(category);

        }

        if(request.getName()!=null ){product.setName(request.getName().trim());}
        if(request.getPrice()!=null && request.getPrice()!=product.getPrice()){product.setPrice(request.getPrice());}
        if(request.getDescription()!=null ){product.setDescription(request.getDescription().trim());}
        if(request.getInventory()!=null && request.getInventory()!=product.getInventory()){product.setInventory(request.getInventory());}
        if(request.getBrand()!=null ){product.setBrand(request.getBrand().trim());}


        return product;


    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndName(String category, String name) {
        return productRepository.findByCategoryNameAndName(category,name);
    }




    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countProductsByBrandAndName(brand,name);
    }

    @Override
    public List<ProductDto> getProductsDto(List<Product> products) {
        return getAllProducts().stream().
                map(product -> modelMapper.map(product,ProductDto.class)).collect(Collectors.toList());
    }

    @Override
    public ProductDto convertToProductto(Product product){
        ProductDto productDto=  modelMapper.map(product,ProductDto.class);
        List<Image> images=imageRepository.getImagesByProductId(product.getId());
        List<ImageDto> imageDtos=images.stream()
                .map(image -> modelMapper.map(image,ImageDto.class))
                .collect(Collectors.toList());
        productDto.setImages(imageDtos);
        return productDto;

    }
}
