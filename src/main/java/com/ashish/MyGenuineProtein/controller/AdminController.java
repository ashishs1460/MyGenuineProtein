package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.model.Category;
import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.service.CategoryService;
import com.ashish.MyGenuineProtein.service.ProductService;
import com.ashish.MyGenuineProtein.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Controller
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @GetMapping("/admin")
    public String getAdminHome(Model model, Principal principal){
        User user = userService.findUserByEmail(principal.getName()).get();
        String username = user.getFirstName();

        model.addAttribute("username",username);

        return "getAdminHome";
    }

    @GetMapping("/admin/getCategories")
    public String getCategory(Model model){
        String successMessage = (String) model.getAttribute("successMessage");
        model.addAttribute("successMessage", successMessage);
        model.addAttribute("categories",categoryService.getAllCategory());
        return "/category/getCategories";
    }

    @GetMapping("/admin/addCategories")
    public String addCategory(Model model){
        model.addAttribute("category",new Category());
        return "/category/addCategories";
    }

    @PostMapping("/admin/addCategories")
    public String postCategory(@ModelAttribute("category") Category category, RedirectAttributes redirectAttributes){
       boolean isPresent=categoryService.getCategoryByName(category.getName());
        if(isPresent){
            redirectAttributes.addFlashAttribute("CategoryPresent","Category is already available , try to add another category");
            return "redirect:/admin/getCategories";
        }
        categoryService.addCategory(category);
        redirectAttributes.addFlashAttribute("successMessage", " successfully!");
        return "redirect:/admin/getCategories";
    }

    @GetMapping("/admin/deleteCategories/{id}")
    public String deleteCategory(@PathVariable UUID id ,Model model ,RedirectAttributes redirectAttributes){
        boolean isPresent = productService.existsByCategoryId(id);
        if(isPresent){
            redirectAttributes.addFlashAttribute("categoryInUse","Products are available in this category,try to delete product first");
            return "redirect:/admin/getCategories";
        }else {
            categoryService.deleteCategoryById(id);
            return "redirect:/admin/getCategories";
        }


    }

    @GetMapping("/admin/updateCategories/{id}")
    public String updateCategory(@PathVariable UUID id ,Model model){
        Optional<Category> category=categoryService.getCategoryById(id);
        if (category.isPresent()){
            model.addAttribute("category",category.get());
            return "/category/addCategories";
        }else {
            return "404";
        }
    }


}
