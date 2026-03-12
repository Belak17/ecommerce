package com.belak.ecommerce.service.image;

import com.belak.ecommerce.dto.ImageDto;
import com.belak.ecommerce.exception.ResourceNotFoundException;
import com.belak.ecommerce.model.Image;
import com.belak.ecommerce.model.Product;
import com.belak.ecommerce.repository.ImageRepository;
import com.belak.ecommerce.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements  IImageService{
    private  final ImageRepository imageRepository ;
    private final IProductService productService ;
    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Image Found"));

    }

    @Override
    public void deleteImageById(Long id) {
       imageRepository.findById(id).ifPresentOrElse(imageRepository::delete
       , () -> {
                   throw new ResourceNotFoundException("No image found with this id " + id);
               });
    }

    @Override
    public Image saveImage(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> imageDtos = new ArrayList<>();
        for (MultipartFile file : files){
            try{
                Image image = new Image() ;
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(file.getBytes());
                image.setProduct(product);


            } catch (IOException e) {

            }
        }
        return null;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {

        Image image = getImageById(imageId) ;
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileName(file.getOriginalFilename());
            image.setImage(file.getBytes());
            imageRepository.save(image);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
