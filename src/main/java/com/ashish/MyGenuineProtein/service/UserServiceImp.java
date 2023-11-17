package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Cart;
import com.ashish.MyGenuineProtein.model.CartItems;
import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.otp.model.Otp;
import com.ashish.MyGenuineProtein.otp.repository.OtpRepository;
import com.ashish.MyGenuineProtein.otp.utils.SendEmail;
import com.ashish.MyGenuineProtein.repository.CartItemRepository;
import com.ashish.MyGenuineProtein.repository.CartRepository;
import com.ashish.MyGenuineProtein.repository.UserRepository;
import com.ashish.MyGenuineProtein.otp.utils.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

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


    @Autowired
    JavaMailSender mailSender;


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

    @Override
    public User findByReferralCode(String referralCode) {
        return userRepository.findByUserReferralCode(referralCode);
    }

    @Override
    public boolean validateEmail(String email) {
        System.out.println(email);
        String emailRegex =  "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";

        System.out.println(emailRegex);

        if (!Pattern.matches(emailRegex, email)) {

            return false;
        }
        return true;
    }

    public void sendMail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ashishs24199@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject); // Set the subject of the email
        message.setText(body);       // Set the email body content
        mailSender.send(message);

    }

    public void  deleteCartItem(CartItems cartItems){
        Optional<CartItems> cartId=cartItemRepository.findById(cartItems.getId());

    }

    public void deleteCartItemById(Long id) {
        cartItemRepository.deleteById(id);
    }







}
