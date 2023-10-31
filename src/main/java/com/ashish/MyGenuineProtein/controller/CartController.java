package com.ashish.MyGenuineProtein.controller;


import com.ashish.MyGenuineProtein.model.*;
import com.ashish.MyGenuineProtein.repository.CartItemRepository;
import com.ashish.MyGenuineProtein.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Controller
public class CartController {

    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;

    @Autowired
    VariantService variantService;
    @Autowired
    CartService cartService;

    @Autowired
    AddressService addressService;

    @GetMapping("/add/{id}")
    @ResponseBody
    public ResponseEntity<String> addCart(@PathVariable("id") Long id, Principal principal) {
        if (principal == null){

        }

        System.out.println(principal.getName());
        try {
            Optional<User> optionalUser = userService.findUserByEmail(principal.getName());
            if (optionalUser.isPresent()) {
                User user =optionalUser.get();
                Variant variant = variantService.getVariantById(id).orElseThrow();
                System.out.println(variant.getVariantName());
                System.out.println(user.getFirstName());

                if (variant.getStock() <= 0) {
                    return ResponseEntity.ok("Out of Stock");
                }

                cartService.addToCart(id, user.getEmail());
                return ResponseEntity.ok("Added to cart");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("cart/goToCart")
    public  String  getCartPage(Model model , Authentication authentication,Principal principal){


        if (principal==null){
            return "login";
        }
        else {
            User user=userService.findUserByEmail(principal.getName()).orElseThrow();
            Cart cart = user.getCart();
            List<CartItem > cartItems = cart.getCartItems();
            int Quantity =   cartItems
                    .stream()
                    .map(item -> item.getQuantity())
                    .reduce(0,(a,b)->a+b);
            long totalAmount = cartItems.stream()
                    .mapToLong(item -> (long) (item.getQuantity() * item.getVariant().getPrice()))
                    .sum();
            model.addAttribute("cartItems" ,cartItems);
            model.addAttribute("quantity",Quantity);
            model.addAttribute("total", totalAmount);


        }
        return "cart";

        }


    @GetMapping ("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id, RedirectAttributes redirectAttributes) {

            // Remove the cart item from the list

                cartService.removeFromCart(id);

                redirectAttributes.addFlashAttribute("successMsg", "Item removed from cart");

                return "redirect:/cart/goToCart";

    }

    @PostMapping("/cart/update-cart")
    public String updateCart(@RequestParam Long id, @RequestParam int newQuantity, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String user = principal.getName();
        System.out.println(user);
       if(newQuantity>=1){
           cartService.updateCartItem(user, id, newQuantity);
       }


        return "redirect:/cart/goToCart";
    }

    @GetMapping("/cart/checkout")
    public String showCheckOut(Model model,Principal principal){
        Optional<User> optionalUser = userService.findUserByEmail(principal.getName());
        if(optionalUser.isPresent()){
            User user =optionalUser.get();
            List<Address> addressList = addressService.findAllUserAddresses(user)
                    .stream()
                    .filter(address -> !address.isDelete())
                    .sorted(Comparator.comparing(Address::getCreatedAt).reversed()).toList();
            model.addAttribute("Addresses",addressList);
            model.addAttribute("user",user);

            Cart cart = user.getCart();

            List<CartItem> cartItems = cart.getCartItems();
            model.addAttribute("cartItems",cartItems);

            double totalPrice = cartItems.stream()
                    .mapToDouble(cartItem -> cartItem.getVariant().getPrice() * cartItem.getQuantity())
                    .sum();
            model.addAttribute("totalPrice",totalPrice);

        }
        return "checkout";


    }




}




