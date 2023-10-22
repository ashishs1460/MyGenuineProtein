package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAllUsers();
    Optional<User> findUserByEmail(String email);

    void otpManagement(User user);

    int verifyAccount(String email, String otp);
}