package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Category;
import com.ashish.MyGenuineProtein.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    

    boolean existsByCategory_Name(String categoryName);

    boolean existsByProduct_Name(String productName);

    Offer  findByCategory(Category category);
}
