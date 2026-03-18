package com.belak.shoppingcart.service.product;

import com.belak.shoppingcart.dto.ProductDto;
import com.belak.shoppingcart.model.Product;
import com.belak.shoppingcart.request.AddProductRequest;
import com.belak.shoppingcart.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest request);


    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product , Long productId);


    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByCategoryAndBrand(String category,String brand);

    List<Product> getProductsByName(String name);

    List<Product> getProductsByBrandAndName(String brand,String name);

    List<Product> getProductsByCategoryAndName(String category,String name);

    Long countProductsByBrandAndName(String brand,String name);

    List<Product> getAllProducts();

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
