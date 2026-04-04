package org.fooddelivery.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Restaurant implements Serializable {

    private String name;
    private String location;
    private List<MenuItem> menu = new ArrayList<>();

    public Restaurant() {}

    public Restaurant(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() { return name; }
    public String getLocation() { return location; }
    public List<MenuItem> getMenu() { return menu; }

    public void setName(String name) { this.name = name; }
    public void setLocation(String location)   { this.location = location; }
    public void setMenu(List<MenuItem> menu)   { this.menu = menu != null ? menu : new ArrayList<>(); }

    public void addMenuItem(MenuItem item) {
        menu.add(item);
    }
}