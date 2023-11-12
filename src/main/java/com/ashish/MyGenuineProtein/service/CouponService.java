package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Coupon;

import java.util.List;
import java.util.Optional;

public interface CouponService {
    List<Coupon> findAll();

    void saveCoupon(Coupon coupon);

    void deleteCouponById(int couponId);


    Optional<Coupon> getCouponById(int couponId);

    Optional<Coupon> getCouponByCouponCode(String couponCode);
}
