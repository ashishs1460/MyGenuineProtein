package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class CartServiceImp implements CartService{
    @Autowired
    CartRepository cartRepository;
    @Override
    public void addToCart(UUID id, String username) {


    }
}
