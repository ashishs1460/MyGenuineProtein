package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {

    void deleteAllByProduct(Product product);
}
