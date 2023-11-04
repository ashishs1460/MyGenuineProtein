package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.model.*;
import com.ashish.MyGenuineProtein.repository.WishListItemRepository;
import com.ashish.MyGenuineProtein.repository.WishListRepository;
import com.ashish.MyGenuineProtein.service.UserService;
import com.ashish.MyGenuineProtein.service.VariantService;
import com.ashish.MyGenuineProtein.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Controller
public class WishListController {
    @Autowired
    UserService userService;
    @Autowired
    VariantService variantService;
    @Autowired
    WishListService wishListService;
    @Autowired
    WishListRepository wishListRepository;
    @Autowired
    WishListItemRepository wishListItemRepository;

    @GetMapping("/user/wishList/{id}")
    public ResponseEntity<Map<String, String>> getWishListPage(
            @PathVariable("id") Long variantId,
            Principal principal
    ) {
        Map<String, String> response = new HashMap<>();
        Optional<User> optionalUser = userService.findUserByEmail(principal.getName());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            int flag = wishListService.addToWishList(variantId, user);

            if (flag == 1) {
                response.put("status", "error");
                response.put("message", "Product already exists in the wishlist");
            } else {
                response.put("status", "success");
                response.put("message", "Product added to wishlist");
            }
        } else {
            response.put("status", "error");
            response.put("message", "User not found");
        }

        return ResponseEntity.ok(response);
    }


    @GetMapping("/user/myWishList")
    public String getWishListPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }


        User user = userService.findUserByEmail(principal.getName()).orElseThrow();

        WishList wishList =wishListRepository.findByUser(user).get();

        System.out.println(wishList);

        if (wishList != null) {
            List<WishListItem> wishListItems = wishList.getWishListItems();
            model.addAttribute("wishListItems", wishListItems);
            System.out.println(wishListItems);
        } else {
            // Handle the case when the wish list is empty or not initialized
            model.addAttribute("wishListItems", new ArrayList<>()); // Empty wish list
        }

        return "wishListPage";
    }


    @GetMapping("/user/removeItem/{id}")
    public String removeFromWishList(@PathVariable("id") Long wishListItemId,
                                     Principal principal,
                                     RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findUserByEmail(principal.getName()).orElseThrow();
        WishList wishList = user.getWishList();

        if (wishList != null) {
            // Use the WishListItemRepository to delete the item by ID
            wishListItemRepository.deleteById(wishListItemId);
            redirectAttributes.addFlashAttribute("successMsg", "Item removed from wishlist");
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "Wishlist not found");
        }

        return "redirect:/user/myWishList";
    }



}
