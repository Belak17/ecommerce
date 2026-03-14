package com.belak.shoppingcart.service.image;

import com.belak.shoppingcart.dto.ImageDto;
import com.belak.shoppingcart.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    Image getImageById(Long id);

    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> files , Long productId );

    void updateImage(MultipartFile file , Long imageId);

}
