package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.*;
import com.ashish.MyGenuineProtein.repository.CartItemRepository;
import com.ashish.MyGenuineProtein.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    CouponService couponService;



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

            if (cart.getCartItems() == null) {
                List<CartItems> cartItemsList = new ArrayList<>();
                cartItemsList.add(cartItems);
                cart.setCartItems(cartItemsList);
            } else {
                cart.getCartItems().add(cartItems);

            }




        }

        double variantPrice = variant.getPrice();

        double discountedVariantPrice = variant.getDiscountedPrice();

        if (discountedVariantPrice >0){
            cart.setTotal(cart.getTotal()+discountedVariantPrice);
        }else {
            cart.setTotal(cart.getTotal()+variantPrice);
        }
//        cart.setTotal(cart.getTotal()+variantPrice);



       Optional<Coupon> optionalCoupon = couponService.getCouponByCouponCode(cart.getCouponCode());
       if(optionalCoupon.isPresent()){
           Coupon coupon =optionalCoupon.get();
           if(coupon.getDiscountType().equals("PERCENTAGE")){
               cart.setDiscount(coupon.getDiscountValue()/100*cart.getTotal());

           }
       }

        cartRepository.save(cart);


    }

    @Override
    public void removeFromCart(Long cartItemId, Cart cart) {
        CartItems cartItem = cartItemRepository.findById(cartItemId).get();
        if(cartItem.getVariant().getDiscountedPrice()>0){
            cart.setTotal(cart.getTotal()-cartItem.getQuantity()*cartItem.getVariant().getDiscountedPrice());
        }else {
            cart.setTotal(cart.getTotal()-cartItem.getQuantity()*cartItem.getVariant().getPrice());
        }

        Optional<Coupon> optionalCoupon = couponService.getCouponByCouponCode(cart.getCouponCode());
        if (optionalCoupon.isPresent()){
            Coupon coupon = optionalCoupon.get();
            if (cart.getTotal()<coupon.getMinimumPurchase()){
                cart.setDiscount(0);
                cart.setCouponCode(null);
            }
        }

        cartRepository.save(cart);
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

                        cart.setTotal(cart.getTotal()+cartItem.getVariant().getPrice()*(newQuantity-cartItem.getQuantity()));
                        cartItem.setQuantity(newQuantity);
                        cartItemRepository.save(cartItem);
                        Coupon coupon = couponService.getCouponByCouponCode(cart.getCouponCode()).get();
                        if (cart.getTotal()<coupon.getMinimumPurchase()){
                            cart.setDiscount(0);
                            cart.setCouponCode(null);
                        }
                        cartRepository.save(cart);

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

    @Override
    public void save(Cart userCartEntity) {
        cartRepository.save(userCartEntity);
    }

    public  Cart createCart(User user) {
        Cart cart =new Cart();
        cart.setUser(user);
        user.setCart(cart);
        cartRepository.save(cart);
        return cart;
    }
}
