# 🍔 MyFoodDelivery

A console-based Food Delivery Application built with Java as part of the **SWE 4302 — Object Oriented Concepts Lab** project.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [How to Use](#how-to-use)
- [Web Service API](#web-service-api)
- [Sample Data](#sample-data)
- [OOP Design](#oop-design)

---

## Overview

MyFoodDelivery is a role-based food delivery system where users can browse restaurants, view menus, select items with add-ons, apply discount coupons, and place orders — all through a console interface. Restaurants are persisted using XML file storage and a SOAP-based web service exposes restaurant data over HTTP.

---

## Features

### User
- Register and login
- View all available restaurants
- Search restaurants by name or keyword
- Find nearest restaurants filtered by area (Dhaka metro)
- Browse a restaurant's full menu with prices
- Add items to cart and select add-ons per item
- View cart with running total
- Apply discount coupon at checkout
- Track order status

### Restaurant Owner
- Add menu items to their restaurant
- Attach add-on options to each menu item (e.g. Extra Cheese +20)
- View their full menu

### Rider
- Update delivery status of an active order

### Admin
- View all registered restaurants
- Add new restaurants to the system

---

## Project Structure

```
MyFoodDelivery/
├── data/
│   ├── restaurants.xml         # Persisted restaurant and menu data
│   └── users.xml               # Persisted user accounts
│
└── src/main/java/org/fooddelivery/
    ├── Main.java                # Application entry point
    │
    ├── api/
    │   ├── Publisher.java               # Starts the SOAP endpoint
    │   ├── RestaurantWebService.java    # Web service interface
    │   └── RestaurantWebServiceImpl.java# Web service implementation
    │
    ├── model/
    │   ├── AuthUser.java        # User account (username, password, role)
    │   ├── Coupon.java          # Discount coupon (code, percent, min order)
    │   ├── MenuItem.java        # Menu item (name, price, quantity, add-ons)
    │   ├── Order.java           # Order (items list, status, total)
    │   └── Restaurant.java      # Restaurant (name, location, menu)
    │
    ├── service/
    │   ├── AuthService.java     # Register and login logic
    │   ├── CouponService.java   # Coupon storage and validation
    │   ├── OrderService.java    # Coupon application and order tracking
    │   ├── PaymentService.java  # Payment processing (stub)
    │   ├── RestaurantService.java # Restaurant CRUD, search, nearest filter
    │   └── UserService.java     # User helpers
    │
    ├── storage/
    │   └── XMLStorage.java      # Generic XML file read/write using XMLEncoder
    │
    ├── ui/
    │   ├── ConsoleUI.java       # Main menu — login, register, role routing
    │   └── dashboards/
    │       ├── AdminDashboard.java
    │       ├── RestaurantDashboard.java
    │       ├── RiderDashboard.java
    │       └── UserDashboard.java
    │
    └── util/
        └── DistanceCalculator.java  # Grid-based distance between Dhaka areas
```

---

## Technologies Used

| Technology | Purpose |
|---|---|
| Java 17 | Core language |
| Maven | Build and dependency management |
| `java.beans.XMLEncoder/XMLDecoder` | XML-based file storage |
| Jakarta XML Web Services (JAX-WS) | SOAP web service |
| `jaxws-rt 3.0.2` | JAX-WS runtime |

---

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- IntelliJ IDEA (recommended) or any Java IDE

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/SayTas/FoodDeliverySystem-java.git
cd MyFoodDelivery
```

**2. Open in IntelliJ IDEA**

- `File → Open` → select the `MyFoodDelivery` folder (the one containing `pom.xml`)
- IntelliJ will detect the Maven project and prompt — click **Trust Project**
- Wait for Maven to download dependencies

**3. Run the application**

- Open `src/main/java/org/fooddelivery/Main.java`
- Click the green ▶ button next to `public static void main`

> **Note:** On first run, `data/restaurants.xml` is auto-populated with 6 sample restaurants. Make sure the `data/` folder exists in the project root.

---

## How to Use

### Register an account
```
===== FOOD DELIVERY SYSTEM =====
2. Register

Username: sayma
Password: 1234
Role: USER
```

Available roles: `USER`, `RESTAURANT`, `RIDER`, `ADMIN`

### Login and place an order
```
1. Login → username + password
→ Lands on USER MENU

4. Browse menu & order food
  → Pick a restaurant by number
  → Pick menu items by number
  → Select add-ons (e.g. enter: 1,2)
  → Type 0 when done adding items

6. Apply coupon & checkout
  → Coupon code: SAVE10
  → Confirm with: y
```

### Available coupon
| Code | Discount | Minimum Order |
|---|---|---|
| `SAVE10` | 10% off | ৳100 |

---

## Web Service API

A SOAP web service can be started separately by running `Publisher.java`.

```
Run → Publisher.java
```

**Endpoint:** `http://localhost:8080/ws/restaurants`  
**WSDL:** `http://localhost:8080/ws/restaurants?wsdl`

### Exposed operations

| Operation | Parameter | Returns |
|---|---|---|
| `getRestaurantsByArea` | `area: String` | Restaurants in that exact area |
| `getNearestRestaurants` | `location: String` | Restaurants near that location |
| `searchRestaurants` | `keyword: String` | Restaurants matching keyword |

---

## Sample Data

Six restaurants are seeded automatically on first launch:

| Restaurant | Area | Highlights |
|---|---|---|
| KFC | Mirpur | Zinger Burger, Hot Wings |
| Travel East | Banani | Butter Chicken, Lamb Biryani |
| Pizza Hut | Gulshan | Margherita, BBQ Chicken Pizza |
| Burger King | Dhanmondi | Whopper, Chicken Royale |
| Segreto | Uttara | Spaghetti Carbonara, Grilled Salmon |
| Hello Pizza | Gazipur | Classic Pepperoni, Chicken BBQ Pizza |

The nearest restaurant filter covers 20 Dhaka-area locations including Gulshan, Banani, Dhanmondi, Mirpur, Uttara, Gazipur, Tejgaon, Badda, Rampura, and more.

---

## OOP Design

| Principle | Where applied |
|---|---|
| **Encapsulation** | All model fields are private with getters/setters |
| **Abstraction** | `RestaurantWebService` is an interface implemented by `RestaurantWebServiceImpl` |
| **Single Responsibility** | One class per concern — each service handles exactly one domain |
| **Layered Architecture** | UI → Service → Storage; UI never touches storage directly |
| **Inheritance** | `MenuItem` extended by add-on aware constructor overloads |

---

## Author

**Sayma Tasnim**  <br>
Department: Computer Science & Engineering <br>
Program: Software Engineering <br>
Course: SWE 4302 - Object Oriented Concepts Lab <br>
Islamic University of Technology, OIC
