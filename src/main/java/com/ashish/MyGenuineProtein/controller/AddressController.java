package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.model.Address;
import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.service.AddressService;
import com.ashish.MyGenuineProtein.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
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

    @GetMapping("/editAddress/{id}")
    public String getEditPage(@PathVariable Long id,
                              @RequestParam(value = "editSource",required = false,defaultValue = "profile") String editSource,
                              Principal principal,
                              Model model){
        Optional<User> optionalUser = userService.findUserByEmail(principal.getName());
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            String username = user.getFirstName();
            model.addAttribute("username", username);
        }
      Optional<Address> optionalAddress = addressService.findById(id);
        if(optionalAddress.isPresent()){
            Address address = optionalAddress.get();
            model.addAttribute("address",address);
            model.addAttribute("editSource",editSource);
            return "editAddress";
        }
        return "404";

    }

    @PostMapping("/updateAddress")
    public String updateAddress(@ModelAttribute Address address,
                                @RequestParam(value = "editSource",required = false,defaultValue = "profile") String editSource,
                                Principal principal
                                ){
        User user = userService.findUserByEmail(principal.getName()).get();
        address.setUser(user);
        address.setCreatedAt(   addressService.findById(address.getId()).get().getCreatedAt());
        addressService.editAddress(address);
        if ("profile".equals(editSource)) {
            return "redirect:/user/address";
        } else if ("checkout".equals(editSource)) {
            return "redirect:/cart/checkout";
        } else {
            return "404";
        }
    }

    @GetMapping("/address")
    public String userAddressList(Model model,Principal principal){
      Optional<User> optionalUser = userService.findUserByEmail(principal.getName());
      if (optionalUser.isPresent()){
          User user = optionalUser.get();
          List<Address> userAddress =addressService.findAllUserAddresses(user).stream().
                  filter(address -> !address.isDelete()).toList();
          String username = user.getFirstName();
          model.addAttribute("username", username);
          model.addAttribute("user",user);
          model.addAttribute("userAddress",userAddress);
          return "userAddress";

      }else {
          return "404";
      }

    }

    @GetMapping("/delete/address/{id}")
    public String deleteAddress(@PathVariable("id") Long id){
        Optional<Address> optionalAddress = addressService.findById(id);
        if (optionalAddress.isPresent()){
            Address address = optionalAddress.get();
            address.setDelete(true);
            addressService.saveAddress(address);
        }
        return "redirect:/user/address";
    }

    @GetMapping("/setDefault/{id}")
    public String setDefaultAddress(@PathVariable("id") Long addressId, Principal principal) {
        User user = userService.findUserByEmail(principal.getName()).get();
        addressService.setDefaultAddress(addressId, user);
        return "redirect:/user/address"; // Redirect to the address management page
    }


}
