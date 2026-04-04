package org.fooddelivery.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private List<MenuItem> items = new ArrayList<>();
    private String status;

    public Order() {
        status = "Preparing";
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public List<MenuItem> getItems() { return items; }

    public String getStatus() { return status; }

    public void updateStatus(String status) {
        this.status = status;
    }

    public double calculateTotal() {
        double total = 0;
        for (MenuItem item : items) {
            total += item.getPrice();
        }
        return total;
    }
}
