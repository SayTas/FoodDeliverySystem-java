# 🍔 FoodDash — Food Delivery System

A role-based Food Delivery Application built with **Java 17**, **JavaFX 21**, and a **SOAP Web Service**, developed as part of the **SWE 4302 — Object Oriented Concepts Lab** project.

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
- [Known Notes](#known-notes)
- [Author](#author)

---

## Overview

FoodDash is a role-based food delivery system where users can browse restaurants, view menus, add items to a cart with custom add-ons, apply discount coupons, place orders, and rate restaurants — all through a modern **JavaFX dark-themed GUI**. All data is persisted locally using XML file storage. A **SOAP web service** exposes restaurant and review data over HTTP.

The project was initially console-based and evolved through three major phases:

1. **Menu item seeding fix** — initial restaurant data was loaded from a stale XML file that had no menu items; the service was updated to detect and re-seed stale data automatically.
2. **Review system** — users can write star ratings and comments for restaurants; reviews are persisted and also exposed through the SOAP web service.
3. **JavaFX GUI** — a full dark-themed graphical interface replaced the console, with role-specific dashboards for all four user types.

---

## Features

### 👤 User
- Register and login through a graphical login screen
- View all restaurants with live average star ratings
- Search restaurants by name or filter by area
- Browse a restaurant's full menu with prices and add-on options
- Add items to cart via a dialog with interactive add-on checkboxes
- Remove items from the cart individually
- Apply discount coupons at checkout
- Confirm and place orders
- Write star reviews (1–5 stars) with a comment for any restaurant
- View all reviews for any restaurant, including average rating

### 🏪 Restaurant Owner
- Select their restaurant from a dropdown
- Add new menu items with price, stock quantity, and comma-separated add-ons
- View their full current menu with stock and add-on count

### 🚴 Rider
- Toggle online / offline delivery status
- View today's delivery summary panel

### 🛠️ Admin
- View live stats: total restaurants, menu items, and reviews
- Add new restaurants with name and location
- View all restaurants with their ratings, item count, and location

---

## Project Structure

```
FoodDeliverySystem/
├── data/
│   ├── restaurants.xml         # Persisted restaurant + menu data
│   ├── users.xml               # Persisted user accounts
│   └── reviews.xml             # Persisted restaurant reviews  ← new
│
└── src/main/java/org/fooddelivery/
    ├── Main.java                # Entry point — launches JavaFX
    │
    ├── api/
    │   ├── Publisher.java               # Starts the SOAP endpoint
    │   ├── RestaurantWebService.java    # Web service interface  ← updated
    │   └── RestaurantWebServiceImpl.java# Web service implementation  ← updated
    │
    ├── model/
    │   ├── AuthUser.java        # User account (username, password, role)
    │   ├── Coupon.java          # Discount coupon (code, percent, min order)
    │   ├── MenuItem.java        # Menu item (name, price, quantity, add-ons)
    │   ├── Order.java           # Order (items list, status, total)
    │   ├── Restaurant.java      # Restaurant (name, location, menu)
    │   └── Review.java          # Star review (restaurant, reviewer, rating, comment, date)  ← new
    │
    ├── service/
    │   ├── AuthService.java     # Register and login logic
    │   ├── CouponService.java   # Coupon storage and validation
    │   ├── OrderService.java    # Coupon application and order tracking
    │   ├── PaymentService.java  # Payment processing (stub)
    │   ├── RestaurantService.java # Restaurant CRUD, search, nearest filter  ← updated
    │   ├── ReviewService.java   # Review persistence and rating averages  ← new
    │   └── UserService.java     # User helpers
    │
    ├── storage/
    │   └── XMLStorage.java      # Generic XML file read/write (XMLEncoder/XMLDecoder)
    │
    ├── ui/
    │   ├── ConsoleUI.java       # Legacy console interface (retained, unused at runtime)
    │   │
    │   ├── dashboards/          # Legacy console dashboards (retained, unused at runtime)
    │   │   ├── AdminDashboard.java
    │   │   ├── RestaurantDashboard.java
    │   │   ├── RiderDashboard.java
    │   │   └── UserDashboard.java  ← updated (ReviewService, username integration)
    │   │
    │   └── gui/                 # JavaFX graphical interface  ← all new
    │       ├── FoodDeliveryApp.java     # Application class, shared services, navigation
    │       ├── Styles.java              # Dark theme palette, CSS strings, UI factories
    │       ├── LoginScene.java          # Split-pane login + register with tab toggle
    │       ├── UserScene.java           # User dashboard: restaurants, cart, reviews
    │       ├── RestaurantScene.java     # Restaurant owner: manage menu
    │       ├── AdminScene.java          # Admin: stats, add restaurants
    │       └── RiderScene.java          # Rider: status toggle + delivery summary
    │
    └── util/
        └── DistanceCalculator.java  # Grid-based distance between Dhaka areas
```

---

## Technologies Used

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Core language |
| JavaFX | 21 | Graphical user interface |
| Maven | 3.6+ | Build and dependency management |
| `java.beans.XMLEncoder/XMLDecoder` | JDK built-in | XML-based file persistence |
| Jakarta XML Web Services (JAX-WS) | 4.0.1 | SOAP web service interface |
| `jaxws-rt` | 3.0.2 | JAX-WS runtime implementation |
| `javafx-maven-plugin` | 0.0.8 | Run JavaFX from Maven cleanly |

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
cd FoodDeliverySystem
```

**2. Open in IntelliJ IDEA**

- `File → Open` → select the `FoodDeliverySystem` folder (the one containing `pom.xml`)
- IntelliJ will detect the Maven project and prompt — click **Trust Project**
- Wait for Maven to download all dependencies (JavaFX + JAX-WS)

**3. Run the GUI application**

Using Maven (recommended — handles the JavaFX module path automatically):
```bash
mvn javafx:run
```

Or from IntelliJ:
- Open `src/main/java/org/fooddelivery/Main.java`
- Click the green ▶ button next to `public static void main`

> ⚠️ Do **not** use `mvn exec:java` for JavaFX — use `mvn javafx:run` only.

**4. Run the SOAP web service (optional, separate process)**

- Open `src/main/java/org/fooddelivery/api/Publisher.java`
- Click the green ▶ button to run it
- The service will start at `http://localhost:8080/ws/restaurants`

---

## How to Use

### Login / Register

The app opens on a split-screen login page. Use the **Login / Register** tab toggle on the right panel.

- **Login** — enter your username and password, press Login
- **Register** — choose a username, password, and role (`USER`, `RESTAURANT`, `RIDER`, `ADMIN`)

After login you are routed to the correct dashboard for your role.

---

### User Dashboard

#### Browse Restaurants
- Use the **Search** bar to filter by name, or the **Area** field to find nearby restaurants
- Each restaurant card shows its location, menu item count, and live average star rating
- Click **View Menu →** on any card to open that restaurant's menu

#### Adding Items to Cart
- Inside a menu view, click **+ Add to Cart** on any item
- A dialog opens showing available add-ons as checkboxes — tick any you want
- Click **Add to Cart ✔** to confirm; the cart badge in the sidebar updates instantly

#### Checkout
- Navigate to **🛒 My Cart** in the sidebar
- Review your items; click **✕** on any row to remove it
- Enter a coupon code if you have one (e.g. `SAVE10` or `FLAT50`)
- Click **✔ Confirm Order** — a confirmation dialog shows your final total

#### Writing a Review
- Navigate to **⭐ Reviews** in the sidebar
- Under **Write a Review**: select a restaurant, click stars to set your rating (1–5), type a comment, and click **Submit Review**

#### Viewing Reviews
- Under **View Reviews**: select a restaurant and click **Load Reviews**
- The panel displays the average rating and a card for each individual review

---

### Restaurant Dashboard

- Select your restaurant from the dropdown and click **Load Menu**
- Fill in the **Add Menu Item** form: name, price (৳), stock quantity, and optional add-ons (comma-separated, e.g. `Extra Cheese +20, Jalapeño +15`)
- Click **Add Item** — the menu list below refreshes automatically

---

### Admin Dashboard

- **Stats row** at the top shows live counts: total restaurants, total menu items, total reviews
- Use the **Add New Restaurant** card to register a new restaurant with a name and location
- The restaurant list below shows all restaurants with their ratings, item counts, and locations

---

### Available Coupons

| Code | Discount | Minimum Order |
|---|---|---|
| `SAVE10` | 10% off | ৳100 |
| `FLAT50` | ৳50 flat off | ৳200 |

---

## Web Service API

Start the SOAP web service by running `Publisher.java` as a separate Java process.

**Endpoint:** `http://localhost:8080/ws/restaurants`  
**WSDL:** `http://localhost:8080/ws/restaurants?wsdl`

Test using any SOAP client such as SoapUI or Postman (SOAP mode).

### Exposed Operations

| Operation | Parameter | Returns |
|---|---|---|
| `getRestaurantsByArea` | `area: String` | List of restaurants in that exact area |
| `getNearestRestaurants` | `location: String` | Restaurants near that Dhaka location |
| `searchRestaurants` | `keyword: String` | Restaurants whose name matches the keyword |
| `getRestaurantReviews` | `restaurantName: String` | Full list of `Review` objects for that restaurant |
| `getAverageRating` | `restaurantName: String` | Formatted string e.g. `"4.3 / 5.0  (12 reviews)"` |

### Example SOAP Request — `getRestaurantReviews`

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:api="http://api.fooddelivery.org/">
  <soapenv:Body>
    <api:getRestaurantReviews>
      <restaurantName>KFC</restaurantName>
    </api:getRestaurantReviews>
  </soapenv:Body>
</soapenv:Envelope>
```

---

## Sample Data

Seven restaurants are seeded automatically on first launch (or when stale data is detected):

| Restaurant | Area | Signature Items |
|---|---|---|
| KFC | Mirpur | Zinger Burger, Crispy Chicken, Hot Wings |
| Travel East | Banani | Butter Chicken, Lamb Biryani, Paneer Tikka |
| Pizza Hut | Gulshan | Margherita Pizza, BBQ Chicken Pizza, Garlic Bread |
| Burger King | Dhanmondi | Whopper, Chicken Royale, Onion Rings |
| Segreto | Uttara | Spaghetti Carbonara, Grilled Salmon, Tiramisu |
| Hello Pizza | Gazipur | Classic Pepperoni, Veggie Supreme, Chicken BBQ Pizza |
| North End | Gulshan | (manageable via Restaurant Dashboard) |

The nearest-restaurant filter covers 20 Dhaka metro areas including Gulshan, Banani, Dhanmondi, Mirpur, Uttara, Gazipur, Tejgaon, Badda, Rampura, Motijheel and more.

---

## OOP Design

| Principle | Where Applied |
|---|---|
| **Encapsulation** | All model fields are private with getters/setters; `XMLEncoder` relies on this |
| **Abstraction** | `RestaurantWebService` is an interface; `RestaurantWebServiceImpl` provides the concrete logic |
| **Single Responsibility** | Each service class owns exactly one domain — `ReviewService` only handles reviews, `AuthService` only handles authentication, etc. |
| **Layered Architecture** | GUI → Service → Storage; no scene ever touches `XMLStorage` directly |
| **Separation of Concerns** | `Styles.java` owns all visual tokens; scenes own only layout and event logic |
| **Open/Closed** | New web service operations (`getRestaurantReviews`, `getAverageRating`) were added by extending the interface, not modifying existing methods |

---

## Known Notes

- The **console interface** (`ConsoleUI` and all `dashboards/`) is fully retained in the codebase and functional. To switch back to console mode, revert `Main.java` to instantiate `ConsoleUI` instead of launching `FoodDeliveryApp`.
- The `@XmlRootElement` annotations on `Restaurant` and `MenuItem` are JAXB annotations for the SOAP layer. They do not affect `XMLEncoder`/`XMLDecoder` which works purely on JavaBean conventions (getters/setters).
- `data/reviews.xml` is created automatically on first review submission — no manual setup needed.
- The **Rider dashboard** and **payment flow** are scaffolded for future expansion; order assignment to riders is not yet implemented.

---

## Author

**Sayma Tasnim**  
Department: Computer Science & Engineering  
Program: Software Engineering  
Course: SWE 4302 — Object Oriented Concepts Lab  
Islamic University of Technology, OIC