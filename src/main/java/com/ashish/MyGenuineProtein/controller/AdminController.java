package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.model.Category;
import com.ashish.MyGenuineProtein.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        model.addAttribute("categories",categoryService.getAllCategory());
        return "getCategories";
    }

    @GetMapping("/admin/addCategories")
    public String addCategory(Model model){
        model.addAttribute("category",new Category());
        return "addCategories";
    }

    @PostMapping("/admin/addCategories")
    public String postCategory(@ModelAttribute("category") Category category){
        categoryService.addCategory(category);
        return "redirect:/admin/getCategories";

    }


}
