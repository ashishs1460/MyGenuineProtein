package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.model.Wallet;
import com.ashish.MyGenuineProtein.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService{

    @Autowired
    WalletRepository walletRepository;

    @Override
    public Optional<Wallet> findByUser(User user) {
        return walletRepository.findByUser(user);
    }

    @Override
    public void save(Wallet wallet) {
        walletRepository.save(wallet);
    }
}
