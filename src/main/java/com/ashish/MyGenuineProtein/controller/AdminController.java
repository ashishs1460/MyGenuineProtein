package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.model.Category;
import com.ashish.MyGenuineProtein.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.UUID;

@Controller
public class AdminController {


    @Autowired
    CategoryService categoryService;

    @GetMapping("/admin")
    public String getAdminHome(){
        return "getAdminHome";
    }

    @GetMapping("/admin/getCategories")
    public String getCategory(Model model){
        String successMessage = (String) model.getAttribute("successMessage");
        model.addAttribute("successMessage", successMessage);
        model.addAttribute("categories",categoryService.getAllCategory());
        return "getCategories";
    }

    @GetMapping("/admin/addCategories")
    public String addCategory(Model model){
        model.addAttribute("category",new Category());
        return "addCategories";
    }

    @PostMapping("/admin/addCategories")
    public String postCategory(@ModelAttribute("category") Category category, RedirectAttributes redirectAttributes){
        categoryService.addCategory(category);
        redirectAttributes.addFlashAttribute("successMessage", " successfully!");
        return "redirect:/admin/getCategories";
    }

    @GetMapping("/admin/deleteCategories/{id}")
    public String deleteCategory(@PathVariable UUID id){
        categoryService.deleteCategoryById(id);
        return "redirect:/admin/getCategories";

    }

    @GetMapping("/admin/updateCategories/{id}")
    public String updateCategory(@PathVariable UUID id ,Model model){
        Optional<Category> category=categoryService.getCategoryById(id);
        if (category.isPresent()){
            model.addAttribute("category",category.get());
            return "addCategories";
        }else {
            return "404";
        }
    }


}
