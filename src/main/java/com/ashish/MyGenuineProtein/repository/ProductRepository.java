package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByCategoryId(UUID id);
}
