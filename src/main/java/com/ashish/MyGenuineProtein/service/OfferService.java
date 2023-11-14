package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Category;
import com.ashish.MyGenuineProtein.model.Offer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfferService {
    List<Offer> getAllOffers();

    boolean offerExistsForProductName(String productName);

    boolean offerExistsForCategoryName(String categoryName);

    Offer addOfferCategory(Offer offer, UUID categoryId);

    Offer addOfferProduct(Offer offer, UUID productUuid);

    Offer getCategoryOffers(Category category);

  

    Optional<Offer> findOfferById(Long id);

    void saveOffer(Offer offer);

    void deleteOfferById(long id);
}
