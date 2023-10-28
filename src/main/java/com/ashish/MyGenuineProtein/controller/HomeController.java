package com.ashish.MyGenuineProtein.controller;


import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.service.CategoryService;
import com.ashish.MyGenuineProtein.service.VariantService;
import com.ashish.MyGenuineProtein.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class HomeController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;


    @Autowired
    VariantService variantService;

    @GetMapping({"/","/home"})
    public String homePage(Model model ,Pageable pageable){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String str = authentication.getAuthorities().toString();
        if (str.equals("[ROLE_ANONYMOUS]") || str.equals("[ROLE_USER]")) {
            model.addAttribute("products",productService.getAllProducts(pageable));
            model.addAttribute("categories",categoryService.getAllCategory());
            return "index";
        } else {
            return "redirect:/admin";
        }
    }

    @GetMapping("/shop")
    public String shopPage(Model model) {
        // Create a Pageable object
        Pageable pageable = PageRequest.of(0, 5);

        model.addAttribute("categories", categoryService.getAllCategory());
        Page<Product> products = productService.getAllProducts(pageable);
        model.addAttribute("products", products);
        model.addAttribute("flavours", variantService.getAllVariants());

        return "shop";
    }


    @GetMapping("/shop/category/{id}")
    public String shopByCategory(@PathVariable UUID id, Model model){
        Pageable pageable = PageRequest.of(0, 2);
        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("products",productService.getAllProductsByCategoryId(id,pageable));
        return "shop";
    }
//    @GetMapping("/shop/flavour/{id}")
//    public String shopByFlavour(@PathVariable UUID id,Model model){
//        Pageable pageable = PageRequest.of(0, 2);
//        model.addAttribute("flavours", variantService.getAllVariants());
//        model.addAttribute("products",productService.getAllProductsByVariantsById(id,pageable));
//        return "shop";
//    }

    @GetMapping("/shop/viewProduct/{id}")
    public String viewProduct(@PathVariable UUID id ,Model model){

        model.addAttribute("product",productService.findProductById(id).get());
        model.addAttribute("variants",variantService.getAllVariants());
        return "viewProduct";

    }
    @GetMapping("/pagination/{offset}/{pageSize}")
    public Page<Product> getProductsWithPagination(@PathVariable int offset, @PathVariable int pageSize) {
        return productService.findProductsWithPagination(offset,pageSize);
    }


}
