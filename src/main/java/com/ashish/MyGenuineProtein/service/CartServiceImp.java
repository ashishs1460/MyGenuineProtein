package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Cart;
import com.ashish.MyGenuineProtein.model.CartItems;
import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.model.Variant;
import com.ashish.MyGenuineProtein.repository.CartItemRepository;
import com.ashish.MyGenuineProtein.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImp implements CartService{

    @Autowired
    VariantService variantService;
    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    UserService userService;



    @Override
    public void addToCart(Long variantId, String username) {
        Variant variant = variantService.getVariantById(variantId).orElseThrow();
        User user =userService.findUserByEmail(username).orElseThrow();
        Cart cart =cartRepository.findByUser(user).orElseGet(()->createCart(user));


         Optional<CartItems> optionalCartItem = cartItemRepository.findByCartAndVariant(cart,variant);




        if (optionalCartItem.isPresent()) {
            CartItems existingCartItems = optionalCartItem.get();
            existingCartItems.setQuantity(existingCartItems.getQuantity() + 1);
            cartItemRepository.save(existingCartItems);

        } else {
            CartItems cartItems = new CartItems();
            cartItems.setQuantity(1);
            cartItems.setVariant(variant);
            cartItems.setCart(cart);
            cart.getCartItems().add(cartItems);
            cartRepository.save(cart);
        }







    }

    @Override
    public void removeFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void updateCartItem(String user, Long variantId, int newQuantity) {
        Optional<User> optionalUser = userService.findUserByEmail(user);
        try {
            if(optionalUser.isPresent()){
                User user1 = optionalUser.get();
                Cart cart = user1.getCart();
                List<CartItems> cartItems =cart.getCartItems();
                for (CartItems cartItem: cartItems) {
                    if(cartItem.getVariant().getId().equals(variantId)){
                        cartItem.setQuantity(newQuantity);
                        cartItemRepository.save(cartItem);

                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public double calculateTotalPrice(List<CartItems> cartItems) {
        return cartItems.stream().mapToDouble(cartItem ->
                cartItem.getVariant().getPrice() * cartItem.getQuantity()).sum();
    }

    @Override
    public void deleteCart(Cart cart) {
        cartRepository.delete(cart);
    }

    public Cart createCart(User user) {
        Cart cart =new Cart();
        cart.setUser(user);
        user.setCart(cart);
        cartRepository.save(cart);
        userService.saveUser(user);
        return cart;
    }
}
