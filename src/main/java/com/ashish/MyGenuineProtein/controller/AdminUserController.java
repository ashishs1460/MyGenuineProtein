package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.repository.RoleRepository;
import com.ashish.MyGenuineProtein.repository.UserRepository;
import com.ashish.MyGenuineProtein.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class AdminUserController {


    @Autowired
    UserRepository userRepository;



    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/admin/getUsers")
    public String getUsersPage(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        model.addAttribute("users", usersPage);
        model.addAttribute("role", roleRepository.findAll());

        // Calculate total pages and current page
        int totalPages = usersPage.getTotalPages();
        int currentPage = usersPage.getNumber();

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        return "getUsers";
    }

    @GetMapping("/admin/block/{id}")
    public String blockUser(@PathVariable UUID id){

        User user= userRepository.findById(id).get();
        user.setActive(false);

        userRepository.save(user);
        return "redirect:/admin/getUsers";


    }
    @GetMapping("/admin/unblock/{id}")
    public String unBlockUser(@PathVariable UUID id){

        User user= userRepository.findById(id).get();
        user.setActive(true);

        userRepository.save(user);
        return "redirect:/admin/getUsers";


    }
}
