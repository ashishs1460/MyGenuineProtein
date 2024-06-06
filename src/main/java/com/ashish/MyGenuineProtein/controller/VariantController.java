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

    @Autowired
    VariantRepository variantRepository;

    @GetMapping("/admin/getVariant")
    public String getVariantPage(Model model, RedirectAttributes redirectAttributes) {
        List<Variant> variants = variantService.getAllVariants();
        if (variants.isEmpty()) {
            model.addAttribute("error", "No variants found."); // Add error message
        } else {
            model.addAttribute("variants", variants);
        }
        return "product/getVariant";
    }


    @GetMapping("/admin/addVariant")
    public String getAddVariant(Model model){
        List<Product> productList = productService.findAllProducts();
        model.addAttribute("products",productList);
        model.addAttribute("variant" ,new Variant());
        return "product/addVariant";

    }

    @PostMapping("/admin/postVariant")
    public String postAddVariant(@ModelAttribute("variants") Variant variant,RedirectAttributes redirectAttributes){

        if (variant.getId()==null) {
            Product product = productService.findProductById(variant.getProduct().getId()).get();
//            List<Variant> variantList = product.getVariants();
//            for (Variant variant1 : variantList) {
//                if (variant1.getVariantName().equals(variant.getVariantName())) {
//                    redirectAttributes.addFlashAttribute("msg","Variant already exists");
//                    return "redirect:/admin/getVariant";
//                }
//            }
            if (variantRepository.existsByProductAndVariantName(product, variant.getVariantName())) {
                redirectAttributes.addFlashAttribute("msg","Variant already exists");
                    return "redirect:/admin/getVariant";
            }
        }

        variantService.addVariantToProduct(variant.getProduct().getId(), variant);
        redirectAttributes.addFlashAttribute("msg"," successful!");

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



    @GetMapping("/admin/deleteVariant/{id}")
    public String deleteVariant(@PathVariable("id") long id,
                                RedirectAttributes redirectAttributes){
        try {
            variantService.deleteById(id);
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("msg","Variant is associated with an order/wishlist and cannot be deleted.");
            return "redirect:/admin/getVariant";
        }
        redirectAttributes.addFlashAttribute("msg","Variant deleted successfully");

        return "redirect:/admin/getVariant";
    }

    @GetMapping("/admin/updateVariant/{id}")
    public String updateVariant(@PathVariable("id") long id,
                                Model model
                                ){
        List<Product> productList = productService.findAllProducts();
        model.addAttribute("products",productList);
        Variant variant = variantService.getVariantById(id).get();
        model.addAttribute("variant", variant);
        return "product/addVariant";

    }


}
