package org.fooddelivery.ui.dashboards;

import org.fooddelivery.model.Restaurant;
import org.fooddelivery.service.RestaurantService;

import java.util.Scanner;

public class AdminDashboard {

    private Scanner sc = new Scanner(System.in);
    private RestaurantService service;

    public AdminDashboard(RestaurantService service) {
        this.service = service;
    }

    public void show() {
        while (true) {
            System.out.println("\n--- ADMIN ---");
            System.out.println("1. View Restaurants");
            System.out.println("2. Add Restaurant");
            System.out.println("0. Back");

            int ch = sc.nextInt(); sc.nextLine();

            switch (ch) {
                case 1 -> service.getAll()
                        .forEach(r -> System.out.println(r.getName()));

                case 2 -> {
                    System.out.print("Name: ");
                    String name = sc.nextLine();
                    System.out.print("Location: ");
                    String loc = sc.nextLine();

                    service.addRestaurant(new Restaurant(name, loc));
                }

                case 0 -> { return; }
            }
        }
    }
}