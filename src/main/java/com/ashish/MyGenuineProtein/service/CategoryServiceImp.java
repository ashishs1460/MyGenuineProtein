package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Category;
import com.ashish.MyGenuineProtein.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImp implements  CategoryService{
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategoryById(UUID id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Optional<Category> getCategoryById(UUID id) {
       return categoryRepository.findById(id);
    }


}
