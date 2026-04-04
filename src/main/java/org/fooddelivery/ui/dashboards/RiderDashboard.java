package org.fooddelivery.ui.dashboards;

import org.fooddelivery.model.Order;
import org.fooddelivery.service.OrderService;

import java.util.Scanner;

public class RiderDashboard {

    private Scanner sc = new Scanner(System.in);
    private OrderService service;

    private Order order = new Order();

    public RiderDashboard(OrderService service) {
        this.service = service;
    }

    public void show() {
        while (true) {
            System.out.println("\n--- RIDER ---");
            System.out.println("1. Update Delivery Status");
            System.out.println("0. Back");

            int ch = sc.nextInt(); sc.nextLine();

            switch (ch) {
                case 1 -> {
                    System.out.print("Status: ");
                    String s = sc.nextLine();
                    order.updateStatus(s);
                    service.trackOrder(order);
                }

                case 0 -> { return; }
            }
        }
    }
}