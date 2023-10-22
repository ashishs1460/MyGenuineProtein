package com.ashish.MyGenuineProtein.otp.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendEmail {
    @Autowired
    private JavaMailSender sender;

    public void send(String toEmailAdd,String otp){

        SimpleMailMessage smm=new SimpleMailMessage();
//        smm.setFrom("ashishs24199@gmail.com");
        smm.setTo(toEmailAdd);
        smm.setSubject("This is for verification");
        smm.setText("Your OTP is "+otp);
        sender.send(smm);

    }
}
