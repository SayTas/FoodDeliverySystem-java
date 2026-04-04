package org.fooddelivery.ui.dashboards;

import org.fooddelivery.model.MenuItem;
import org.fooddelivery.model.Restaurant;
import org.fooddelivery.service.RestaurantService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class RestaurantDashboard {

    private final Scanner sc = new Scanner(System.in);
    private final RestaurantService service;

    public RestaurantDashboard(RestaurantService service) {
        this.service = service;
    }

    public void show() {
        while (true) {
            System.out.println("\n========== RESTAURANT MENU ==========");
            System.out.println("  1. Add menu item (with add-ons)");
            System.out.println("  2. View my menu");
            System.out.println("  0. Back");
            System.out.print("Select: ");

            int ch = readInt();

            switch (ch) {
                case 1 -> addMenuItem();
                case 2 -> viewMenu();
                case 0 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void addMenuItem() {
        System.out.print("Restaurant name: ");
        String rname = sc.nextLine().trim();

        // Verify restaurant exists
        Optional<Restaurant> found = service.findByName(rname);
        if (found.isEmpty()) {
            System.out.println("  Restaurant \"" + rname + "\" not found.");
            System.out.println("  Ask an admin to create the restaurant first.");
            return;
        }

        System.out.print("Item name: ");
        String item = sc.nextLine().trim();

        System.out.print("Price (৳): ");
        double price = readDouble();

        System.out.print("Stock quantity: ");
        int qty = readInt();

        // Add-ons
        List<String> addOns = new ArrayList<>();
        System.out.println("  Add-ons: enter each one as \"Label +price\" (e.g. Extra Cheese +20)");
        System.out.println("  Press Enter on a blank line when done.");
        while (true) {
            System.out.print("  Add-on (or Enter to finish): ");
            String ao = sc.nextLine().trim();
            if (ao.isEmpty()) break;
            addOns.add(ao);
            System.out.println("    ✔ Added: " + ao);
        }

        service.addMenuToRestaurant(rname, item, price, qty, addOns);
        System.out.printf("  ✔ \"%s\" added to %s's menu.%n", item, rname);
    }

    private void viewMenu() {
        System.out.print("Restaurant name: ");
        String rname = sc.nextLine().trim();

        Optional<Restaurant> found = service.findByName(rname);
        if (found.isEmpty()) {
            System.out.println("  Restaurant not found.");
            return;
        }

        List<MenuItem> menu = found.get().getMenu();
        if (menu.isEmpty()) {
            System.out.println("  Menu is empty.");
            return;
        }

        System.out.println("\n  ══ " + rname + " — Menu ══");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem m = menu.get(i);
            System.out.printf("  %d. %s  (stock: %d)%n", i + 1, m, m.getQuantity());
            if (m.hasAddOns()) {
                for (String ao : m.getAddOns()) {
                    System.out.println("       + " + ao);
                }
            }
        }
    }

    // ── Helpers ────────────────────────────────────────────
    private int readInt() {
        try { int v = sc.nextInt(); sc.nextLine(); return v; }
        catch (Exception e) { sc.nextLine(); return -1; }
    }

    private double readDouble() {
        try { double v = sc.nextDouble(); sc.nextLine(); return v; }
        catch (Exception e) { sc.nextLine(); return 0; }
    }
}
