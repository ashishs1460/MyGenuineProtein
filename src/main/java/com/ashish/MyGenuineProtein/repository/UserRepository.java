package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmail(String email);

    Page<User> findAll(Pageable pageable);

    User findByUserReferralCode(String referralCode);
}
