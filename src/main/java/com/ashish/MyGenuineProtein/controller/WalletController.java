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
        User user = userService.findUserByEmail(principal.getName()).get();
        Wallet wallet = user.getWallet();
        if (wallet == null){
            Wallet wallet1 = new Wallet();
            wallet1.setUser(user);
            wallet1.setAmount(0);
            walletRepository.save(wallet1);
            wallet = wallet1;
        }

        model.addAttribute("wallet",wallet);
        return "myWallet";
    }

    @PostMapping ("/user/addToWallet")
    public String addToWallet(@RequestParam("amount") double totalAmount,
                              Principal principal,
                              Model model) throws RazorpayException {

        RazorpayClient razorpay = new RazorpayClient("rzp_test_wSP8EmmFjEbRbD", "wDhpCDheW2ZzApA89aD6hhAu");

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", totalAmount * 100);
        orderRequest.put("currency", "INR");
        Order order = razorpay.orders.create(orderRequest);
        model.addAttribute("amount", totalAmount * 100);
        model.addAttribute("orderId", order.get("id"));
        model.addAttribute("totalAmount",totalAmount);

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



}
