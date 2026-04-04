package org.fooddelivery.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Calculates straight-line distance between named areas.
 * Coordinates are approximate grid units for the Dhaka metro area.
 */
public class DistanceCalculator {

    /** Max distance (in grid units) to be considered "nearby". */
    public static final int NEARBY_THRESHOLD = 5;

    private static final Map<String, int[]> LOCATIONS = new HashMap<>();

    static {
        // Central Dhaka
        LOCATIONS.put("Motijheel",   new int[]{ 0,  0});
        LOCATIONS.put("Dhanmondi",   new int[]{-3,  2});
        LOCATIONS.put("Gulshan",     new int[]{ 3,  4});
        LOCATIONS.put("Banani",      new int[]{ 2,  5});
        LOCATIONS.put("Baridhara",   new int[]{ 4,  6});
        LOCATIONS.put("Tejgaon",     new int[]{ 1,  3});
        LOCATIONS.put("Farmgate",    new int[]{-1,  3});
        LOCATIONS.put("Mirpur",      new int[]{-4,  6});
        LOCATIONS.put("Pallabi",     new int[]{-5,  7});
        LOCATIONS.put("Uttara",      new int[]{ 1,  9});
        LOCATIONS.put("Tongi",       new int[]{ 2, 12});
        LOCATIONS.put("Gazipur",     new int[]{ 3, 15});
        LOCATIONS.put("Narsingdi",   new int[]{ 8, 18});
        LOCATIONS.put("Narayanganj", new int[]{ 2, -5});
        LOCATIONS.put("Demra",       new int[]{ 5, -3});
        LOCATIONS.put("Rayer Bazar", new int[]{-4,  1});
        LOCATIONS.put("Mohammadpur", new int[]{-3,  3});
        LOCATIONS.put("Lalmatia",    new int[]{-2,  2});
        LOCATIONS.put("Badda",       new int[]{ 5,  3});
        LOCATIONS.put("Rampura",     new int[]{ 4,  2});
        LOCATIONS.put("Khilgaon",    new int[]{ 3,  0});
    }

    /**
     * Returns Euclidean distance (grid units) between two location names.
     * Unknown locations default to origin (0, 0).
     */
    public static int calculate(String loc1, String loc2) {
        int[] a = LOCATIONS.getOrDefault(normalise(loc1), new int[]{0, 0});
        int[] b = LOCATIONS.getOrDefault(normalise(loc2), new int[]{0, 0});
        return (int) Math.sqrt(Math.pow(a[0] - b[0], 2) + Math.pow(a[1] - b[1], 2));
    }

    /**
     * Returns true only if the two locations are within NEARBY_THRESHOLD.
     * This is the filter used by "Find Nearest Restaurants".
     */
    public static boolean isNearby(String userLocation, String restaurantLocation) {
        // Exact match always qualifies
        if (normalise(userLocation).equalsIgnoreCase(normalise(restaurantLocation))) return true;
        return calculate(userLocation, restaurantLocation) <= NEARBY_THRESHOLD;
    }

    /** Returns the registered location name that best matches the input (case-insensitive). */
    public static String normalise(String input) {
        if (input == null) return "";
        for (String key : LOCATIONS.keySet()) {
            if (key.equalsIgnoreCase(input.trim())) return key;
        }
        return input.trim(); // return as-is if not found
    }

    /** List all known area names (useful for hints in the UI). */
    public static Iterable<String> knownAreas() {
        return LOCATIONS.keySet();
    }
}
