package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Cart;
import com.ashish.MyGenuineProtein.model.CartItems;

import java.util.List;

public interface CartService {

    void addToCart(Long id, String username);

    void  removeFromCart(Long CartItemId);

    void updateCartItem(String user, Long variantId, int newQuantity);

    double calculateTotalPrice(List<CartItems> cartItems);

    void deleteCart(Cart userCart);
}
