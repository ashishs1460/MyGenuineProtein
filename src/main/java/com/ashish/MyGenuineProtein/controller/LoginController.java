package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.model.Role;
import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.otp.repository.OtpRepository;
import com.ashish.MyGenuineProtein.repository.RoleRepository;
import com.ashish.MyGenuineProtein.repository.UserRepository;
import com.ashish.MyGenuineProtein.service.UserService;
import com.ashish.MyGenuineProtein.service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class LoginController {


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

    @GetMapping("/login")
    public String getLoginPage(){

        return "login";
    }

    @GetMapping("/register")
    public String getRegisterPage(){
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@ModelAttribute("User") User user, Model model){
        if(userRepository.findUserByEmail(user.getEmail()).isPresent()){
            if(userRepository.findUserByEmail(user.getEmail()).get().isVerified()){
                return "redirect:/register";
            }else {
                otpRepository.delete(otpRepository.findByUser(userRepository.findUserByEmail(user.getEmail()).get()).get());
                userRepository.delete(userRepository.findUserByEmail(user.getEmail()).get());
            }
        }
        String password =user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        List<Role> roles=new ArrayList<>();
        roles.add(roleRepository.findById(UUID.fromString("56d445f0-6f53-11ee-b70a-6f05ccf153cb")).get());
//        roles.add(roleRepository.findById(UUID.fromString("4c3d830e-6f53-11ee-b70a-6f05ccf153cb")).get());
        user.setRoles(roles);
        user.setActive(true);
        userRepository.save(user);
        userService.otpManagement(user);
        model.addAttribute("user" ,user);


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


}