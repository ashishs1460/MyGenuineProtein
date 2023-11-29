package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.model.Category;
import com.ashish.MyGenuineProtein.model.Order;
import com.ashish.MyGenuineProtein.model.Review;
import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Controller
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;
    @Autowired
    ReviewService reviewService;

    @Autowired
    OrderService orderService;

    @Autowired
    ChartService chartService;

    @GetMapping("/admin")
    public String getAdminHome(Model model, Principal principal){
        Optional<User> optionalUser = userService.findUserByEmail(principal.getName());
        if(optionalUser.isPresent()){

            User user = optionalUser.get();
            String username = user.getFirstName();
            List<Order> orders = orderService.findAll();



            // Call the service methods to generate revenue data
            List<Double> dailyRevenue = chartService.generateDailyRevenue(orders);
            List<Double> monthlyRevenue = chartService.generateMonthlyRevenue(orders);
            List<Double> yearlyRevenue = chartService.generateYearlyRevenue(orders);

            model.addAttribute("dailyRevenue", dailyRevenue);
            model.addAttribute("monthlyRevenue", monthlyRevenue);
            model.addAttribute("yearlyRevenue", yearlyRevenue);
            model.addAttribute("username", username);
            System.out.println(dailyRevenue);
            System.out.println("month"+monthlyRevenue);
            System.out.println("year"+yearlyRevenue);

            return "getAdminHome";
        }else {
            return "redirect:/login";
        }
    }

    @GetMapping("/admin/getCategories")
    public String getCategory(Model model){
        String successMessage = (String) model.getAttribute("successMessage");
        model.addAttribute("successMessage", successMessage);
        model.addAttribute("categories",categoryService.getAllCategory());
        return "category/getCategories";
    }

    @GetMapping("/admin/addCategories")
    public String addCategory(Model model){
        model.addAttribute("category",new Category());
        return "category/addCategories";
    }

    @PostMapping("/admin/addCategories")
    public String postCategory(@ModelAttribute("category") Category category, RedirectAttributes redirectAttributes){
       boolean isPresent=categoryService.getCategoryByName(category.getName());
        if(isPresent){
            redirectAttributes.addFlashAttribute("CategoryPresent","Category is already available , try to add another category");
            return "redirect:/admin/getCategories";
        }
        categoryService.addCategory(category);
        redirectAttributes.addFlashAttribute("successMessage", " successfully!");
        return "redirect:/admin/getCategories";
    }

    @GetMapping("/admin/deleteCategories/{id}")
    public String deleteCategory(@PathVariable UUID id ,Model model ,RedirectAttributes redirectAttributes){
        boolean isPresent = productService.existsByCategoryId(id);
        if(isPresent){
            redirectAttributes.addFlashAttribute("categoryInUse","Products are available in this category,try to delete product first");
            return "redirect:/admin/getCategories";
        }else {
            categoryService.deleteCategoryById(id);
            return "redirect:/admin/getCategories";
        }


    }


    @GetMapping("/admin/updateCategories/{id}")
    public String updateCategory(@PathVariable UUID id ,Model model){
        Optional<Category> category=categoryService.getCategoryById(id);
        if (category.isPresent()){
            model.addAttribute("category",category.get());
            return "category/addCategories";
        }else {
            return "404";
        }
    }

    @GetMapping("/admin/getReferralCode")
    public String referralCodePage(Model model){
        List<User> userList = userService.findAllUsers();
        model.addAttribute("users",userList);
        return "referralPage";
    }

    @GetMapping("/admin/getUserReviews")
    public String getUserReviewPage(Model model){
        List<Review> reviews = reviewService.findAll();
        model.addAttribute("reviews",reviews);
        return "userReviewPage";
    }

   @GetMapping("/admin/deleteReview/{id}")
    public String deleteUserReview(@PathVariable(name = "id") int id,
                                   RedirectAttributes redirectAttributes){
        reviewService.deleteById(id);
        redirectAttributes.addFlashAttribute("msg","Review deleted successfully!");
        return "redirect:/admin/getUserReviews";
   }

   @GetMapping("/admin/getSalesReport")
    public String getSalesReport(Model model){

        List<Order> orders = orderService.findAll();
        List<Order> filteredUserOrders = new ArrayList<>(orders
                .stream()
                .filter(order -> !order.getStatus().name().equals("CANCELLED")).toList());

       Collections.reverse(filteredUserOrders);
       model.addAttribute("orderFilter", "All orders");
       model.addAttribute("ALL", "ALL");
       model.addAttribute("userOrders",filteredUserOrders);
       model.addAttribute("totalOrders",filteredUserOrders.size());
       model.addAttribute("totalSales",filteredUserOrders.stream().map(Order::getTotalPrice).reduce(0.0, Double::sum));
        return "salesReport";
   }
}
