package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Cart;
import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.otp.model.Otp;
import com.ashish.MyGenuineProtein.otp.repository.OtpRepository;
import com.ashish.MyGenuineProtein.otp.utils.SendEmail;
import com.ashish.MyGenuineProtein.repository.CartItemRepository;
import com.ashish.MyGenuineProtein.repository.CartRepository;
import com.ashish.MyGenuineProtein.repository.UserRepository;
import com.ashish.MyGenuineProtein.otp.utils.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService{


    @Autowired
    OtpUtil otpUtil;

    @Autowired
    SendEmail sendEmail;


    @Autowired
    OtpRepository otpRepository;

    @Autowired
    UserRepository userRepository;


    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;


    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findAllUsers() {

        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {

        return userRepository.findUserByEmail(email);
    }

    @Override
    public void otpManagement(User user) {
        String otp= otpUtil.generateOtp();
        sendEmail.send(user.getEmail(),otp);

        Otp otp1=new Otp();
        otp1.setOtp(otp);
        otp1.setTime(LocalDateTime.now());
        otp1.setUser(user);

        otpRepository.save(otp1);



    }

    @Override
    public int verifyAccount(String email, String otp) {
        User user=userRepository.findUserByEmail(email).get();
        Otp otp1=otpRepository.findByUser(user).get();
        if(otp1.getOtp().equals(otp) ){
            if( Duration.between(otp1.getTime(),LocalDateTime.now()).getSeconds()<=300){
                user.setVerified(true);
                userRepository.save(user);
                otpRepository.delete(otp1);
                return 1;
            }else {
                return 2;
            }
        }

        return 0;
    }

    @Override
    public void deleteCart(Cart cart) {
        User user = userRepository.findById(cart.getUser().getId()).orElse(null);
        assert user != null;
        user.setCart(null);
        userRepository.save(user);

    }

    public void deleteCartItemById(Long id) {
        cartItemRepository.deleteById(id);
    }







}
