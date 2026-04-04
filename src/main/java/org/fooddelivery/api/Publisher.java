package org.fooddelivery.api;

import jakarta.xml.ws.Endpoint;

public class Publisher {
    public static void main(String[] args) {
        Endpoint.publish(
                "http://localhost:8080/ws/restaurants",
                new RestaurantWebServiceImpl()
        );

        System.out.println("SOAP Service running...");
        System.out.println("WSDL: http://localhost:8080/ws/restaurants?wsdl");
    }
}