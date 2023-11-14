package com.ashish.MyGenuineProtein.service;


import com.ashish.MyGenuineProtein.model.Category;
import com.ashish.MyGenuineProtein.model.Offer;
import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.repository.OfferRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OfferServiceImp implements OfferService{

    @Autowired
    OfferRepository offerRepository;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    @Override
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

   @Override
    public boolean offerExistsForCategoryName(String categoryName) {
        return offerRepository.existsByCategory_Name(categoryName);
    }
   @Override
    public boolean offerExistsForProductName(String productName) {
        return offerRepository.existsByProduct_Name(productName);
    }

    @Override
    public Offer addOfferCategory(Offer offer, UUID categoryId) {
        Category category = categoryService.findCategoryById(categoryId).get();
        offer.setCategory(category);

         return offerRepository.save(offer);
    }

    @Override
    public Offer addOfferProduct(Offer offer, UUID productUuid) {
        Product product = productService.findProductById(productUuid).get();
        offer.setProduct(product);
        return offerRepository.save(offer);
    }

    @Override
    public Offer getCategoryOffers(Category category) {
        return offerRepository.findByCategory(category);
    }


    @Override
    public Optional<Offer> findOfferById(Long id) {
        return offerRepository.findById(id);
    }

    @Override
    public void saveOffer(Offer offer) {
        offerRepository.save(offer);
    }

    @Override
    public void deleteOfferById(long id) {
        offerRepository.deleteById(id);
    }
}
