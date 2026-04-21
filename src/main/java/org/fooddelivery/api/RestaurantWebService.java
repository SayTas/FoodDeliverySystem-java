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


    @WebMethod
    List<Review> getRestaurantReviews(@WebParam(name = "restaurantName") String restaurantName);

    @WebMethod
    String getAverageRating(@WebParam(name = "restaurantName") String restaurantName);
}