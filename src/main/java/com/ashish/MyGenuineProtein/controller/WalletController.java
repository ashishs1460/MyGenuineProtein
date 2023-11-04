package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.model.Wallet;
import com.ashish.MyGenuineProtein.repository.WalletRepository;
import com.ashish.MyGenuineProtein.service.UserService;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

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
    public String addToWallet(@RequestParam("amount") double amount, Principal principal) {
        User user = userService.findUserByEmail(principal.getName()).get();
        Wallet wallet = user.getWallet();

        if (wallet == null) {
            Wallet newWallet = new Wallet();
            newWallet.setUser(user);
            newWallet.setAmount(amount);
            walletRepository.save(newWallet);
        } else {
            double currentAmount = wallet.getAmount();
            wallet.setAmount(currentAmount + amount);
            walletRepository.save(wallet);
        }

        return "redirect:/user/myWallet";
    }

}
