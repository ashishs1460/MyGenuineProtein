package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.model.Address;
import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.service.AddressService;
import com.ashish.MyGenuineProtein.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class AddressController {

    @Autowired
    UserService userService;
    @Autowired
    AddressService addressService;
    @GetMapping("/addAddress")
    public String getAddressForm(@RequestParam(name = "editSource",required = false,defaultValue = "profile") String editSource,
                                 Principal principal,
                                 Model model){
        Optional<User> optionalUser = userService.findUserByEmail(principal.getName());
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            String username = user.getFirstName();
            model.addAttribute("username", username);
        }

        model.addAttribute("addresses",new Address());
        model.addAttribute("editSource",editSource);
        return "addressForm";

    }
    @PostMapping("/saveAddress")
    public String saveAddress(@ModelAttribute Address address,
                              @RequestParam("editSource") String editSource,
                              Model model,
                              Principal principal){

        User user = userService.findUserByEmail(principal.getName()).orElseThrow();
        addressService.addNewAddress(address,user);
        if ("profile".equals(editSource)){
            model.addAttribute("user",user);
            return "redirect:/user/address";
        } else if ("checkout".equals(editSource)) {
            model.addAttribute("user",user);
            return "redirect:/cart/checkout";
        }else {
            return "404";
        }

    }
}
