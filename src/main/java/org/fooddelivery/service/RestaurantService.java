package org.fooddelivery.service;

import org.fooddelivery.model.MenuItem;
import org.fooddelivery.model.Restaurant;
import org.fooddelivery.storage.XMLStorage;
import org.fooddelivery.util.DistanceCalculator;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class RestaurantService {

    private static final String FILE = "data/restaurants.xml";
    private List<Restaurant> restaurants;

    public RestaurantService() {
        Object obj = XMLStorage.load(FILE);
        if (obj != null) {
            restaurants = (List<Restaurant>) obj;

            // If every restaurant has an empty menu, the data file is stale
            // (was saved before menu items were added to seedSampleData).
            // Delete it and re-seed from scratch.
            if (restaurants.stream().allMatch(r -> r.getMenu().isEmpty())) {
                new File(FILE).delete();
                restaurants.clear();
                seedSampleData();
                save();
            }
        } else {
            restaurants = new ArrayList<>();
            seedSampleData();
            save();
        }
    }

    private void save() {
        XMLStorage.save(restaurants, FILE);
    }

    // ── ADD RESTAURANT ─────────────────────────────────────
    public void addRestaurant(Restaurant r) {
        restaurants.add(r);
        save();
    }

    // ── GET ALL ────────────────────────────────────────────
    public List<Restaurant> getAll() {
        return restaurants;
    }

    // ── FILTER BY EXACT LOCATION ───────────────────────────
    public List<Restaurant> getByLocation(String location) {
        return restaurants.stream()
                .filter(r -> r.getLocation().equalsIgnoreCase(location))
                .collect(Collectors.toList());
    }

    // ── SEARCH BY NAME ─────────────────────────────────────
    public List<Restaurant> searchByName(String keyword) {
        return restaurants.stream()
                .filter(r -> r.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Restaurant> getNearest(String userLocation) {
        List<Restaurant> nearby = restaurants.stream()
                .filter(r -> DistanceCalculator.isNearby(userLocation, r.getLocation()))
                .sorted(Comparator.comparingInt(
                        r -> DistanceCalculator.calculate(userLocation, r.getLocation())))
                .collect(Collectors.toList());

        if (nearby.isEmpty()) {
            System.out.println("  No restaurants found near \"" + userLocation + "\".");
            System.out.println("  Tip — known areas: Dhanmondi, Gulshan, Banani, Mirpur,");
            System.out.println("        Uttara, Gazipur, Motijheel, Tejgaon, Badda ...");
        }
        return nearby;
    }

    // ── SORT BY NAME ───────────────────────────────────────
    public List<Restaurant> sortByName() {
        return restaurants.stream()
                .sorted(Comparator.comparing(Restaurant::getName))
                .collect(Collectors.toList());
    }

    // ── FIND RESTAURANT BY NAME ────────────────────────────
    public Optional<Restaurant> findByName(String name) {
        return restaurants.stream()
                .filter(r -> r.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    // ── ADD MENU ITEM TO RESTAURANT ────────────────────────
    public void addMenuToRestaurant(String restaurantName, String item,
                                    double price, int qty, List<String> addOns) {
        for (Restaurant r : restaurants) {
            if (r.getName().equalsIgnoreCase(restaurantName)) {
                r.addMenuItem(new MenuItem(item, price, qty, addOns));
            }
        }
        save();
    }

    // ── LEGACY OVERLOAD (keeps RestaurantDashboard compatible) ─
    public void addMenuToRestaurant(String restaurantName, String item,
                                    double price, int qty) {
        addMenuToRestaurant(restaurantName, item, price, qty, new ArrayList<>());
    }

    // ── SEED SAMPLE DATA ───────────────────────────────────
    private void seedSampleData() {
        // ── Mirpur ─────────────────────────────────────────
        Restaurant kfc = new Restaurant("KFC", "Mirpur");
        kfc.addMenuItem(new MenuItem("Zinger Burger",    199, 50, Arrays.asList("Extra Patty +60", "Cheese Slice +20", "Jalapeño +15")));
        kfc.addMenuItem(new MenuItem("Crispy Chicken",   249, 40, Arrays.asList("Extra Sauce +10", "Coleslaw +30")));
        kfc.addMenuItem(new MenuItem("Hot Wings (6pc)",  179, 60, Arrays.asList("BBQ Dip +15", "Garlic Dip +15")));
        kfc.addMenuItem(new MenuItem("Pepsi",             50, 100, new ArrayList<>()));
        restaurants.add(kfc);

        // ── Banani ─────────────────────────────────────────
        Restaurant travel = new Restaurant("Travel East", "Banani");
        travel.addMenuItem(new MenuItem("Butter Chicken",     320, 30, Arrays.asList("Extra Naan +30", "Raita +20")));
        travel.addMenuItem(new MenuItem("Lamb Biryani",       380, 25, Arrays.asList("Extra Meat +90", "Borhani +30")));
        travel.addMenuItem(new MenuItem("Paneer Tikka",       280, 35, Arrays.asList("Mint Chutney +15", "Extra Gravy +30")));
        travel.addMenuItem(new MenuItem("Mango Lassi",         80, 60, new ArrayList<>()));
        restaurants.add(travel);

        // ── Gulshan ────────────────────────────────────────
        Restaurant pizzaHut = new Restaurant("Pizza Hut", "Gulshan");
        pizzaHut.addMenuItem(new MenuItem("Margherita Pizza",  349, 30, Arrays.asList("Extra Cheese +60", "Olives +30", "Mushroom +30")));
        pizzaHut.addMenuItem(new MenuItem("BBQ Chicken Pizza", 449, 25, Arrays.asList("Extra Cheese +60", "Jalapeño +20")));
        pizzaHut.addMenuItem(new MenuItem("Garlic Bread",      129, 50, Arrays.asList("Cheese Dip +20")));
        pizzaHut.addMenuItem(new MenuItem("Pepsi",              50, 100, new ArrayList<>()));
        restaurants.add(pizzaHut);

        // ── Dhanmondi ──────────────────────────────────────
        Restaurant burgerKing = new Restaurant("Burger King", "Dhanmondi");
        burgerKing.addMenuItem(new MenuItem("Whopper",         299, 40, Arrays.asList("Double Patty +80", "Bacon +50", "Extra Cheese +25")));
        burgerKing.addMenuItem(new MenuItem("Chicken Royale",  249, 45, Arrays.asList("Extra Sauce +10", "Cheese +25")));
        burgerKing.addMenuItem(new MenuItem("Onion Rings",     119, 60, Arrays.asList("BBQ Dip +15")));
        burgerKing.addMenuItem(new MenuItem("Fanta",            50, 100, new ArrayList<>()));
        restaurants.add(burgerKing);

        // ── Uttara ─────────────────────────────────────────
        Restaurant segreto = new Restaurant("Segreto", "Uttara");
        segreto.addMenuItem(new MenuItem("Spaghetti Carbonara", 420, 20, Arrays.asList("Extra Bacon +60", "Truffle Oil +50")));
        segreto.addMenuItem(new MenuItem("Grilled Salmon",      550, 15, Arrays.asList("Lemon Butter +30", "Steamed Veg +40")));
        segreto.addMenuItem(new MenuItem("Tiramisu",            220, 25, new ArrayList<>()));
        segreto.addMenuItem(new MenuItem("Lemonade",             80, 50, Arrays.asList("Mint +10")));
        restaurants.add(segreto);

        // ── Gazipur ────────────────────────────────────────
        Restaurant helloPizza = new Restaurant("Hello Pizza", "Gazipur");
        helloPizza.addMenuItem(new MenuItem("Classic Pepperoni",  299, 40, Arrays.asList("Extra Cheese +50", "Chili Flakes +10")));
        helloPizza.addMenuItem(new MenuItem("Veggie Supreme",     279, 35, Arrays.asList("Extra Cheese +50", "Jalapeño +15")));
        helloPizza.addMenuItem(new MenuItem("Chicken BBQ Pizza",  349, 30, Arrays.asList("Extra Chicken +70", "BBQ Drizzle +20")));
        helloPizza.addMenuItem(new MenuItem("Garlic Knots",       120, 50, Arrays.asList("Marinara Dip +20")));
        restaurants.add(helloPizza);
    }
}