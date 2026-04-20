package org.fooddelivery.api;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import org.fooddelivery.model.Restaurant;
import org.fooddelivery.model.Review;

import java.util.List;

@WebService
public interface RestaurantWebService {

    @WebMethod
    List<Restaurant> getRestaurantsByArea(@WebParam(name = "area") String area);

    @WebMethod
    List<Restaurant> getNearestRestaurants(@WebParam(name = "location") String location);

    @WebMethod
    List<Restaurant> searchRestaurants(@WebParam(name = "keyword") String keyword);

    /**
     * Returns all reviews for the given restaurant name.
     * Each Review contains: restaurantName, reviewerName, rating (1–5),
     * comment, date, and a stars() string like "★★★★☆".
     */
    @WebMethod
    List<Review> getRestaurantReviews(@WebParam(name = "restaurantName") String restaurantName);

    /**
     * Returns the average star rating for the given restaurant
     * as a formatted string e.g. "4.3 / 5.0  (12 reviews)".
     * Returns "No reviews yet." if there are none.
     */
    @WebMethod
    String getAverageRating(@WebParam(name = "restaurantName") String restaurantName);
}