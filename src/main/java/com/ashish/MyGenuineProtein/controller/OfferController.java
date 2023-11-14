package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.model.Category;
import com.ashish.MyGenuineProtein.model.Offer;
import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.service.CategoryService;
import com.ashish.MyGenuineProtein.service.OfferService;
import com.ashish.MyGenuineProtein.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class OfferController {
    @Autowired
    OfferService offerService;
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;

    @GetMapping("/admin/getOffers")
    public String getOffers(Model model){

        List<Offer> offers = offerService.getAllOffers();
        model.addAttribute("offers",offers);
        return "offers";
    }

    @GetMapping("/admin/createOffer")
    public  String createOffer(Model model){

        List<Product> products = productService.findAllProducts();
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("products",products);
        model.addAttribute("categories",categories);
        model.addAttribute("offer",new Offer());
        return "createOffer";

    }

    @PostMapping("/admin/createOffer")
    public  String createOffer(    @RequestParam("CategoryOffPercentage") Integer categoryOffPercentage,
                                   @RequestParam(value = "categoryUuid" ,required = false) UUID categoryUuid,
                                   @RequestParam(value = "productUuid",required = false) UUID productUuid,
                                   @RequestParam("count") Integer count,
                                   @RequestParam("expiryDate") LocalDate expiryDate,
                                   RedirectAttributes redirectAttributes
                               ){


        if(productUuid != null){

            Optional <Product> product = productService.findProductById(productUuid);
            String productName = product.get().getName();
            if(offerService.offerExistsForProductName(productName)){
                redirectAttributes.addFlashAttribute("errorMsg","Offer Already exist for this product!");
                return "redirect:/admin/createOffer";
            }
        }else if(categoryUuid != null){
            Optional<Category> category = categoryService.findCategoryById(categoryUuid);
            String categoryName = category.get().getName();
            if (offerService.offerExistsForCategoryName(categoryName)){
                redirectAttributes.addFlashAttribute("errorMsg","Offer Already exist for this category!");
                return "redirect:/admin/createOffer";
            }
        }

        try {
            Offer offer = new Offer();
            offer.setCategoryOffPercentage(categoryOffPercentage);
            offer.setEnabled(true);
            offer.setExpiryDate(expiryDate);
            offer.setCount(count);

            if(categoryUuid != null){
                offer = offerService.addOfferCategory(offer,categoryUuid);
            }else {
               offer = offerService.addOfferProduct(offer,productUuid);
            }
            redirectAttributes.addFlashAttribute("successMsg","Offer created successfully");
            return "redirect:/admin/getOffers";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMsg","Offer Already exist!");
            return "redirect:/admin/createOffer";
        }

    }

    @GetMapping("/admin/offer/delete/{id}")
    public  String deleteOffer(@PathVariable long id,
                               RedirectAttributes redirectAttributes){
        offerService.deleteOfferById(id);
        redirectAttributes.addFlashAttribute("successMsg","Offer deleted successfully!");
        return "redirect:/admin/getOffers";
    }

    @GetMapping("/admin/offer/edit/{id}")
    public String editOfferPage(@PathVariable Long id,
                                Model model){
        Optional<Offer> optionalOffer = offerService.findOfferById(id);
        if (optionalOffer.isPresent()){
            Offer offer = optionalOffer.get();
            model.addAttribute("offer",offer);
            return "editOffer";
        }else {
            return "404";
        }
    }

    @PostMapping("/offer/updateOffer")
    public String updateOffer(@ModelAttribute Offer updatedOffer,
                              RedirectAttributes redirectAttributes){
        Optional<Offer> optionalOffer = offerService.findOfferById(updatedOffer.getId());
        if (optionalOffer.isPresent()){
            Offer offer = optionalOffer.get();
            offer.setCategoryOffPercentage(updatedOffer.getCategoryOffPercentage());
            offer.setExpiryDate(updatedOffer.getExpiryDate());
            offer.setCount(updatedOffer.getCount());
            offer.setEnabled(true);
            offerService.saveOffer(offer);
            redirectAttributes.addFlashAttribute("successMsg","Offer updated successfully!");
            return "redirect:/admin/getOffers";
        }else {
            redirectAttributes.addFlashAttribute("errorMsg","Failed to update offer");
            return "redirect:/admin/getOffers";
        }

    }




}
