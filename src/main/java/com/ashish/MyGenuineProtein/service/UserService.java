package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Cart;
import com.ashish.MyGenuineProtein.model.User;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

public interface UserService {


    User saveUser(User user);

    List<User> findAllUsers();
    Optional<User> findUserByEmail(String email);

    void otpManagement(User user);

    int verifyAccount(String email, String otp);


    void deleteCart(Cart userCart);

    User findByReferralCode(String referralCode);


    boolean validateEmail(String email);

    void sendMail(String email, String subject, String referralCode);


}
