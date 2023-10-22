package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.CustomUserDetail;
import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional=userRepository.findUserByEmail(username);



        User user;
        if(userOptional.isPresent()){
            user=userOptional.get();
        }else {

            user= null;

        }
        if(user==null){
            throw new RuntimeException("Invalid email");
        }
        if(!user.isActive()){
            throw new RuntimeException("user is disabled by the admin");
        }

        if(!user.isVerified()){
            throw new RuntimeException("OTP Authentication failed!");
        }


        return userOptional.map(CustomUserDetail::new).get();
    }
}
