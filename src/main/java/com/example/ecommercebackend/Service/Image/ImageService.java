package com.example.ecommercebackend.Service.Image;

import com.example.ecommercebackend.Dto.ImageDto;
import com.example.ecommercebackend.Exceptions.ImageNotFoundException;
import com.example.ecommercebackend.Exceptions.ProductNotFoundException;
import com.example.ecommercebackend.Exceptions.ResourceNotFoundException;
import com.example.ecommercebackend.Model.Image;
import com.example.ecommercebackend.Model.Product;
import com.example.ecommercebackend.Repository.Image.ImageRepository;
import com.example.ecommercebackend.Repository.Product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService implements IImageService{

    private final ProductRepository productRepository;
    ImageRepository  imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository, ProductRepository productRepository) {
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
    }


    @Override
    public Image findImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Image Not Found"));
    }

    @Override
    public void deleteImageById(Long id) {
       imageRepository.findById(id).ifPresentOrElse(imageRepository::delete,()->{throw new ImageNotFoundException("Image Not Found With"+" "+id);});
    }



    @Override
    public List<ImageDto> saveImage(List<MultipartFile> file, Long productid) {
        Product product=productRepository.findById(productid).orElseThrow(()->new ProductNotFoundException("Product Not Found"));

        List<ImageDto> images=new ArrayList<>();

        for(MultipartFile multipartFile:file){
            try{

                Image image=new Image();
                image.setFileType(multipartFile.getContentType());
                image.setFileName(multipartFile.getOriginalFilename());
                image.setImage(new SerialBlob(multipartFile.getBytes()));
                image.setProduct(product);




                Image savedImage=imageRepository.save(image);
                savedImage.setUrl("/api/v1/images/image/download/"+savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto=new ImageDto();
                imageDto.setName(savedImage.getFileName());
                imageDto.setId(savedImage.getId());
                imageDto.setUrl(savedImage.getUrl());

                images.add(imageDto);



            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return images;

    }

    @Override
    public void updateImage(MultipartFile file, Long imageid) {
       Image image=findImageById(imageid);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public List<Image> getImagesByProductId(Long productId) {

        productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException("Product Not Found"));
        return imageRepository.getImagesByProductId( productId);
    }


}
