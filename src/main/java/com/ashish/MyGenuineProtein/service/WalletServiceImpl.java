package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.model.Wallet;
import com.ashish.MyGenuineProtein.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

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
        System.out.println("Wallet in service......"+wallet);
        walletRepository.save(wallet);
    }

    @Override
    public String getReferralCode() {

        UUID uuid = UUID.randomUUID();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        int codeLength = 8;
        SecureRandom random = new SecureRandom();
        char[] codeChars = new char[codeLength];
        for (int i = 0; i < codeLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            codeChars[i] = characters.charAt(randomIndex);
        }

        String uniqueCode = new String(codeChars);

        System.out.println("Referral Code: " + uniqueCode);

        return uniqueCode;

    }
}
