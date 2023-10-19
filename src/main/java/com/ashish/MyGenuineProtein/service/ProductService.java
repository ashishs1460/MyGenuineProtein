package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {


    void addProducts(Product product);
    List<Product> getAllProducts();

    void deleteProductById(UUID id);

    Optional<Product> findProductById(UUID id);

    List<Product> getAllProductsByCategoryId(UUID id);

    boolean getProductsByCategoryId(UUID id);



}
