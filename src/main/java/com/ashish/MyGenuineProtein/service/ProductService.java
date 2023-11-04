package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.model.Variant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {


    void addProducts(Product product);
    List<Product> getAllProducts();

    List<Product> findAllProducts();

    void deleteProductById(UUID id);

    Optional<Product> findProductById(UUID id);

    Page<Product> getAllProductsByCategoryId(UUID id,Pageable pageable);

    boolean existsByCategoryId(UUID id);

    Page<Product> getAllProductsByVariantsById(Variant variant, Pageable pageable);

    Page<Product> findProductsWithPagination(int offset,int pageSize);


    boolean existsById(UUID id);
}
