package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.model.Wallet;

import java.util.Optional;

public interface WalletService {

    Optional<Wallet> findByUser(User user);

    void save(Wallet wallet);

    String getReferralCode();
}
