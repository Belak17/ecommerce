package com.belak.ecommerce.service.image;

import com.belak.ecommerce.model.Image;
import com.belak.ecommerce.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    Image getImageById(Long id);

    void deleteImageById(Long id);
    Image saveImage(List<MultipartFile> files , Long productId );

    void updateImage(MultipartFile file , Long imageId);

}
