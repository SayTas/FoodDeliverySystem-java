package org.fooddelivery.service;

import org.fooddelivery.model.Review;
import org.fooddelivery.storage.XMLStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewService {

    private static final String FILE = "data/reviews.xml";
    private List<Review> reviews;

    @SuppressWarnings("unchecked")
    public ReviewService() {
        Object obj = XMLStorage.load(FILE);
        if (obj instanceof List) {
            reviews = (List<Review>) obj;
        } else {
            reviews = new ArrayList<>();
        }
    }

    private void save() {
        XMLStorage.save(reviews, FILE);
    }

    // ── Add a review ───────────────────────────────────────
    public void addReview(Review review) {
        reviews.add(review);
        save();
    }

    // ── Get all reviews for a restaurant ──────────────────
    public List<Review> getByRestaurant(String restaurantName) {
        return reviews.stream()
                .filter(r -> r.getRestaurantName().equalsIgnoreCase(restaurantName))
                .collect(Collectors.toList());
    }

    // ── Average rating for a restaurant ───────────────────
    public double getAverageRating(String restaurantName) {
        List<Review> list = getByRestaurant(restaurantName);
        if (list.isEmpty()) return 0.0;
        return list.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    // ── All reviews (for web service) ─────────────────────
    public List<Review> getAll() {
        return reviews;
    }
}