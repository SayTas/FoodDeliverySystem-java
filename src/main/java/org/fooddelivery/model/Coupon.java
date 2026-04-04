package org.fooddelivery.model;

import java.io.Serializable;

public class Coupon implements Serializable {

    private String code;
    private double discountPercent;
    private double minAmount;

    public Coupon() {}

    public Coupon(String code, double discountPercent, double minAmount) {
        this.code = code;
        this.discountPercent = discountPercent;
        this.minAmount = minAmount;
    }

    public String getCode() {
        return code;
    }

    public double applyDiscount(double amount) {
        if (amount >= minAmount) {
            return amount - (amount * discountPercent / 100);
        }
        return amount;
    }
}