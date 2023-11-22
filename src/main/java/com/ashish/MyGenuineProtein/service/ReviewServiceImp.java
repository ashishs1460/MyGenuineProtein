package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.model.Review;
import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewServiceImp implements ReviewService{
    @Autowired
    ReviewRepository reviewRepository;
    @Override
    public List<Review> getProductReviews(UUID id) {
        return reviewRepository.getByProductId(id);
    }

    @Override
    public void save(Review review) {
        reviewRepository.save(review);
    }

    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public void deleteById(int id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public Optional<Review> findReviewByUserAndProduct(User user, Product product) {
        return reviewRepository.findByUserAndProduct(user,product);
    }


}
