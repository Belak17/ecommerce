package com.belak.ecommerce.service.product;

import com.belak.ecommerce.model.Product;
import com.belak.ecommerce.request.AddProductRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest request);


    Product getProductById(Long id);
    void deleteProductById(Long id);
    void updateProduct(Product product , Long productId);

    List<Product> getProducts();
    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByCategoryAndBrand(String category,String brand);

    List<Product> getProductsByName(String name);

    List<Product> getProductsByBrandAndName(String brand,String name);

    List<Product> getProductsByCategoryAndName(String category,String name);

    Long countProductsByBrandAndName(String brand,String name);

}
