package com.example.ecommercebackend.Controller;

import com.example.ecommercebackend.Dto.ImageDto;
import com.example.ecommercebackend.Exceptions.ImageNotFoundException;
import com.example.ecommercebackend.Exceptions.ResourceNotFoundException;
import com.example.ecommercebackend.Model.Image;
import com.example.ecommercebackend.Response.ApiResponse;
import com.example.ecommercebackend.Service.Image.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/images")
@EnableMethodSecurity(prePostEnabled = true)
public class ImageController {

    private IImageService imageService;

    @Autowired
    ImageController(IImageService imageService) {
        this.imageService = imageService;
    }
    private final static List<String> ALLOWED_CONTENT_TYPES=List.of("image/jpeg","image/jpg","image/png","image/webp");
    private final static List<String> ALLOWED_EXTENSIONS=List.of("jpg","jpeg","png","webp");


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("image/upload")
    ResponseEntity<ApiResponse> uploadImage(@RequestParam List<MultipartFile>  file, @RequestParam Long productId) {
        try {
            List<ImageDto> image=imageService.saveImage(file,productId);
            return ResponseEntity.ok(new ApiResponse("Image uploade Sucessful!",image));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Image upload Failed !",e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("image/download/{imageId}")
    ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {

        try {
            Image image = imageService.findImageById(imageId);

            String contentType = image.getFileType();
            String fileName = image.getFileName();


            if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
                String message = "Invalid file type. Only JPEG and PNG and WEBP are allowed.";
                ByteArrayResource error = new ByteArrayResource(message.getBytes());

                return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"error.txt\"").body(error);
            }

            if (fileName == null || ALLOWED_EXTENSIONS.stream().noneMatch(extension -> fileName.endsWith(extension))) {
                String message = "Invalid file extension";
                ByteArrayResource error = new ByteArrayResource(message.getBytes());
                return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"error.txt\"").body(error);
            }

            ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attached; filename=\"" + image.getFileName() + "\"")
                    .body(resource);
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ByteArrayResource(e.getMessage().getBytes()));
        }


    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("image/{imageId}/update")
    ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestParam MultipartFile file) {

        try {

            Image image=imageService.findImageById(imageId);
            if(image!=null){
                imageService.updateImage(file,imageId);
                return ResponseEntity.ok(new ApiResponse("Image Update Sucessful!",null));
            }

        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update Failed",INTERNAL_SERVER_ERROR));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("image/{imageId}/delete")
    ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        try {
            Image image=imageService.findImageById(imageId);
            System.out.print("image :"+image);
            if(image!=null){
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("Image Delete Sucessful!",null));
            }
        }
        catch (ResourceNotFoundException | ImageNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
        return  ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete Failed",INTERNAL_SERVER_ERROR));
    }

    @GetMapping("image/{productId}")
    ResponseEntity<List<Image>> getImagesByProductId(@PathVariable Long  productId) {

        return ResponseEntity.ok(imageService.getImagesByProductId(productId));
    }
}
