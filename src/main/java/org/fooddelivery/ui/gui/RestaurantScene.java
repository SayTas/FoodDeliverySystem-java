package org.fooddelivery.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.fooddelivery.model.MenuItem;
import org.fooddelivery.model.Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RestaurantScene {

    private final String username;
    private VBox menuListBox;

    private RestaurantScene(String username) {
        this.username = username;
    }

    public static Scene create(String username) {
        return new RestaurantScene(username).build();
    }

    private Scene build() {
        BorderPane root = new BorderPane();
        root.setStyle(Styles.ROOT);

        HBox nav = Styles.navBar("Restaurant Dashboard", username);
        Button logout = Styles.secondaryButton("Logout");
        logout.setOnAction(e -> FoodDeliveryApp.navigate(LoginScene.create()));
        nav.getChildren().add(logout);

        ScrollPane scroll = new ScrollPane(buildContent());
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        root.setTop(nav);
        root.setCenter(scroll);

        return new Scene(root, 1020, 720);
    }

    private VBox buildContent() {
        VBox content = new VBox(28);
        content.setPadding(new Insets(28));

        Label title = Styles.h2("🏪  Restaurant Management");
        Label sub   = Styles.body("Select your restaurant to manage its menu.");

        // ── Restaurant selector ───────────────────────────
        HBox selectorRow = new HBox(14);
        selectorRow.setAlignment(Pos.CENTER_LEFT);

        Label lbl = new Label("Restaurant:");
        lbl.setStyle("-fx-text-fill: " + Styles.MUTED + "; -fx-font-size: 14px;");

        ComboBox<String> restCombo = new ComboBox<>();
        restCombo.setPromptText("Choose a restaurant");
        restCombo.setStyle(Styles.INPUT);
        restCombo.setPrefWidth(260);
        for (Restaurant r : FoodDeliveryApp.restaurantService.getAll()) {
            restCombo.getItems().add(r.getName());
        }

        menuListBox = new VBox(10);

        Button loadBtn = Styles.primaryButton("Load Menu");
        loadBtn.setOnAction(e -> {
            String name = restCombo.getValue();
            if (name == null) return;
            FoodDeliveryApp.restaurantService.findByName(name)
                    .ifPresent(this::populateMenu);
        });

        selectorRow.getChildren().addAll(lbl, restCombo, loadBtn);

        // ── Add menu item card ────────────────────────────
        VBox addCard = buildAddItemCard(restCombo);

        // ── Menu display ──────────────────────────────────
        Label menuTitle = Styles.h3("📋  Current Menu");

        content.getChildren().addAll(
                title, sub, selectorRow, addCard, menuTitle, menuListBox);
        return content;
    }

    private VBox buildAddItemCard(ComboBox<String> restCombo) {
        VBox card = new VBox(16);
        card.setPadding(new Insets(22, 24, 22, 24));
        card.setStyle(Styles.CARD);
        card.setMaxWidth(580);

        Label cardTitle = Styles.h3("➕  Add Menu Item");

        TextField nameField = new TextField();
        nameField.setPromptText("Item name  (e.g. Zinger Burger)");
        nameField.setStyle(Styles.INPUT);
        nameField.setMaxWidth(Double.MAX_VALUE);

        TextField priceField = new TextField();
        priceField.setPromptText("Price (৳)");
        priceField.setStyle(Styles.INPUT);
        priceField.setPrefWidth(180);

        TextField qtyField = new TextField();
        qtyField.setPromptText("Stock qty");
        qtyField.setStyle(Styles.INPUT);
        qtyField.setPrefWidth(160);

        HBox priceRow = new HBox(14, priceField, qtyField);

        TextField addOnsField = new TextField();
        addOnsField.setPromptText("Add-ons — comma separated  (e.g. Extra Cheese +20, Sauce +10)");
        addOnsField.setStyle(Styles.INPUT);
        addOnsField.setMaxWidth(Double.MAX_VALUE);

        Label msgLbl = new Label();
        msgLbl.setStyle("-fx-font-size: 13px;");
        msgLbl.setVisible(false);
        msgLbl.setManaged(false);

        Button addBtn = Styles.primaryButton("Add Item");
        addBtn.setOnAction(e -> {
            String rName    = restCombo.getValue();
            String itemName = nameField.getText().trim();
            String priceStr = priceField.getText().trim();
            String qtyStr   = qtyField.getText().trim();

            if (rName == null) {
                setMsg(msgLbl, "Select a restaurant first.", false);
                return;
            }
            if (itemName.isEmpty() || priceStr.isEmpty() || qtyStr.isEmpty()) {
                setMsg(msgLbl, "Please fill in name, price, and quantity.", false);
                return;
            }
            try {
                double price = Double.parseDouble(priceStr);
                int    qty   = Integer.parseInt(qtyStr);

                List<String> addOns = new ArrayList<>();
                String aoTxt = addOnsField.getText().trim();
                if (!aoTxt.isEmpty()) {
                    addOns = Arrays.asList(aoTxt.split(",\\s*"));
                }

                FoodDeliveryApp.restaurantService
                        .addMenuToRestaurant(rName, itemName, price, qty, addOns);

                setMsg(msgLbl, "✔ \"" + itemName + "\" added to " + rName + "!", true);
                nameField.clear();
                priceField.clear();
                qtyField.clear();
                addOnsField.clear();

                FoodDeliveryApp.restaurantService.findByName(rName)
                        .ifPresent(this::populateMenu);

            } catch (NumberFormatException ex) {
                setMsg(msgLbl, "Price and quantity must be valid numbers.", false);
            }
        });

        card.getChildren().addAll(
                cardTitle,
                Styles.labeled("Item Name", nameField),
                Styles.labeled("Price & Quantity", priceRow),
                Styles.labeled("Add-ons (comma separated)", addOnsField),
                msgLbl, addBtn
        );
        return card;
    }

    private void populateMenu(Restaurant r) {
        menuListBox.getChildren().clear();
        if (r.getMenu().isEmpty()) {
            menuListBox.getChildren().add(
                    Styles.body("No menu items yet. Add some above!"));
            return;
        }
        for (MenuItem item : r.getMenu()) {
            HBox row = new HBox(16);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(14, 20, 14, 20));
            row.setStyle(Styles.CARD);

            Label nameLbl = Styles.h3(item.getName());
            HBox.setHgrow(nameLbl, Priority.ALWAYS);

            Label qtyLbl  = Styles.body("Stock: " + item.getQuantity());
            Label priceLbl = Styles.accent("৳" + (int) item.getPrice());

            if (item.hasAddOns()) {
                Label aoLbl = Styles.body("  •  " + item.getAddOns().size() + " add-ons");
                row.getChildren().addAll(nameLbl, aoLbl, qtyLbl, priceLbl);
            } else {
                row.getChildren().addAll(nameLbl, qtyLbl, priceLbl);
            }
            menuListBox.getChildren().add(row);
        }
    }

    private void setMsg(Label lbl, String text, boolean success) {
        lbl.setText(text);
        lbl.setStyle("-fx-font-size: 13px; -fx-text-fill: " +
                (success ? Styles.SUCCESS : Styles.DANGER) + ";");
        lbl.setVisible(true);
        lbl.setManaged(true);
    }
}