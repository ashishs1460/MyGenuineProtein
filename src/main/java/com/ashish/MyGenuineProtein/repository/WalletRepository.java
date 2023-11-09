package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUser(User user);
}
