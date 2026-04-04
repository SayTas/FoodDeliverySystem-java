package org.fooddelivery.api;


import jakarta.jws.WebService;
import org.fooddelivery.model.Restaurant;
import org.fooddelivery.service.RestaurantService;

import java.util.List;

@WebService(endpointInterface = "org.fooddelivery.api.RestaurantWebService")
public class RestaurantWebServiceImpl implements RestaurantWebService {

    private RestaurantService service = new RestaurantService();

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
}