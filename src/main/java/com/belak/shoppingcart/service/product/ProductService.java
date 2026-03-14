package com.belak.shoppingcart.service.product;

import com.belak.shoppingcart.exception.ProductNotFoundException;
import com.belak.shoppingcart.exception.ResourceNotFoundException;
import com.belak.shoppingcart.model.Category;
import com.belak.shoppingcart.model.Product;
import com.belak.shoppingcart.repository.CategoryRepository;
import com.belak.shoppingcart.repository.ProductRepository;
import com.belak.shoppingcart.request.AddProductRequest;
import com.belak.shoppingcart.request.ProductUpdateRequest;

import java.util.List;
import java.util.Optional;

public class ProductService implements  IProductService {
    private final ProductRepository productRepository ;
    private  final CategoryRepository categoryRepository;
    ProductService(ProductRepository productRepository ,
                   CategoryRepository categoryRepository)
    {
        this.productRepository=productRepository ;
        this.categoryRepository=categoryRepository ;
    }
    @Override
    public Product addProduct(AddProductRequest request) {
        // check if the Product is found in the DB
        // If Yes  , set it as the new Product Category
        // If No ,then save it as a new Category
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newcategory = categoryRepository.findByName(request.getCategory().getName());
                            return categoryRepository.save(newcategory);
                        }
                    );
        // then set as a new product category
        request.setCategory(category);

        return productRepository.save(createProduct(request,category)) ;
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
}
