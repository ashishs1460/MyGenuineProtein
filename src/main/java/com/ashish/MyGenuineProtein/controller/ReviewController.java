package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.model.Review;
import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.service.ProductService;
import com.ashish.MyGenuineProtein.service.ReviewService;
import com.ashish.MyGenuineProtein.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.UUID;

@Controller
public class ReviewController {
    @Autowired
    ReviewService reviewService;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;




    @PostMapping("/review/{id}")
    public String addReview(@PathVariable(name = "id")UUID id,
                            @RequestParam(name = "comment")String comment,
                            @RequestParam(name = "rating")int rating,
                            @RequestParam(name = "orderId") long orderId,
                            RedirectAttributes redirectAttributes,
                            Principal principal
                            ){
        User user = userService.findUserByEmail(principal.getName()).get();
        Product product = productService.findProductById(id).get();
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setComment(comment);
        review.setRating(rating);
        reviewService.save(review);


        System.out.println("productId"+id);
        System.out.println("comment"+comment);
        System.out.println("rating"+rating);
        System.out.println("orderId"+orderId);


        redirectAttributes.addFlashAttribute("successMsg","Review added successfully!");
        return "redirect:/order/viewOrder/"+orderId;
    }
}
