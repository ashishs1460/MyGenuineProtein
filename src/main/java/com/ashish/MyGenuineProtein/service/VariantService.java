package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.CartItems;
import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.model.Variant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VariantService {

    void addVariantToProduct(UUID id, Variant variant);

    void addVariant(Variant variant);



    List<Variant> getAllVariants();

   Optional<Variant> getVariantById(Long id);


   List<Variant>getVariantForProduct(Product product);

    void reduceVariantStock(List<CartItems> cartItems);
}
