package com.ashish.MyGenuineProtein.controller;


import com.ashish.MyGenuineProtein.enums.PaymentMode;
import com.ashish.MyGenuineProtein.model.*;
import com.ashish.MyGenuineProtein.repository.CartItemRepository;
import com.ashish.MyGenuineProtein.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class CartController {

    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;

    @Autowired
    VariantService variantService;
    @Autowired
    CartService cartService;

    @Autowired
    AddressService addressService;
    @Autowired
    OrderService orderService;
    @Autowired
    WalletService walletService;

    @Autowired
    CouponService couponService;

    @Autowired
    CartItemRepository cartItemRepository;

    @GetMapping("/add/{id}")
    @ResponseBody
    public ResponseEntity<String> addCart(@PathVariable("id") Long id, Principal principal) {

        try {
            Optional<User> optionalUser = userService.findUserByEmail(principal.getName());
            if (optionalUser.isPresent()) {
                User user =optionalUser.get();
                Variant variant = variantService.getVariantById(id).orElseThrow();

                if (variant.getStock() <= 0) {
                    return ResponseEntity.ok("Out of Stock");
                }

                cartService.addToCart(id, user.getEmail());
                return ResponseEntity.ok("Added to cart");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("cart/goToCart")
    public String getCartPage(Model model, Authentication authentication, Principal principal) {
        if (principal == null) {
            return "login";
        } else {
            User user = userService.findUserByEmail(principal.getName()).orElseThrow();
            Cart cart = user.getCart();

            if (cart != null) {
                List<CartItems> cartItems = cart.getCartItems();
                int quantity = cartItems.stream()
                        .map(item -> item.getQuantity())
                        .reduce(0, (a, b) -> a + b);
                double totalAmount = cart.getTotal()-cart.getDiscount();
                model.addAttribute("cartItems", cartItems);
                model.addAttribute("quantity", quantity);
                model.addAttribute("total", totalAmount);
            } else {
                // Handle the case when the cart is empty or not initialized
                model.addAttribute("cartItems", new ArrayList<CartItems>()); // Empty cart
                model.addAttribute("quantity", 0); // No items
                model.addAttribute("total", 0); // Total amount is zero
            }
        }
        return "cart";
    }



    @GetMapping ("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id,
                                 RedirectAttributes redirectAttributes,
                                 Principal principal) {

            // Remove the cart item from the list
        Optional<User> userOptional = userService.findUserByEmail(principal.getName());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Cart cart = user.getCart();

            cartService.removeFromCart(id, cart);

            redirectAttributes.addFlashAttribute("successMsg", "Item removed from cart");

            return "redirect:/cart/goToCart";
        } else {
            // Handle the case where the user is not found
            // You might want to redirect to an error page or do some other error handling
            return "redirect:/error";
        }


    }

    @PostMapping("/cart/update-cart")
    public String updateCart(@RequestParam Long id, @RequestParam int newQuantity, Principal principal
                            ) {
        if (principal == null) {
            return "redirect:/login";
        }
        String user = principal.getName();
        System.out.println(user);
       if(newQuantity>=1 && newQuantity <= 5){
           cartService.updateCartItem(user, id, newQuantity);
       }


        return "redirect:/cart/goToCart";
    }

    @GetMapping("/cart/checkout")
    public String showCheckOut(Model model,Principal principal,
                               RedirectAttributes redirectAttributes){


        Optional<User> optionalUser = userService.findUserByEmail(principal.getName());

        if(optionalUser.isPresent()){
            User user =optionalUser.get();
            List<Address> addressList = addressService.findAllUserAddresses(user)
                    .stream()
                    .filter(address -> !address.isDelete())
                    .sorted(Comparator.comparing(Address::getCreatedAt).reversed()).toList();


            model.addAttribute("Addresses",addressList);
            model.addAttribute("user",user);


            Cart cart = user.getCart();

            List<CartItems> cartItems = cart.getCartItems();
            model.addAttribute("cartItems",cartItems);

//            double totalPrice = cartItems.stream()
//                    .mapToDouble(cartItem -> cartItem.getVariant().getPrice() * cartItem.getQuantity())
//                    .sum();
            double totalPrice = cart.getTotal()-cart.getDiscount();
            model.addAttribute("totalPrice",totalPrice);
            if(cart.getCouponCode() != null)
                 model.addAttribute("coupon",cart.getCouponCode());

        }
       

        return "checkout";


    }

    @PostMapping("/checkout/applyCoupon")
    public String applyCoupon(@ModelAttribute("couponCode") String couponCode, Principal principal,
                              RedirectAttributes  redirectAttributes) {

        Optional<Coupon> couponOptional = couponService.getCouponByCouponCode(couponCode);

        User user = userService.findUserByEmail(principal.getName()).get();
        List<Address> addressList = addressService.findAllUserAddresses(user)
                .stream()
                .filter(address -> !address.isDelete())
                .sorted(Comparator.comparing(Address::getCreatedAt).reversed()).toList();

        Cart userCartEntity = user.getCart();
        List<CartItems> cartItems = userCartEntity.getCartItems();

        double totalPrice = userCartEntity.getTotal();



        if (couponOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("invalidCoupon", "Invalid coupon code");
            return "redirect:/cart/checkout";
        }

        if (totalPrice < couponOptional.get().getMinimumPurchase()) {
            redirectAttributes.addFlashAttribute("invalidCoupon", "This coupon is only valid for purchases of " + couponOptional.get().getMinimumPurchase() + " and above");

            return "redirect:/cart/checkout";
        }
        userCartEntity.setDiscount(0);
        cartService.save(userCartEntity);

        if (couponOptional.get().getDiscountType().equals("ABSOLUTE")) {

            userCartEntity.setDiscount(couponOptional.get().getDiscountValue());
            userCartEntity.setCouponCode(couponOptional.get().getCouponCode());
//            totalPrice = totalPrice - couponOptional.get().getDiscountValue();
            cartService.save(userCartEntity);

            redirectAttributes.addFlashAttribute("discountApplied", "You get a discount of ₹" + couponOptional.get().getDiscountValue());
            redirectAttributes.addFlashAttribute("couponApplied", "Coupon applied");

            return "redirect:/cart/checkout";

        }

        if (couponOptional.get().getDiscountType().equals("PERCENTAGE")) {

            userCartEntity.setDiscount(couponOptional.get().getDiscountValue()/100*totalPrice);
            userCartEntity.setCouponCode(couponOptional.get().getCouponCode());
            cartService.save(userCartEntity);
//            totalPrice = totalPrice - (couponOptional.get().getDiscountValue()/100*totalPrice);

            redirectAttributes.addFlashAttribute("discountApplied", "You get a discount of " + couponOptional.get().getDiscountValue() + "%");
            redirectAttributes.addFlashAttribute("couponApplied", "Coupon applied");


            return "redirect:/cart/checkout";

        }

      return "404";

    }

    @PostMapping("/cart/placeOrder")
    public String placeOrder(Model model,
                             @RequestParam(value = "selectedAddressId", required = false) Long addressId,
                             @RequestParam(value = "paymentMethod", required = false) PaymentMode selectedPaymentMode,
                             Principal principal, RedirectAttributes redirectAttributes) {

        if (addressId==null) {
            redirectAttributes.addFlashAttribute("errorAddress", "Please select an address.");
            return "redirect:/cart/checkout";
        }

        if (principal != null) {
            Optional<User> optionalUser= userService.findUserByEmail(principal.getName());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                Cart userCart = user.getCart();
                List<CartItems> cartItems = userCart.getCartItems();


//                System.out.println( "CartItems"+cartItems);


                double totalPrice = userCart.getTotal()-userCart.getDiscount();


                Optional<Address> optionalUserAddress = addressService.findById(addressId);
                if (optionalUserAddress.isPresent()) {
                    Address userAddress = optionalUserAddress.get();
                    try {


                        if (isCod(selectedPaymentMode)) {
                            Order order = orderService.saveOrder(user, cartItems, totalPrice, selectedPaymentMode, userAddress);
                            LocalDate expectedDeliveryDate = order.getOrderDate().plusDays(7);
                            handleCodPayment(model, userCart, cartItems, order, expectedDeliveryDate);
                            return "orderConfirmation";
                        }else if (isRazorpay(selectedPaymentMode)) {
                            return "redirect:/payment/" + totalPrice + "?addressId=" + addressId + "&selectedPaymentMode=" + selectedPaymentMode;
                        }else if (isWalletPay(selectedPaymentMode)){

                            Optional<Wallet> optionalWallet = walletService.findByUser(user);
                            if(optionalWallet.isPresent()){
                                Wallet wallet = optionalWallet.get();
                                if (totalPrice <= wallet.getAmount()){
                                    Order order = orderService.saveOrder(user, cartItems, totalPrice, selectedPaymentMode, userAddress);
                                    LocalDate expectedDeliveryDate = order.getOrderDate().plusDays(7);
                                    handleWalletPayment(model,userCart,cartItems,order,expectedDeliveryDate,wallet,totalPrice);
                                    return "orderConfirmation";
                                }else {
                                    redirectAttributes.addFlashAttribute("error", "Insufficient fund in the wallet , please try another payment methods!");
                                    return "redirect:/cart/checkout";
                                }

                            }
                        }
                        else {
                            redirectAttributes.addFlashAttribute("error", "payment method not selected");
                            return "redirect:/cart/checkout";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return "redirect:/login";
    }

    private void handleWalletPayment(Model model,
                                     Cart userCart,
                                     List<CartItems> cartItems,
                                     Order order,
                                     LocalDate expectedDeliveryDate,
                                     Wallet wallet,
                                     double totalPrice) {
        wallet.setAmount(wallet.getAmount()-totalPrice);
        walletService.save(wallet);
        variantService.reduceVariantStock(cartItems);
        userService.deleteCart(userCart);
        cartService.deleteCart(userCart);
        model.addAttribute("order",order);
        model.addAttribute("expectedDeliveryDate",expectedDeliveryDate);

    }

    private void handleCodPayment(Model model,
                                  Cart userCart,
                                  List<CartItems> cartItems,
                                  Order order,
                                  LocalDate expectedDeliveryDate) {

        variantService.reduceVariantStock(cartItems);
        userService.deleteCart(userCart);
        cartService.deleteCart(userCart);

        model.addAttribute("order", order);
        model.addAttribute("expectedDeliveryDate", expectedDeliveryDate);


    }

    private static boolean isCod(PaymentMode selectedPaymentMode) {
        return selectedPaymentMode == PaymentMode.COD;
    }

    private  static  boolean isRazorpay(PaymentMode selectedPaymentMode){
        return selectedPaymentMode == PaymentMode.RAZORPAY;
    }

    private static  boolean isWalletPay(PaymentMode selectedPaymentMode){
        return selectedPaymentMode == PaymentMode.WALLET;
    }





}




