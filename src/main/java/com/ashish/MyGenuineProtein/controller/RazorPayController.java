package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.enums.PaymentMode;
import com.ashish.MyGenuineProtein.enums.Status;
import com.ashish.MyGenuineProtein.model.Address;
import com.ashish.MyGenuineProtein.model.Cart;
import com.ashish.MyGenuineProtein.model.CartItems;
import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.service.*;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class RazorPayController {


    @Autowired
    UserService userService;

    @Autowired
    VariantService variantService;

    @Autowired
    CartService cartService;
    @Autowired
    OrderService orderService;
    @Autowired
    AddressService addressService;

    @GetMapping("/payment/{totalAmount}")
    public String razorPay(@PathVariable Double totalAmount,
                           @RequestParam("addressId") long addressId,
                           @RequestParam("selectedPaymentMode") PaymentMode selectedPaymentMode,
                           Model model) throws RazorpayException {

        RazorpayClient razorpay = new RazorpayClient("rzp_test_wSP8EmmFjEbRbD", "wDhpCDheW2ZzApA89aD6hhAu");

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", totalAmount * 100);
        orderRequest.put("currency", "INR");
        Order order = razorpay.orders.create(orderRequest);
        model.addAttribute("amount", totalAmount * 100);
        model.addAttribute("orderId", order.get("id"));
        model.addAttribute("addressId", addressId);
        model.addAttribute("selectedPaymentMode", selectedPaymentMode);
        model.addAttribute("totalAmount",totalAmount);

        return "razorPay";


    }

    @GetMapping("/razorPaySuccess")
    public String success(Model model,
                          Principal principal,
                          @RequestParam("addressId") long addressId,
                          @RequestParam("selectedPaymentMode")PaymentMode selectedPaymentMode,
                          @RequestParam("totalAmount") Double totalPrice
                          ) {

            Optional<User> optionalUser = userService.findUserByEmail(principal.getName());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                Cart userCart = user.getCart();
                List<CartItems> cartItem = userCart.getCartItems();
                Address userAddress = addressService.findById(addressId).get();
                com.ashish.MyGenuineProtein.model.Order order = orderService.saveOrder(user, cartItem, totalPrice, selectedPaymentMode, userAddress);
                LocalDate expectedDeliveryDate = order.getOrderDate().plusDays(7);
                    variantService.reduceVariantStock(cartItem);
                    userService.deleteCart(userCart);
                    cartService.deleteCart(userCart);

                    model.addAttribute("expectedDeliveryDate", expectedDeliveryDate);
                    model.addAttribute("order", order);
                    return "orderConfirmation";
                }
            return "redirect:/login";
            }


}
