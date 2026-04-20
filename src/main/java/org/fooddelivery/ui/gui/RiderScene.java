package org.fooddelivery.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class RiderScene {

    private final String username;

    private RiderScene(String username) {
        this.username = username;
    }

    public static Scene create(String username) {
        return new RiderScene(username).build();
    }

    private Scene build() {
        BorderPane root = new BorderPane();
        root.setStyle(Styles.ROOT);

        HBox nav = Styles.navBar("Rider Dashboard", username);
        Button logout = Styles.secondaryButton("Logout");
        logout.setOnAction(e -> FoodDeliveryApp.navigate(LoginScene.create()));
        nav.getChildren().add(logout);

        ScrollPane scroll = new ScrollPane(buildContent());
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        root.setTop(nav);
        root.setCenter(scroll);

        return new Scene(root, 960, 680);
    }

    private VBox buildContent() {
        VBox content = new VBox(24);
        content.setPadding(new Insets(32));

        Label title = Styles.h2("🚴  Rider Dashboard");
        Label sub   = Styles.body("Manage your delivery status and track your earnings.");

        // ── Status card ───────────────────────────────────
        VBox statusCard = new VBox(16);
        statusCard.setPadding(new Insets(22, 24, 22, 24));
        statusCard.setStyle(Styles.CARD);
        statusCard.setMaxWidth(420);

        Label statusTitle = Styles.h3("📡  Delivery Status");

        ToggleGroup tg  = new ToggleGroup();
        RadioButton on  = new RadioButton("🟢  Available for delivery");
        RadioButton off = new RadioButton("🔴  Currently busy / offline");
        on.setToggleGroup(tg);
        off.setToggleGroup(tg);
        on.setSelected(true);

        String rbStyle = "-fx-text-fill: " + Styles.TEXT + "; -fx-font-size: 14px;";
        on.setStyle(rbStyle);
        off.setStyle(rbStyle);

        Label statusMsg = Styles.body("You are currently online.");
        tg.selectedToggleProperty().addListener((obs, o, n) -> {
            statusMsg.setText(n == on
                    ? "You are currently online."
                    : "You have gone offline.");
        });

        statusCard.getChildren().addAll(statusTitle, on, off, statusMsg);

        // ── Active orders card ────────────────────────────
        VBox ordersCard = new VBox(14);
        ordersCard.setPadding(new Insets(22, 24, 22, 24));
        ordersCard.setStyle(Styles.CARD);
        ordersCard.setMaxWidth(520);

        Label ordersTitle = Styles.h3("📦  Active Orders");
        Label noOrders = Styles.body("No active deliveries right now. Stay on standby!");

        ordersCard.getChildren().addAll(ordersTitle, noOrders);

        // ── Summary card ──────────────────────────────────
        VBox summaryCard = new VBox(12);
        summaryCard.setPadding(new Insets(22, 24, 22, 24));
        summaryCard.setStyle(Styles.CARD);
        summaryCard.setMaxWidth(420);

        Label summaryTitle = Styles.h3("💰  Today's Summary");

        HBox statsRow = new HBox(32);
        statsRow.setAlignment(Pos.CENTER_LEFT);

        VBox dCard = miniStat("🛵", "0", "Deliveries");
        VBox eCard = miniStat("💵", "৳0", "Earnings");

        statsRow.getChildren().addAll(dCard, eCard);
        summaryCard.getChildren().addAll(summaryTitle, statsRow);

        content.getChildren().addAll(title, sub, statusCard, ordersCard, summaryCard);
        return content;
    }

    private VBox miniStat(String icon, String value, String label) {
        VBox box = new VBox(4);
        box.setAlignment(Pos.CENTER_LEFT);
        Label i = new Label(icon);
        i.setStyle("-fx-font-size: 22px;");
        Label v = new Label(value);
        v.setStyle("-fx-text-fill: " + Styles.ACCENT +
                "; -fx-font-size: 22px; -fx-font-weight: bold;");
        Label l = Styles.body(label);
        box.getChildren().addAll(i, v, l);
        return box;
    }
}