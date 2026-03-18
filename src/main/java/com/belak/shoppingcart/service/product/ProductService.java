package com.belak.shoppingcart.service.product;

import com.belak.shoppingcart.dto.ImageDto;
import com.belak.shoppingcart.dto.ProductDto;
import com.belak.shoppingcart.exception.ProductNotFoundException;
import com.belak.shoppingcart.exception.ResourceNotFoundException;
import com.belak.shoppingcart.model.Category;
import com.belak.shoppingcart.model.Image;
import com.belak.shoppingcart.model.Product;
import com.belak.shoppingcart.repository.CategoryRepository;
import com.belak.shoppingcart.repository.ImageRepository;
import com.belak.shoppingcart.repository.ProductRepository;
import com.belak.shoppingcart.request.AddProductRequest;
import com.belak.shoppingcart.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements  IProductService {
    private final ProductRepository productRepository ;
    private  final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper ;
    private final ImageRepository imageRepository ;
    //ProductService(ProductRepository productRepository ,
     //              CategoryRepository categoryRepository)
    //{
      //  this.productRepository=productRepository ;
        //this.categoryRepository=categoryRepository ;
    //}
    @Override
    public Product addProduct(AddProductRequest request) {

        // check if the Product is found in the DB
        // If Yes  , set it as the new Product Category
        // If No ,then save it as a new Category
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newcategory = new Category();
                    newcategory.setName(request.getCategory().getName());
                    return categoryRepository.save(newcategory);
                });

        // then set as a new product category
        request.setCategory(category);

        return productRepository.save(createProduct(request,category));
    }

    private Product createProduct(AddProductRequest request , Category category)
    {
        return new Product(request.getName(), request.getBrand(),request.getPrice()
        ,request.getInventory(), request.getDescription(), category) ;
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Product not found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete ,
                        () -> {throw new ProductNotFoundException("Product Not Found");});

    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {

        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct,request))
                .map(productRepository ::save)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll() ;
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand) ;
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public List<Product> getProductsByCategoryAndName(String category, String name) {
        return productRepository.findByCategoryNameAndName(category,name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }

    private Product updateExistingProduct(Product existingProduct , ProductUpdateRequest request)
    {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setInventory(request.getInventory());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);

        return  existingProduct ;
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products)
    {
        return products.stream().map(this::convertToDto).toList();
    }
    @Override
    public ProductDto convertToDto(Product product)
    {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image,ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto ;

    }
}
