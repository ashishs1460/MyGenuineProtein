package com.ashish.MyGenuineProtein.controller;


import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.model.Variant;
import com.ashish.MyGenuineProtein.repository.VariantRepository;
import com.ashish.MyGenuineProtein.service.ProductService;
import com.ashish.MyGenuineProtein.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class VariantController {
    @Autowired
    VariantService variantService;
    @Autowired
    ProductService productService;

    @GetMapping("/admin/getVariant")
    public String getVariantPage(Model model){

        model.addAttribute("variants",variantService.getAllVariants());
        return "/product/getVariant";
    }

    @GetMapping("/admin/addVariant")
    public String getAddVariant(Model model){
        List<Product> productList = productService.findAllProducts();
        model.addAttribute("products",productList);
        model.addAttribute("variant" ,new Variant());
        return "/product/addVariant";

    }

    @PostMapping("/admin/postVariant")
    public String postAddVariant(@ModelAttribute("variants") Variant variant, RedirectAttributes redirectAttributes){

        Product product =variant.getProduct();
        UUID id=product.getId();
        variantService.addVariantToProduct(id,variant);
        redirectAttributes.addFlashAttribute("message","Variant added successfully!");


        return "redirect:/admin/getVariant";



    }



    @GetMapping("/variant/get-variant/{id}")
    @ResponseBody
    public ResponseEntity<Variant> getVariantDetails(@PathVariable("id") String id) {
        try {
            Long variantId = Long.parseLong(id);
            System.out.println(id);
            Optional<Variant> optionalVariant = variantService.getVariantById(variantId);
            if (optionalVariant.isPresent()) {
                Variant variant = optionalVariant.get();
                return ResponseEntity.ok(variant);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
