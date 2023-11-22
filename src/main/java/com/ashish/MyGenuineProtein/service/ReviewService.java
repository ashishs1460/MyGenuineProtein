package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.model.Review;
import com.ashish.MyGenuineProtein.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewService {
    List<Review> getProductReviews(UUID id);

    void save(Review review);

    List<Review> findAll();

    void deleteById(int id);

    Optional<Review> findReviewByUserAndProduct(User user, Product product);
}
