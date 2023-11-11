package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.*;
import com.ashish.MyGenuineProtein.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VariantServiceImp implements VariantService {

    @Autowired
    ProductService productService;

    @Autowired
    VariantRepository variantRepository;

    @Override
    public void addVariantToProduct(UUID id, Variant variant) {
        Optional<Product> optionalProduct = productService.findProductById(id);
        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            variant.setProduct(product);
            variant.setPrice(variant.getPrice());
            variantRepository.save(variant);

        }
    }

    @Override
    public void addVariant(Variant variant) {

        variantRepository.save(variant);

    }

    @Override
    public List<Variant> getAllVariants() {
        return variantRepository.findAll();
    }

    @Override
    public Optional<Variant> getVariantById(Long id) {
        return variantRepository.findById(id);
    }

    @Override
    public List<Variant> getVariantForProduct(Product product) {
        return variantRepository.findByProduct(product);
    }

    @Override
    public void reduceVariantStock(List<CartItems> cartItems) {
        for (CartItems cartItem: cartItems) {
            Variant variant = cartItem.getVariant();
            int orderQuantity = cartItem.getQuantity();
            int currentStock = variant.getStock();
            if(currentStock >= orderQuantity){
                variant.setStock(currentStock-orderQuantity);
            }else {
                new RuntimeException("out of stock");
            }
            variantRepository.save(variant);

        }
    }

    @Override
    public void deleteById(long id) {
        variantRepository.deleteById(id);
    }

    @Override
    public void save(Variant existingVariant) {
        variantRepository.save(existingVariant);
    }

    @Override
    public void increaseVariantStock(Order orderId) {
       List<OrderItems> orderItems = orderId.getOrderItems();
        for (OrderItems item: orderItems) {
            Variant variant = item.getVariant();
            int currentStock = variant.getStock();
            int orderQuantity = item.getQuantity();
            variant.setStock(currentStock+orderQuantity);
            variantRepository.save(variant);
            System.out.println("variant added to the stock");

        }
    }


}

