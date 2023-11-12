package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon , Integer> {
    Optional<Coupon> findByCouponCode(String couponCode);
}
