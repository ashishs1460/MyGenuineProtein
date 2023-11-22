package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.model.Order;
import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.model.Review;
import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.service.ProductService;
import com.ashish.MyGenuineProtein.service.ReviewService;
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
public class ReviewController {
    @Autowired
    ReviewService reviewService;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;


     @GetMapping("/review/{orderId}/{productId}")
    public String getReviewPage(@PathVariable long orderId,
                                @PathVariable(name = "productId") UUID id,
                                Model model,
                                Principal principal){
         Optional<User> optionalUser = userService.findUserByEmail(principal.getName());
         if (optionalUser.isPresent()){
             User user = optionalUser.get();
             Product product = productService.findProductById(id).get();
             Optional<Review> existingReview = reviewService.findReviewByUserAndProduct(user, product);
             Review review = existingReview.orElseGet(Review::new);
             model.addAttribute("review",review);
             model.addAttribute("productId",id.toString());
             model.addAttribute("orderId",orderId);
             return "productReview";
         }else {
             return "redirect:/login";
         }


     }

     @PostMapping("/review/submit")
    public String postReviewPage(@ModelAttribute Review userReview,
                                 @RequestParam(name = "productId") String id,
                                 @RequestParam(name = "orderId") long orderId,
                                 RedirectAttributes redirectAttributes,
                                 Principal principal){
        UUID productId = UUID.fromString(id);

        Optional<User> optionalUser = userService.findUserByEmail(principal.getName());

        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            Product product = productService.findProductById(productId).get();

            Review review = new Review();
            review.setId(userReview.getId());
            review.setHasReview(true);
            review.setUser(user);
            review.setComment(userReview.getComment());
            review.setRating(userReview.getRating());
            review.setProduct(product);
            reviewService.save(review);
            redirectAttributes.addFlashAttribute("successMsg","Review added Successfully");
            return "redirect:/order/viewOrder/" + orderId;

        }else {
            return "redirect:/login";
        }


     }
}
