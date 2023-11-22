package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.model.Review;
import com.ashish.MyGenuineProtein.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review ,Integer> {
    List<Review> getByProductId(UUID id);

    Optional<Review> findByUserAndProduct(User user, Product product);
}
