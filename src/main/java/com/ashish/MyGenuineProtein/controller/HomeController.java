package com.ashish.MyGenuineProtein.controller;


import com.ashish.MyGenuineProtein.model.*;
import com.ashish.MyGenuineProtein.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;


    @Autowired
    VariantService variantService;
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;
    @Autowired
    OfferService offerService;

    @GetMapping({"/","/home"})
    public String homePage(Model model ,Pageable pageable){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String str = authentication.getAuthorities().toString();
        if (str.equals("[ROLE_ANONYMOUS]") || str.equals("[ROLE_USER]")) {
            List<Product> products =productService.getAllProducts().stream().
                    filter(product -> !product.isDelete()).toList();
            model.addAttribute("products",products);
            model.addAttribute("categories",categoryService.getAllCategory());
            return "index";
        } else {
            return "redirect:/admin";
        }
    }

    @GetMapping("/shop")
    public String shopPage(Model model,@PageableDefault(size = 8) Pageable pageable) {

        model.addAttribute("categories", categoryService.getAllCategory());
        Page<Product> productsPage = productService.getProductsPage(pageable);

        List<Product> filteredProducts = productsPage.getContent().stream()
                .filter(product -> !product.isDelete())
                .collect(Collectors.toList());



        model.addAttribute("productsPage", productsPage);
        model.addAttribute("products", filteredProducts);



        return "shop";
    }


    @GetMapping("/shop/category/{id}")
    public String shopByCategory(@PathVariable UUID id, Model model, @PageableDefault(size = 7) Pageable pageable) {
        Page<Product> productsPage = productService.findProductById(id, pageable);
        List<Product> filteredProducts = productsPage.getContent().stream()
                .filter(product -> !product.isDelete())
                .collect(Collectors.toList());
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("productsPage", productsPage);
        model.addAttribute("products", filteredProducts);

        return "shop";
    }




    @GetMapping("/shop/viewProduct/{id}")
    public String viewProduct(@PathVariable UUID id, Model model) {
        Product product = productService.findProductById(id).orElseThrow(); // Assuming you want to throw an exception if the product is not found
        List<Variant> variants = variantService.getVariantForProduct(product);

         Offer productOffers = product.getOffer();
         Offer categoryOffers = offerService.getCategoryOffers(product.getCategory());


        if (productOffers == null && categoryOffers == null) {
            applyDefaultDiscountToVariants(variants);
        } else {
            applyMaxOfferToVariants(variants, productOffers, categoryOffers);
        }

        model.addAttribute("product", product);
        model.addAttribute("variants", variants);
        return "viewProduct";
    }



    private void applyDefaultDiscountToVariants(List<Variant> variants) {
        variants.forEach(variant -> {
            variant.setDiscountedPrice(0);
            variantService.save(variant);
        });
    }

    private void applyMaxOfferToVariants(List<Variant> variants, Offer productOffers, Offer categoryOffers) {
        for (Variant variant : variants) {
            float discountPrice;

            if (productOffers == null && categoryOffers !=null) {
                discountPrice = variant.getPrice() - (variant.getPrice() * categoryOffers.getCategoryOffPercentage() / 100);
            } else if (categoryOffers == null && productOffers !=null) {
                discountPrice = variant.getPrice() - (variant.getPrice() * productOffers.getCategoryOffPercentage() / 100);
            } else if (productOffers.getCategoryOffPercentage() > categoryOffers.getCategoryOffPercentage()) {
                System.out.println("Inside the product-wise offer");
                discountPrice = variant.getPrice() - (variant.getPrice() * productOffers.getCategoryOffPercentage() / 100);
            } else {
                System.out.println("Inside the category-wise offer");
                discountPrice = variant.getPrice() - (variant.getPrice() * categoryOffers.getCategoryOffPercentage() / 100);
            }

            variant.setDiscountedPrice(discountPrice);
            variantService.save(variant);
        }
    }


    private float getMaxOfferPercentage(List<Offer> offers) {
        return offers.stream()
                .map(Offer::getCategoryOffPercentage)
                .max(Float::compare)
                .orElse(0);
    }

    private float calculateDiscountedPrice(Variant variant, float percentage) {
        return variant.getPrice() - (variant.getPrice() * percentage / 100);
    }


    @PostMapping("/shop/search")
    public String searchProducts(@ModelAttribute("keyword") String keyword,
                                 @PageableDefault(size = 8) Pageable pageable,
                                 Model model) {

        List<Product> list = productService.getAllProducts();
        List<Product> searchResult = list.stream()
                .filter(product -> product.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        Page<Product> productsPage = new PageImpl<>(searchResult, pageable, searchResult.size());

        List<Product> filteredProducts = productsPage.getContent();

        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("productsPage", productsPage);
        model.addAttribute("products", filteredProducts);

        return "shop";
    }



    @GetMapping("/user/userDashboard")
    public String userDashboard (Model model, Principal principal){
        User user = userService.findUserByEmail(principal.getName()).get();
        model.addAttribute("userProfile",user);
        return "userDashboard";
    }

    @GetMapping("/user/myOrders")
    public String showAllOrders(Model model, Principal principal){

        Optional<User> optionalUser = userService.findUserByEmail(principal.getName());
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            List<Order> orders = orderService.findByUser(user);
//            List<Order> userOrders = orders.stream()
//                    .sorted(Comparator.comparing(Order::getOrderDate).reversed())
//                    .collect(Collectors.toList());
             Collections.reverse(orders);

            model.addAttribute("userOrders",orders);
        }
        return "order-list";
    }
}
