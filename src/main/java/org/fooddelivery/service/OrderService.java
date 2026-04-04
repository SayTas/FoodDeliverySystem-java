package org.fooddelivery.service;


import org.fooddelivery.model.Order;


public class OrderService {

    public double applyCoupon(Order order, String code, CouponService couponService) {
        double total = order.calculateTotal();
        return couponService.applyCoupon(code, total);
    }

    public void trackOrder(Order order) {
        System.out.println("Order Status: " + order.getStatus());
    }
}