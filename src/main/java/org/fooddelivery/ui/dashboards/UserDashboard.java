package org.fooddelivery.ui.dashboards;

import org.fooddelivery.model.MenuItem;
import org.fooddelivery.model.Order;
import org.fooddelivery.model.Restaurant;
import org.fooddelivery.model.Review;
import org.fooddelivery.service.CouponService;
import org.fooddelivery.service.OrderService;
import org.fooddelivery.service.RestaurantService;
import org.fooddelivery.service.ReviewService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserDashboard {

    private final Scanner sc = new Scanner(System.in);

    private final RestaurantService rs;
    private final OrderService      os;
    private final CouponService     cs;
    private final ReviewService     rvs;
    private final String            username;

    private Order currentOrder = new Order();

    public UserDashboard(RestaurantService rs, OrderService os,
                         CouponService cs, ReviewService rvs, String username) {
        this.rs       = rs;
        this.os       = os;
        this.cs       = cs;
        this.rvs      = rvs;
        this.username = username;
    }

    public void show() {
        while (true) {
            System.out.println("\n========== USER MENU ==========");
            System.out.println("  1. View all restaurants");
            System.out.println("  2. Search restaurant by name");
            System.out.println("  3. Find nearest restaurants (by area)");
            System.out.println("  4. Browse menu & order food");
            System.out.println("  5. View current order");
            System.out.println("  6. Apply coupon & checkout");
            System.out.println("  7. Track order");
            System.out.println("  8. Write a review for a restaurant");
            System.out.println("  9. View reviews for a restaurant");
            System.out.println("  0. Back");
            System.out.print("Select: ");

            int ch = readInt();

            switch (ch) {
                case 1 -> listRestaurants(rs.getAll());
                case 2 -> searchRestaurants();
                case 3 -> findNearest();
                case 4 -> browseAndOrder();
                case 5 -> viewCurrentOrder();
                case 6 -> checkout();
                case 7 -> os.trackOrder(currentOrder);
                case 8 -> writeReview();
                case 9 -> viewReviews();
                case 0 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ── 1. List all restaurants ────────────────────────────
    private void listRestaurants(List<Restaurant> list) {
        if (list.isEmpty()) {
            System.out.println("  No restaurants found.");
            return;
        }
        System.out.println();
        for (int i = 0; i < list.size(); i++) {
            Restaurant r = list.get(i);
            double avg = rvs.getAverageRating(r.getName());
            String avgStr = avg > 0
                    ? String.format("%.1f★", avg)
                    : "No reviews";
            System.out.printf("  %d. %-25s [%s]  %d items  %s%n",
                    i + 1, r.getName(), r.getLocation(), r.getMenu().size(), avgStr);
        }
    }

    // ── 2. Search ──────────────────────────────────────────
    private void searchRestaurants() {
        System.out.print("Keyword: ");
        String k = sc.nextLine().trim();
        List<Restaurant> results = rs.searchByName(k);
        if (results.isEmpty()) System.out.println("  No restaurants match \"" + k + "\".");
        else listRestaurants(results);
    }

    // ── 3. Find nearest ────────────────────────────────────
    private void findNearest() {
        System.out.print("Your area (e.g. Dhanmondi, Gulshan, Mirpur): ");
        String loc = sc.nextLine().trim();
        List<Restaurant> nearby = rs.getNearest(loc);
        if (!nearby.isEmpty()) {
            System.out.println("\n  Restaurants near " + loc + ":");
            listRestaurants(nearby);
        }
    }

    // ── 4. Browse menu & place order ───────────────────────
    private void browseAndOrder() {
        List<Restaurant> all = rs.getAll();
        if (all.isEmpty()) {
            System.out.println("  No restaurants available.");
            return;
        }
        System.out.println("\n  --- Choose a Restaurant ---");
        listRestaurants(all);
        System.out.print("Enter number (0 to cancel): ");
        int rIdx = readInt() - 1;
        if (rIdx < 0 || rIdx >= all.size()) return;

        Restaurant restaurant = all.get(rIdx);
        List<MenuItem> menu = restaurant.getMenu();

        if (menu.isEmpty()) {
            System.out.println("  This restaurant has no menu items yet.");
            return;
        }

        boolean ordering = true;
        while (ordering) {
            System.out.println("\n  ══ " + restaurant.getName()
                    + " — " + restaurant.getLocation() + " ══");
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.get(i);
                System.out.printf("  %d. %s%n", i + 1, item);
                if (item.hasAddOns()) {
                    System.out.print("     Add-ons available: ");
                    System.out.println(String.join(", ", item.getAddOns()));
                }
            }
            System.out.println("  0. Done adding items");
            System.out.print("Pick item number: ");
            int itemIdx = readInt() - 1;
            if (itemIdx < 0 || itemIdx >= menu.size()) {
                ordering = false;
                break;
            }

            MenuItem chosen = menu.get(itemIdx);
            List<String> selectedAddOns = new ArrayList<>();
            double addOnTotal = 0;

            if (chosen.hasAddOns()) {
                System.out.println("\n  --- Add-ons for " + chosen.getName() + " ---");
                List<String> addOns = chosen.getAddOns();
                for (int i = 0; i < addOns.size(); i++) {
                    System.out.printf("  %d. %s%n", i + 1, addOns.get(i));
                }
                System.out.println("  Enter add-on numbers separated by commas (e.g. 1,3)"
                        + " or press Enter to skip:");
                String input = sc.nextLine().trim();
                if (!input.isEmpty()) {
                    for (String part : input.split(",")) {
                        try {
                            int aIdx = Integer.parseInt(part.trim()) - 1;
                            if (aIdx >= 0 && aIdx < addOns.size()) {
                                String addOn = addOns.get(aIdx);
                                selectedAddOns.add(addOn);
                                addOnTotal += parseAddOnPrice(addOn);
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }

            double itemTotal = chosen.getPrice() + addOnTotal;
            String itemLabel = selectedAddOns.isEmpty()
                    ? chosen.getName()
                    : chosen.getName() + " (" + String.join(", ", selectedAddOns) + ")";

            currentOrder.addItem(new MenuItem(itemLabel, itemTotal, 1));
            System.out.printf("  ✔ Added: %s  ৳%.0f%n", itemLabel, itemTotal);
        }

        if (!currentOrder.getItems().isEmpty()) {
            System.out.printf("%n  Cart total so far: ৳%.0f%n",
                    currentOrder.calculateTotal());
            System.out.println("  Go to option 6 to apply a coupon and checkout.");
        }
    }

    // ── 5. View current order ──────────────────────────────
    private void viewCurrentOrder() {
        List<MenuItem> items = currentOrder.getItems();
        if (items.isEmpty()) {
            System.out.println("  Your cart is empty.");
            return;
        }
        System.out.println("\n  ── Your Cart ──");
        for (MenuItem i : items) {
            System.out.printf("    %-35s  ৳%.0f%n", i.getName(), i.getPrice());
        }
        System.out.printf("  ───────────────────────────────────────%n");
        System.out.printf("  Total:  ৳%.0f%n", currentOrder.calculateTotal());
    }

    // ── 6. Checkout ────────────────────────────────────────
    private void checkout() {
        if (currentOrder.getItems().isEmpty()) {
            System.out.println("  Cart is empty. Add items first (option 4).");
            return;
        }
        viewCurrentOrder();

        System.out.print("\n  Coupon code (press Enter to skip): ");
        String code = sc.nextLine().trim();
        double total = currentOrder.calculateTotal();

        if (!code.isEmpty()) {
            total = os.applyCoupon(currentOrder, code, cs);
        }

        System.out.printf("  ══ Final Total: ৳%.0f ══%n", total);
        System.out.print("  Confirm order? (y/n): ");
        String confirm = sc.nextLine().trim();
        if ("y".equalsIgnoreCase(confirm)) {
            currentOrder.updateStatus("Confirmed");
            System.out.println("  ✔ Order placed! Status: " + currentOrder.getStatus());
            currentOrder = new Order();
        } else {
            System.out.println("  Order cancelled.");
        }
    }

    // ── 8. Write a review ──────────────────────────────────
    private void writeReview() {
        List<Restaurant> all = rs.getAll();
        if (all.isEmpty()) {
            System.out.println("  No restaurants available to review.");
            return;
        }

        System.out.println("\n  --- Choose a Restaurant to Review ---");
        listRestaurants(all);
        System.out.print("Enter number (0 to cancel): ");
        int rIdx = readInt() - 1;
        if (rIdx < 0 || rIdx >= all.size()) return;

        String restaurantName = all.get(rIdx).getName();

        // Rating
        int rating = 0;
        while (rating < 1 || rating > 5) {
            System.out.print("  Rating (1–5 stars): ");
            rating = readInt();
            if (rating < 1 || rating > 5) {
                System.out.println("  Please enter a number between 1 and 5.");
            }
        }

        // Comment
        System.out.print("  Comment (press Enter to leave blank): ");
        String comment = sc.nextLine().trim();
        if (comment.isEmpty()) comment = "No comment.";

        Review review = new Review(
                restaurantName,
                username,
                rating,
                comment,
                LocalDate.now().toString()
        );

        rvs.addReview(review);
        System.out.printf("  ✔ Review saved! You gave %s: %s%n",
                restaurantName, review.stars());
    }

    // ── 9. View reviews for a restaurant ──────────────────
    private void viewReviews() {
        List<Restaurant> all = rs.getAll();
        if (all.isEmpty()) {
            System.out.println("  No restaurants available.");
            return;
        }

        System.out.println("\n  --- Choose a Restaurant ---");
        listRestaurants(all);
        System.out.print("Enter number (0 to cancel): ");
        int rIdx = readInt() - 1;
        if (rIdx < 0 || rIdx >= all.size()) return;

        String restaurantName = all.get(rIdx).getName();
        List<Review> reviews = rvs.getByRestaurant(restaurantName);

        System.out.println("\n  ══ Reviews for " + restaurantName + " ══");

        if (reviews.isEmpty()) {
            System.out.println("  No reviews yet. Be the first to review!");
            return;
        }

        double avg = rvs.getAverageRating(restaurantName);
        System.out.printf("  Average Rating: %.1f / 5.0  (%d review%s)%n%n",
                avg, reviews.size(), reviews.size() == 1 ? "" : "s");

        for (Review rv : reviews) {
            System.out.println("  " + rv);
        }
    }

    // ── Helpers ────────────────────────────────────────────

    private double parseAddOnPrice(String addOnLabel) {
        int plusIdx = addOnLabel.lastIndexOf('+');
        if (plusIdx >= 0) {
            try {
                return Double.parseDouble(addOnLabel.substring(plusIdx + 1).trim());
            } catch (NumberFormatException ignored) {}
        }
        return 0;
    }

    private int readInt() {
        try {
            int v = sc.nextInt();
            sc.nextLine();
            return v;
        } catch (Exception e) {
            sc.nextLine();
            return -1;
        }
    }
}