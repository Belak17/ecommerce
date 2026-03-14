package com.belak.shoppingcart.service.image;

import com.belak.shoppingcart.dto.ImageDto;
import com.belak.shoppingcart.exception.ResourceNotFoundException;
import com.belak.shoppingcart.model.Image;
import com.belak.shoppingcart.model.Product;
import com.belak.shoppingcart.repository.ImageRepository;
import com.belak.shoppingcart.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile file : files){
            try{
                Image image = new Image() ;
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(file.getBytes());
                image.setProduct(product);
                String buildDownloadUrl = "/api/v1/images/image/donwload" ;
                String downloadUrl = buildDownloadUrl+ image.getId();
                image.setDownloadUrl(downloadUrl);
                Image savedImage = imageRepository.save(image);
                 savedImage.setDownloadUrl(buildDownloadUrl+savedImage.getId());
                 imageRepository.save(savedImage);

                 ImageDto imageDto = new ImageDto() ;
                 imageDto.setImageId(savedImage.getId());
                 imageDto.setImageName(savedImage.getFileName());
                 savedImage.setDownloadUrl(savedImage.getDownloadUrl());
                 savedImageDto.add(imageDto);
            } catch (IOException e) {
                throw  new RuntimeException(e.getMessage());

            }
        }
        return savedImageDto ;
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
