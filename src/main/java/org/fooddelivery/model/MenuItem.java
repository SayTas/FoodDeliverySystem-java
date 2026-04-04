package org.fooddelivery.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class MenuItem implements Serializable {

    private String name;
    private double price;
    private int quantity;
    private List<String> addOns = new ArrayList<>();  // e.g. ["Extra Cheese +20", "Spicy Sauce +10"]

    public MenuItem() {}

    public MenuItem(String name, double price, int quantity) {
        this.name     = name;
        this.price    = price;
        this.quantity = quantity;
    }

    public MenuItem(String name, double price, int quantity, List<String> addOns) {
        this.name     = name;
        this.price    = price;
        this.quantity = quantity;
        this.addOns   = addOns != null ? new ArrayList<>(addOns) : new ArrayList<>();
    }

    public String       getName()     { return name; }
    public double       getPrice()    { return price; }
    public int          getQuantity() { return quantity; }
    public List<String> getAddOns()   { return addOns; }

    public void setName(String name)         { this.name = name; }
    public void setPrice(double price)       { this.price = price; }
    public void setQuantity(int quantity)    { this.quantity = quantity; }
    public void setAddOns(List<String> list) { this.addOns = list!= null ? new ArrayList<>(list) : new ArrayList<>(); }

    public boolean hasAddOns() {
        return addOns != null && !addOns.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("%-22s  ৳%.0f", name, price);
    }
}
