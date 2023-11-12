package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Coupon;
import com.ashish.MyGenuineProtein.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CouponServiceImpl implements CouponService{
    @Autowired
    CouponRepository couponRepository;
    @Override
    public List<Coupon> findAll() {
        return  couponRepository.findAll();
    }

    @Override
    public void saveCoupon(Coupon coupon) {
        couponRepository.save(coupon);
    }

    @Override
    public void deleteCouponById(int couponId) {
        couponRepository.deleteById(couponId);
    }

    @Override
    public Optional<Coupon> getCouponById(int couponId) {
        return couponRepository.findById(couponId);
    }

    @Override
    public Optional<Coupon> getCouponByCouponCode(String couponCode) {
        return couponRepository.findByCouponCode(couponCode);
    }
}
