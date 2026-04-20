package org.fooddelivery.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.fooddelivery.model.Restaurant;
import org.fooddelivery.model.Review;

import java.util.List;

public class AdminScene {

    private final String username;
    private VBox restaurantListBox;

    private AdminScene(String username) {
        this.username = username;
    }

    public static Scene create(String username) {
        return new AdminScene(username).build();
    }

    private Scene build() {
        BorderPane root = new BorderPane();
        root.setStyle(Styles.ROOT);

        HBox nav = Styles.navBar("Admin Dashboard", username);
        Button logout = Styles.secondaryButton("Logout");
        logout.setOnAction(e -> FoodDeliveryApp.navigate(LoginScene.create()));
        nav.getChildren().add(logout);

        ScrollPane scroll = new ScrollPane(buildContent());
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        root.setTop(nav);
        root.setCenter(scroll);

        return new Scene(root, 1060, 740);
    }

    private VBox buildContent() {
        VBox content = new VBox(28);
        content.setPadding(new Insets(28));

        Label title = Styles.h2("🛠️  Admin Dashboard");

        // ── Stats row ─────────────────────────────────────
        HBox stats = buildStatsRow();

        // ── Add restaurant form ───────────────────────────
        VBox addCard = buildAddRestaurantCard();

        // ── Restaurant list ───────────────────────────────
        Label listTitle = Styles.h3("📋  All Restaurants");
        restaurantListBox = new VBox(10);
        refreshList();

        content.getChildren().addAll(title, stats, addCard, listTitle, restaurantListBox);
        return content;
    }

    private HBox buildStatsRow() {
        List<Restaurant> all = FoodDeliveryApp.restaurantService.getAll();
        int items   = all.stream().mapToInt(r -> r.getMenu().size()).sum();
        int reviews = FoodDeliveryApp.reviewService.getAll().size();

        HBox row = new HBox(20);
        row.getChildren().addAll(
                statCard("🏪", "Restaurants",   String.valueOf(all.size())),
                statCard("🍽️", "Menu Items",     String.valueOf(items)),
                statCard("⭐", "Total Reviews",  String.valueOf(reviews))
        );
        return row;
    }

    private VBox statCard(String icon, String label, String value) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(22, 36, 22, 36));
        card.setStyle(Styles.CARD);

        Label iconLbl  = new Label(icon);
        iconLbl.setStyle("-fx-font-size: 30px;");

        Label valueLbl = new Label(value);
        valueLbl.setStyle("-fx-text-fill: " + Styles.ACCENT +
                "; -fx-font-size: 30px; -fx-font-weight: bold;");

        Label labelLbl = Styles.body(label);

        card.getChildren().addAll(iconLbl, valueLbl, labelLbl);
        return card;
    }

    private VBox buildAddRestaurantCard() {
        VBox card = new VBox(16);
        card.setPadding(new Insets(22, 24, 22, 24));
        card.setStyle(Styles.CARD);
        card.setMaxWidth(520);

        Label cardTitle = Styles.h3("➕  Add New Restaurant");

        TextField nameField = new TextField();
        nameField.setPromptText("Restaurant name");
        nameField.setStyle(Styles.INPUT);
        nameField.setMaxWidth(Double.MAX_VALUE);

        TextField locField = new TextField();
        locField.setPromptText("Location  (e.g. Gulshan, Mirpur)");
        locField.setStyle(Styles.INPUT);
        locField.setMaxWidth(Double.MAX_VALUE);

        Label msgLbl = new Label();
        msgLbl.setStyle("-fx-font-size: 13px;");
        msgLbl.setVisible(false);
        msgLbl.setManaged(false);

        Button addBtn = Styles.primaryButton("Add Restaurant");
        addBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String loc  = locField.getText().trim();
            if (name.isEmpty() || loc.isEmpty()) {
                msgLbl.setText("Please fill in both fields.");
                msgLbl.setStyle("-fx-text-fill: " + Styles.DANGER + "; -fx-font-size: 13px;");
            } else {
                FoodDeliveryApp.restaurantService
                        .addRestaurant(new Restaurant(name, loc));
                msgLbl.setText("✔ \"" + name + "\" added successfully!");
                msgLbl.setStyle("-fx-text-fill: " + Styles.SUCCESS + "; -fx-font-size: 13px;");
                nameField.clear();
                locField.clear();
                refreshList();
            }
            msgLbl.setVisible(true);
            msgLbl.setManaged(true);
        });

        card.getChildren().addAll(
                cardTitle,
                Styles.labeled("Name", nameField),
                Styles.labeled("Location", locField),
                msgLbl, addBtn
        );
        return card;
    }

    private void refreshList() {
        restaurantListBox.getChildren().clear();
        List<Restaurant> all = FoodDeliveryApp.restaurantService.getAll();
        if (all.isEmpty()) {
            restaurantListBox.getChildren().add(Styles.body("No restaurants yet."));
            return;
        }
        for (Restaurant r : all) {
            HBox row = new HBox(18);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(16, 22, 16, 22));
            row.setStyle(Styles.CARD);

            Label icon = new Label(Styles.restaurantEmoji(r.getName()));
            icon.setStyle("-fx-font-size: 26px;");

            VBox info = new VBox(4);
            Label name = Styles.h3(r.getName());

            double avg = FoodDeliveryApp.reviewService.getAverageRating(r.getName());
            List<Review> rvs = FoodDeliveryApp.reviewService.getByRestaurant(r.getName());
            String ratingTxt = avg > 0
                    ? String.format("%.1f  %s  (%d reviews)",
                    avg, Styles.starsFor(avg), rvs.size())
                    : "No reviews";
            Label details = Styles.body("📍 " + r.getLocation() +
                    "   •   " + r.getMenu().size() + " items   •   " + ratingTxt);

            info.getChildren().addAll(name, details);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label badge = new Label(r.getMenu().size() + " items");
            badge.setStyle("-fx-background-color: " + Styles.SURFACE2 + ";" +
                    "-fx-text-fill: " + Styles.MUTED + ";" +
                    "-fx-background-radius: 20; -fx-padding: 4 12;" +
                    "-fx-font-size: 12px;");

            row.getChildren().addAll(icon, info, spacer, badge);
            restaurantListBox.getChildren().add(row);
        }
    }
}