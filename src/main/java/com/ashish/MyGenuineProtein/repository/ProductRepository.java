package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.model.Variant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Page<Product> findAllProductsByCategory_Id(UUID id, Pageable pageable);
    Page<Product> getAllProductsByVariants(Variant variant, Pageable pageable);

    boolean existsByCategoryId(UUID id);
}
