package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon , Integer> {
    Optional<Coupon> findByCouponCode(String couponCode);
}
