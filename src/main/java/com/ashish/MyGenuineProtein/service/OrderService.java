package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.enums.PaymentMode;
import com.ashish.MyGenuineProtein.model.Address;
import com.ashish.MyGenuineProtein.model.CartItems;
import com.ashish.MyGenuineProtein.model.Order;
import com.ashish.MyGenuineProtein.model.User;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order saveOrder(User user, List<CartItems> cartItems, double totalPrice, PaymentMode selectedPaymentMode, Address userAddress);

    List<Order> findByUser(User user);

    List<Order> findAll();

    Optional<Order> findByOrderId(Long orderId);

    void save(Order order);


}
