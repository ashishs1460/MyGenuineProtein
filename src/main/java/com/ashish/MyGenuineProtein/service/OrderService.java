package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.enums.PaymentMode;
import com.ashish.MyGenuineProtein.model.Address;
import com.ashish.MyGenuineProtein.model.CartItems;
import com.ashish.MyGenuineProtein.model.Order;
import com.ashish.MyGenuineProtein.model.User;

import java.util.List;

public interface OrderService {
    Order saveOrder(User user, List<CartItems> cartItems, double totalPrice, PaymentMode selectedPaymentMode, Address userAddress);
}
