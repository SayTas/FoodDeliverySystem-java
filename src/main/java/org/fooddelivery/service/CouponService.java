package org.fooddelivery.service;


import org.fooddelivery.model.Coupon;

import java.util.HashMap;
import java.util.Map;

public class CouponService {

    private Map<String, Coupon> coupons = new HashMap<>();

    public void addCoupon(Coupon coupon) {
        coupons.put(coupon.getCode(), coupon);
    }

    public double applyCoupon(String code, double amount) {
        Coupon coupon = coupons.get(code);

        if (coupon == null) {
            System.out.println("Invalid coupon!");
            return amount;
        }

        return coupon.applyDiscount(amount);
    }
}