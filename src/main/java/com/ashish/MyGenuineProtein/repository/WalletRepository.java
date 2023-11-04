package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
