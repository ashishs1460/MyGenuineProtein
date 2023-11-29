package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.model.Wallet;
import com.ashish.MyGenuineProtein.repository.WalletRepository;
import com.ashish.MyGenuineProtein.service.UserService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.persistence.Column;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
public class WalletController {
    @Autowired
    UserService userService;

    @Autowired
    WalletRepository walletRepository;


    @GetMapping("/user/myWallet")
    public String  getWalletPage(Model model,
                                 Principal principal){
        Optional<User> optionalUser = userService.findUserByEmail(principal.getName());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            Wallet wallet = user.getWallet();
            String userReferralCode = user.getUserReferralCode();
            if (wallet == null){
                Wallet wallet1 = new Wallet();
                wallet1.setUser(user);
                wallet1.setAmount(1);
                wallet1.setReferralAmount(0d);
                walletRepository.save(wallet1);
                wallet = wallet1;
            }
            model.addAttribute("userReferralCode",userReferralCode);
            model.addAttribute("wallet",wallet);
            model.addAttribute("user",user);
            return "myWallet";
        }else {
            return "redirect:/login";
        }

    }

    @PostMapping("/user/addToWallet")
    public String addToWallet(@RequestParam("amount") double totalAmount,
                              Principal principal,
                              Model model,
                              RedirectAttributes redirectAttributes) throws RazorpayException {

        RazorpayClient razorpay = new RazorpayClient("rzp_test_wSP8EmmFjEbRbD", "wDhpCDheW2ZzApA89aD6hhAu");

        // Check if the total amount exceeds the maximum allowed amount
        if (totalAmount > 100000) {
            // Handle the case where the amount exceeds the limit (e.g., display an error message)
            redirectAttributes.addFlashAttribute("errorMessage", "Amount exceeds maximum allowed amount.");
            return "redirect:/user/myWallet"; // You can create a custom error page
        }

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", totalAmount * 100);
        orderRequest.put("currency", "INR");

        Order order = razorpay.orders.create(orderRequest);

        model.addAttribute("amount", totalAmount * 100);
        model.addAttribute("orderId", order.get("id"));
        model.addAttribute("totalAmount", totalAmount);

        return "razorPayWallet";
    }


    @GetMapping("/user/razorPayWalletSuccess")
    public  String  depositToWallet(Principal principal,
                                    @RequestParam("totalAmount") Double totalPrice,
                                    RedirectAttributes redirectAttributes
                                    ){
        Optional<User> optionalUser = userService.findUserByEmail(principal.getName());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            Wallet wallet = user.getWallet();
            if (wallet == null){
                Wallet newWallet = new Wallet();
                newWallet.setUser(user);
                newWallet.setAmount(totalPrice);
                walletRepository.save(newWallet);

            }else{
                double currentAmount = wallet.getAmount();
                wallet.setAmount(currentAmount + totalPrice);
                walletRepository.save(wallet);
            }
            redirectAttributes.addFlashAttribute("successMsg", "Amount credited to wallet" );
            return "redirect:/user/myWallet";
        }else {
            return "redirect:/login";

        }

    }

    @GetMapping("/referWithMail")
    public String sendReferWithMail (@RequestParam("email") String email,
                                    Principal principal,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {


        User user = userService.findUserByEmail(principal.getName()).get();
        Wallet wallet=user.getWallet();
        String userReferralCode =user.getUserReferralCode();



        boolean validateEmail  =userService.validateEmail(email);
        if (!validateEmail) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please enter a valid email address.");
            model.addAttribute("successMessage", null);
        } else {
            Optional<User> users =userService.findUserByEmail(principal.getName());
            User userEntity=users.get();
            String referralCode  =  userEntity.getUserReferralCode();

            String subject="If your register using this referral code you will get 50 rupees! ";

            userService.sendMail(email, subject, referralCode);
            redirectAttributes.addFlashAttribute("errorMessage", null);
            redirectAttributes.addFlashAttribute("successMessage", "Referral code sent successfully.");
        }
//        model.addAttribute("userWallet",wallet);
//        model.addAttribute("user",user);
//        model.addAttribute("userReferralCode",userReferralCode);
        return "redirect:/user/myWallet";
//        return "redirect:/shop/referral-code";
    }


}
