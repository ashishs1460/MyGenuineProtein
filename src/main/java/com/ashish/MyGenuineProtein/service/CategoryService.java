package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryService {


    List<Category> getAllCategory();
    void addCategory (Category category);

    void deleteCategoryById(UUID id);

    Optional<Category> getCategoryById(UUID id);


    boolean getCategoryByName(String name);



}
