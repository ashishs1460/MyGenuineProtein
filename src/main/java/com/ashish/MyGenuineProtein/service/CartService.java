package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.CartItem;

import java.util.UUID;

public interface CartService {

    void addToCart(Long id, String username);

    void  removeFromCart(Long CartItemId);

    void updateCartItem(String user, Long variantId, int newQuantity);
}
