package com.belak.ecommerce.service.product;

import com.belak.ecommerce.exception.ProductNotFoundException;
import com.belak.ecommerce.model.Category;
import com.belak.ecommerce.model.Product;
import com.belak.ecommerce.repository.ProductRepository;
import com.belak.ecommerce.request.AddProductRequest;

import java.util.List;

public class ProductService implements  IProductService {
    private final ProductRepository productRepository ;
    ProductService(ProductRepository productRepository)
    {
        this.productRepository=productRepository ;
    }
    @Override
    public Product addProduct(AddProductRequest request) {
        return null ;
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
    public void updateProduct(Product product, Long productId) {

    }

    @Override
    public List<Product> getProducts() {
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
}
