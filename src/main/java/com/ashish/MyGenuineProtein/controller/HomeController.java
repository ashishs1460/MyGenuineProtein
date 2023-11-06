package com.ashish.MyGenuineProtein.controller;


import com.ashish.MyGenuineProtein.model.Order;
import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    public String shopPage(Model model,@PageableDefault(size = 7) Pageable pageable) {

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
    public String viewProduct(@PathVariable UUID id ,Model model){
       Product product = productService.findProductById(id).get();


        model.addAttribute("product",productService.findProductById(id).get());
        model.addAttribute("variants",variantService.getVariantForProduct(product));
        return "viewProduct";

    }

    @PostMapping("/shop")
    public String searchProducts(@ModelAttribute("keyword") String keyword,
                                 Model model) {


        List<Product> list = productService.getAllProducts();
      List<Product> searchresult =  list.stream()
                .filter(product -> product.getName().toLowerCase().contains(keyword.toLowerCase()))
                .toList();

        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", searchresult);
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
            List<Order> userOrders = orders.stream()
                    .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                    .collect(Collectors.toList());
            model.addAttribute("userOrders",userOrders);
        }
        return "order-list";
    }
}
