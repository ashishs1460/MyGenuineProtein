package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Cart;
import com.ashish.MyGenuineProtein.model.CartItem;
import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.model.Variant;
import com.ashish.MyGenuineProtein.repository.CartItemRepository;
import com.ashish.MyGenuineProtein.repository.CartRepository;
import com.ashish.MyGenuineProtein.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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


         Optional<CartItem> optionalCartItem = cartItemRepository.findByCartAndVariant(cart,variant);




        if (optionalCartItem.isPresent()) {
            CartItem existingCartItem = optionalCartItem.get();
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
            cartItemRepository.save(existingCartItem);

        } else {
            CartItem cartItem = new CartItem();
            cartItem.setQuantity(1);
            cartItem.setVariant(variant);
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
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
                List<CartItem> cartItems =cart.getCartItems();
                for (CartItem cartItem: cartItems) {
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

    public Cart createCart(User user) {
        Cart cart =new Cart();
        cart.setUser(user);
        user.setCart(cart);
        cartRepository.save(cart);
        userService.saveUser(user);
        return cart;
    }
}
