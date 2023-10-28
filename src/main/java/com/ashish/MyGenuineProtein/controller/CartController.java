package com.ashish.MyGenuineProtein.controller;


import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.service.ProductService;
import com.ashish.MyGenuineProtein.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Controller
public class CartController {

    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable UUID id, Principal principal){
        Optional<User> optionalUser = userService.findUserByEmail(principal.getName());
        System.out.println(optionalUser);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            System.out.println(user);

            Optional<Product> optionalProduct = productService.findProductById(id);


            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();

                System.out.println(product);
            }

        }else {
            return "redirect:/login";
        }

        return "test";



    }
}
