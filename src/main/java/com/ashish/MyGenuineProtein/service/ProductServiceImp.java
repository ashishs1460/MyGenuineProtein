package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImp implements ProductService{

    @Autowired
    ProductRepository productRepository;


    @Override
    public void addProducts(Product product) {
        productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
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
    public List<Product> getAllProductsByCategoryId(UUID id) {
        return productRepository.findAllProductsByCategory_Id(id);
    }

    @Override
    public boolean getProductsByCategoryId(UUID id) {
        if(productRepository.findAllProductsByCategory_Id(id).isEmpty()){
            return  false;
        }
        return  true;

    }
}
