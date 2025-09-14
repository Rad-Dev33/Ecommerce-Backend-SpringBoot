package com.example.ecommercebackend.Service.Image;

import com.example.ecommercebackend.Dto.ImageDto;
import com.example.ecommercebackend.Model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    Image findImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImage(List<MultipartFile> file, Long productid);
    void updateImage(MultipartFile file, Long imageid);
    List<Image> getImagesByProductId(Long productId);

}
