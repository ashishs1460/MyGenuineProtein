package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.model.Variant;
import com.ashish.MyGenuineProtein.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService{

    @Autowired
    ProductRepository productRepository;


    @Override
    public void addProducts(Product product) {
        productRepository.save(product);
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void deleteProductById(UUID id) {
        productRepository.deleteById(id);

    }

    @Override
    public Optional<Product> findProductById(UUID id) {
        return productRepository.findById(id);
    }

    @Override
    public Page<Product> getAllProductsByCategoryId(UUID id,Pageable pageable) {
        return productRepository.findAllProductsByCategory_Id(id,pageable);
    }

    @Override
    public boolean existsByCategoryId(UUID id) {
        return productRepository.existsByCategoryId(id);
    }


    @Override
    public Page<Product> getAllProductsByVariantsById(Variant variant, Pageable pageable) {
        return productRepository.getAllProductsByVariants(variant,pageable);
    }

    @Override
    public Page<Product> findProductsWithPagination(int offset,int pageSize){
        Page<Product> products = productRepository.findAll(PageRequest.of(offset, pageSize));
        return  products;
    }

}
