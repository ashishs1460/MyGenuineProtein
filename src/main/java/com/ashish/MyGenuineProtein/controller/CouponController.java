package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.model.Coupon;
import com.ashish.MyGenuineProtein.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CouponController {
    @Autowired
    CouponService couponService;

    @GetMapping("/admin/getCoupon")
    public String  getCouponPage(Model model){
        List<Coupon> coupons = couponService.findAll();
        model.addAttribute("coupons",coupons);
        return "coupons";

    }

    @GetMapping("/admin/createCoupon")
    public String createCoupon(Model model){
        model.addAttribute("coupon",new Coupon());
        return "createCoupon";
    }

    @PostMapping("/admin/createCoupon")
    public String createCoupon(@ModelAttribute("coupon") Coupon coupon,
                               @ModelAttribute("status") int status,
                               Model model){
        couponService.saveCoupon(coupon);
        if(status == 1){
            model.addAttribute("couponCreated" , "Coupon created Successfully!");

        }
        if (status == 2){
            model.addAttribute("couponCreated","Coupon edited successfully!");
        }

        model.addAttribute("coupons", couponService.findAll());
        return "coupons";
    }

    @GetMapping("/admin/coupons/delete/{id}")
    public String deleteCoupon(@PathVariable("id") int couponId, Model model) {
        couponService.deleteCouponById(couponId);
        model.addAttribute("couponDeleted", "Coupon was deleted successfully");
        model.addAttribute("coupons", couponService.findAll());
        return "coupons";
    }

    @GetMapping("/admin/coupons/edit/{id}")
    public String editCoupon(@PathVariable("id") int couponId, Model model) {

        Coupon coupon = couponService.getCouponById(couponId).get();
        model.addAttribute("coupon", coupon);
        model.addAttribute("edit", "edit");
        return "createCoupon";
    }




}
