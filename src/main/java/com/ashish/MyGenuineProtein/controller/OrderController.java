package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.enums.PaymentMode;
import com.ashish.MyGenuineProtein.enums.Status;
import com.ashish.MyGenuineProtein.model.Order;
import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.model.Wallet;
import com.ashish.MyGenuineProtein.service.OrderService;
import com.ashish.MyGenuineProtein.service.UserService;
import com.ashish.MyGenuineProtein.service.VariantService;
import com.ashish.MyGenuineProtein.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {


    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    VariantService variantService;
    @Autowired
    WalletService walletService;

    @GetMapping("/order/viewOrder/{id}")
    public  String viewOrder(@PathVariable long id,
                             Model model,
                             Principal principal){

        Optional<Order> optionalOrder = orderService.findByOrderId(id);
        if(optionalOrder.isPresent()){
            Order order = optionalOrder.get();

            model.addAttribute("order",order);
            return "view-order";
        }

        return "404";

    }

    @GetMapping("/order/cancelOrder/{id}")
    public String cancelOrder(@PathVariable("id") Long orderId, Principal principal, RedirectAttributes redirectAttributes) {

        //Already logged-in user block
        if (userService.findUserByEmail(principal.getName()).isEmpty()) {
            return "redirect:/logout";
        }

        Optional<Order> optionalOrder = orderService.findByOrderId(orderId);

        if (optionalOrder.isPresent()){
            Order order = optionalOrder.get();
            PaymentMode paymentMode = order.getPaymentMode();

            order.setStatus(Status.CANCELLED);
            orderService.save(order);
                User user = userService.findUserByEmail(principal.getName()).get();
                double refund = order.getTotalPrice();
            if (paymentMode != PaymentMode.COD){
                Wallet wallet = user.getWallet();

                if (wallet != null) {
                    // Wallet exists, update the amount
                    wallet.setAmount(wallet.getAmount() + refund);
                    walletService.save(wallet);
                } else {
                    // Wallet is null, create a new wallet and set the amount
                    Wallet newWallet = new Wallet();
                    newWallet.setAmount(refund);
                    newWallet.setUser(user);
                    walletService.save(newWallet);
                }
                redirectAttributes.addFlashAttribute("moneyCredited", "Invoice amount of ₹" +refund + " has been credited back to your wallet");
            }else {
                redirectAttributes.addFlashAttribute("moneyCredited", "Order Cancelled!");
            }

               variantService.increaseVariantStock(order);




        }




        return "redirect:/order/viewOrder/"+orderId;
    }

    @GetMapping("/order/selectReturnReason/{id}")
    public String selectReturnReasonPage(@PathVariable("id") Long orderId, Model model) {
        // Retrieve the order and add it to the model
        Optional<Order> optionalOrder = orderService.findByOrderId(orderId);
        optionalOrder.ifPresent(order -> model.addAttribute("order", order));

        // Add any additional data needed for the return reason page

        return "returnReasonPage";
    }
    @PostMapping("/order/returnOrder/{id}")
    public String returnOrderWithReason(@PathVariable("id") Long orderId,
                                        @RequestParam(value = "returnReasons" ,required = false) String returnReasons,
                                        Principal principal, RedirectAttributes redirectAttributes) {
        // Already logged-in user block
        if (userService.findUserByEmail(principal.getName()).isEmpty()) {
            return "redirect:/logout";
        }

        // Check if a valid return reason is selected
        if (returnReasons == null || returnReasons.isEmpty()) {
            // Add flash attribute for an error message if no reason is selected
            redirectAttributes.addFlashAttribute("errorMessage", "Please select at least one reason for return");
            return "redirect:/order/selectReturnReason/" + orderId;
        }
        Optional<Order> optionalOrder = orderService.findByOrderId(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();



                order.setStatus(Status.RETURN_PENDING);
                orderService.save(order);


                // Add flash attribute for success message
                redirectAttributes.addFlashAttribute("returnMessage", "Product return initiated successfully");

        }

        return "redirect:/order/viewOrder/" + orderId;
    }

    @GetMapping("/order/refund/{id}")
    public String returnRefund(@PathVariable("id") long id,
                               RedirectAttributes redirectAttributes,
                               Principal principal){

        if(userService.findUserByEmail(principal.getName()).isEmpty()){
            return "redirect:/login";
        }

        Optional<Order> optionalOrder = orderService.findByOrderId(id);
        System.out.println("It's here");

        if(optionalOrder.isPresent()){
            Order order = optionalOrder.get();
            Status stat = Status.REFUNDED;
            order.setStatus(stat);

            orderService.save(order);
            User user = userService.findUserByEmail(principal.getName()).get();
            double refund = order.getTotalPrice();
            Wallet wallet = user.getWallet();
            wallet.setAmount(wallet.getAmount()+refund);
            redirectAttributes.addFlashAttribute("moneyCredited", "Invoice amount of ₹" +refund + " has been credited back to your wallet");

            variantService.increaseVariantStock(order);
            walletService.save(wallet);
        }

        return "redirect:/order/viewOrder/"+id;
    }

}
