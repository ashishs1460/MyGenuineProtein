package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.enums.PaymentMode;
import com.ashish.MyGenuineProtein.enums.Status;
import com.ashish.MyGenuineProtein.model.*;
import com.ashish.MyGenuineProtein.repository.OrderRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OrderServiceImp implements OrderService{

    @Autowired
    OrderRepository orderRepository;
    @Override
    public Order saveOrder(User user, List<CartItems> cartItems, double totalPrice, PaymentMode selectedPaymentMode, Address userAddress) {
        Order order = getOrder(user, totalPrice, selectedPaymentMode, userAddress);
        String orderId = generateOrderId();
        order.setOrderId(orderId);

        List<OrderItems> orderItems = cartItems.stream()
                .map(cartItem -> {
                    OrderItems orderItem = getOrderItem(cartItem);
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .toList();
        order.setOrderItems(orderItems);
        order.setStatus(Status.CONFIRMED);
        orderRepository.save(order);
        return order;
    }

    @Override
    public List<Order> findByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> findByOrderId(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }


    @NotNull
    private static OrderItems getOrderItem(CartItems cartItem) {
        OrderItems orderItem = new OrderItems();
        orderItem.setVariant(cartItem.getVariant());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setVariantPrice(cartItem.getVariant().getPrice());
        return orderItem;
    }
    @NotNull
    private static Order getOrder(User user, Double totalPrice, PaymentMode selectedPaymentMode,
                                  Address userAddress) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setTotalPrice(totalPrice);
        order.setPaymentMode(selectedPaymentMode);
        order.setAddress(userAddress);
        order.setOrderedAddress(userAddress.getFullAddress());
        return order;
    }


    public String generateOrderId() {
        int orderIdLength = 11;
        String prefix = "OD";
        String allowedChars = "0123456789";
        Random random =new Random();
        StringBuilder orderId = new StringBuilder(prefix);
        for (int i = 0; i < orderIdLength; i++) {
            int index = random.nextInt(allowedChars.length());
            orderId.append(allowedChars.charAt(index));
        }
        return orderId.toString();
    }
}
