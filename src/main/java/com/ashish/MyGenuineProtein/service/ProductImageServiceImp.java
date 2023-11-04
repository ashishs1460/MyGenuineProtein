package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.repository.ProductImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductImageServiceImp implements ProductImageService{
    @Autowired
    ProductImageRepository productImageRepository;
    @Override
    @Transactional
    public void deleteAllByProduct(Product product) {

        productImageRepository.deleteAllByProduct(product);

    }
}
