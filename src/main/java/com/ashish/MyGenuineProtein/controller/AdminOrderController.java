package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.enums.Status;
import com.ashish.MyGenuineProtein.model.Order;
import com.ashish.MyGenuineProtein.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/admin")
public class AdminOrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/getOrders")
    public String getOrderPage(Model model) {
        List<Order> orders = orderService.findAll();
        // You can sort the orders by date in descending order, for example
        orders.sort(Comparator.comparing(Order::getOrderDate).reversed());
        System.out.println(orders.get(0).getUser().getFirstName());
        model.addAttribute("orders", orders);
        return "orderManagement";
    }

    @GetMapping("/updateOrderStatus/{orderId}")
    public String getUpdateOrderStatusPage(@PathVariable Long orderId, Model model) {
       Optional<Order> order = orderService.findByOrderId(orderId);

        if (order != null) {
            model.addAttribute("order", order.get());
            return "updateOrderStatus";
        } else {
            // Handle order not found
            return "redirect:/admin/orderManagement";
        }
    }

    @PostMapping("/updateOrderStatus/{orderId}")
    public String updateOrderStatus(@PathVariable Long orderId, @RequestParam("status") Status status) {
        Optional<Order> order = orderService.findByOrderId(orderId);

        if (order.isPresent()) {
            order.get().setStatus(status);
            orderService.save(order.get());
            return "redirect:/admin/getOrders";
        } else {
            // Handle order not found
            return "redirect:/admin/getOrders";
        }
    }










}
