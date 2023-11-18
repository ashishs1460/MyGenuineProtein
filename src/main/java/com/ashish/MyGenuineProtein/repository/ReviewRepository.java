package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review ,Integer> {
    List<Review> getByProductId(UUID id);
}
