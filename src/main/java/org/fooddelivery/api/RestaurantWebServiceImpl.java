package org.fooddelivery.api;

import jakarta.jws.WebService;
import org.fooddelivery.model.Restaurant;
import org.fooddelivery.model.Review;
import org.fooddelivery.service.RestaurantService;
import org.fooddelivery.service.ReviewService;

import java.util.List;

@WebService(endpointInterface = "org.fooddelivery.api.RestaurantWebService")
public class RestaurantWebServiceImpl implements RestaurantWebService {

    private final RestaurantService service       = new RestaurantService();
    private final ReviewService     reviewService = new ReviewService();

    @Override
    public List<Restaurant> getRestaurantsByArea(String area) {
        return service.getByLocation(area);
    }

    @Override
    public List<Restaurant> getNearestRestaurants(String location) {
        return service.getNearest(location);
    }

    @Override
    public List<Restaurant> searchRestaurants(String keyword) {
        return service.searchByName(keyword);
    }

    @Override
    public List<Review> getRestaurantReviews(String restaurantName) {
        return reviewService.getByRestaurant(restaurantName);
    }

    @Override
    public String getAverageRating(String restaurantName) {
        List<Review> reviews = reviewService.getByRestaurant(restaurantName);
        if (reviews.isEmpty()) return "No reviews yet.";
        double avg = reviewService.getAverageRating(restaurantName);
        return String.format("%.1f / 5.0  (%d review%s)",
                avg, reviews.size(), reviews.size() == 1 ? "" : "s");
    }
}