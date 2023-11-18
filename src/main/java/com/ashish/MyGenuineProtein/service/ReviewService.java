package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Review;

import java.util.List;
import java.util.UUID;

public interface ReviewService {
    List<Review> getProductReviews(UUID id);

    void save(Review review);

    List<Review> findAll();

    void deleteById(int id);
}
