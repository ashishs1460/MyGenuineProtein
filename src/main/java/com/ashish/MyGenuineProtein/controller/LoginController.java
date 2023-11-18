package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.model.Role;
import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.model.Wallet;
import com.ashish.MyGenuineProtein.otp.repository.OtpRepository;
import com.ashish.MyGenuineProtein.repository.RoleRepository;
import com.ashish.MyGenuineProtein.repository.UserRepository;
import com.ashish.MyGenuineProtein.service.UserService;
import com.ashish.MyGenuineProtein.service.UserServiceImp;
import com.ashish.MyGenuineProtein.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class    LoginController {


    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;


    @Autowired
    UserService userService;


    @Autowired
    OtpRepository otpRepository;
    @Autowired
    WalletService walletService;

    @GetMapping("/login")
    public String getLoginPage(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication==null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }

        return "redirect:/";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication==null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("user", new User());
            return "register";
        }

        return "redirect:/";
    }

    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        System.out.println("register .................>>>>>");
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        System.out.println("r-code   ......"+user.getUserReferralCode());

        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            if (userRepository.findUserByEmail(user.getEmail()).get().isVerified()) {
                redirectAttributes.addAttribute("userExist", "true");
                return "redirect:/register";
            } else {
                otpRepository.delete(otpRepository.findByUser(userRepository.findUserByEmail(user.getEmail()).get()).get());
                userRepository.delete(userRepository.findUserByEmail(user.getEmail()).get());
            }
        }

        String password = user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findById(UUID.fromString("56d445f0-6f53-11ee-b70a-6f05ccf153cb")).get());
        user.setRoles(roles);
        user.setActive(true);

        // Referral code
        String referralCode = user.getUserReferralCode();
        user.setUserReferralCode("e");
        userRepository.save(user);

        Wallet newUserWallet = new Wallet();
        if (referralCode != null) {
            User referredUser = userService.findByReferralCode(referralCode);

            if (referredUser != null) {
                Optional<Wallet> optionalReferredUserWallet = walletService.findByUser(referredUser);
                double referredMoney = 100;
                if (optionalReferredUserWallet.isPresent()) {
                    Wallet referredUserWallet = optionalReferredUserWallet.get();
                    if (referredUserWallet.getReferralAmount() > 0) {
                        referredUserWallet.setReferralAmount(referredUserWallet.getReferralAmount() + referredMoney);
                    } else {
                        referredUserWallet.setReferralAmount(referredMoney);
                    }
                    referredUserWallet.setAmount(referredUserWallet.getAmount() + referredMoney);
                    walletService.save(referredUserWallet);

                    double referralCash = 50;
                    newUserWallet.setUser(user);
                    newUserWallet.setReferralAmount(referralCash);
                    newUserWallet.setAmount(referralCash);
                    walletService.save(newUserWallet);
                } else {
                    // Set a flash attribute for the error message
                    redirectAttributes.addFlashAttribute("errorMessage", "Error processing referral. Please try again.");
                    return "redirect:/register";
                }
            } else {
                // Set a flash attribute for the error message
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid referral code. Please provide a valid code or leave it blank.");
                return "redirect:/register";
            }
        } else {
            newUserWallet.setUser(user);
            newUserWallet.setReferralAmount(0);
            newUserWallet.setAmount(0);
            user.setWallet(newUserWallet);
            walletService.save(newUserWallet);
        }

        System.out.println("if completed...");
        String userReferralCode = walletService.getReferralCode();
        user.setUserReferralCode(userReferralCode);
        userRepository.save(user);
        userService.otpManagement(user);
        model.addAttribute("user", user);

        return "otpPage";
    }


    @PostMapping("/getOtpPage")
    public String  registerOtp(@ModelAttribute("email") String email,@ModelAttribute("otp") String otp ,Model model){
        int flag = userService.verifyAccount(email, otp);
        if(flag == 1){
            return "redirect:/";
        }else if(flag == 2){
             model.addAttribute("message","OTP time exceed!");
             model.addAttribute("user",userRepository.findUserByEmail(email).get());
             return "otpPage";
        }

        model.addAttribute("message","Please enter a valid OTP");
        model.addAttribute("user",userRepository.findUserByEmail(email).get());
        return "otpPage";

    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }




}
