package org.fooddelivery.ui;

import org.fooddelivery.model.AuthUser;
import org.fooddelivery.service.*;

import java.util.Scanner;

import org.fooddelivery.ui.dashboards.*;

public class ConsoleUI {

    private Scanner sc = new Scanner(System.in);

    private AuthService authService = new AuthService();
    private RestaurantService restaurantService = new RestaurantService();
    private OrderService orderService = new OrderService();
    private CouponService couponService = new CouponService();

    public void start() {

        preload();

        while (true) {
            System.out.println("\n===== FOOD DELIVERY SYSTEM =====");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("0. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> login();
                case 2 -> register();
                case 0 -> { return; }
            }
        }
    }

    private void register() {
        System.out.print("Username: ");
        String u = sc.nextLine();

        System.out.print("Password: ");
        String p = sc.nextLine();

        System.out.print("Role (ADMIN/USER/RESTAURANT/RIDER): ");
        String role = sc.nextLine().toUpperCase();

        authService.register(u, p, role);
    }

    private void login() {
        System.out.print("Username: ");
        String u = sc.nextLine();

        System.out.print("Password: ");
        String p = sc.nextLine();

        AuthUser user = authService.login(u, p);

        if (user == null) {
            System.out.println("Invalid credentials!");
            return;
        }

        System.out.println("Welcome " + user.getUsername());

        switch (user.getRole()) {
            case "ADMIN" ->
                    new AdminDashboard(restaurantService).show();

            case "USER" ->
                    new UserDashboard(restaurantService, orderService, couponService).show();

            case "RESTAURANT" ->
                    new RestaurantDashboard(restaurantService).show();

            case "RIDER" ->
                    new RiderDashboard(orderService).show();
        }
    }

    private void preload() {
        couponService.addCoupon(new org.fooddelivery.model.Coupon("SAVE10", 10, 100));
    }
}