package com.ashish.MyGenuineProtein.otp.repository;

import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.otp.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface OtpRepository extends JpaRepository<Otp,Long> {
    Optional<Otp> findByUser(User user);
}
