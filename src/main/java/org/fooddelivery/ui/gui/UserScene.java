package org.fooddelivery.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.fooddelivery.model.*;
import org.fooddelivery.model.MenuItem;
import org.fooddelivery.service.ReviewService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserScene {

    private final String username;
    private final Order  currentOrder = new Order();

    // Navigation buttons (instance fields for active-state toggling)
    private Button btnRestaurants;
    private Button btnCart;
    private Button btnReviews;

    // Live-updating widgets
    private VBox        cartItemsBox;
    private Label       cartTotalLabel;
    private StackPane   contentArea;

    // Cached panels
    private VBox restaurantsPanel;
    private VBox cartPanel;
    private VBox reviewsPanel;

    private UserScene(String username) {
        this.username = username;
    }

    public static Scene create(String username) {
        return new UserScene(username).build();
    }

    // ── Root layout ───────────────────────────────────────

    private Scene build() {
        BorderPane root = new BorderPane();
        root.setStyle(Styles.ROOT);

        // Build panels once
        restaurantsPanel = buildRestaurantsPanel();
        cartPanel        = buildCartPanel();
        reviewsPanel     = buildReviewsPanel();

        // Content area — swap panels here
        contentArea = new StackPane(restaurantsPanel);
        contentArea.setStyle("-fx-padding: 26;");

        ScrollPane scroll = new ScrollPane(contentArea);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        // Nav bar
        HBox nav = Styles.navBar("User Dashboard", username);
        Button logout = Styles.secondaryButton("Logout");
        logout.setOnAction(e -> FoodDeliveryApp.navigate(LoginScene.create()));
        nav.getChildren().add(logout);

        root.setTop(nav);
        root.setLeft(buildSidebar());
        root.setCenter(scroll);

        return new Scene(root, 1150, 740);
    }

    // ── Sidebar ───────────────────────────────────────────

    private VBox buildSidebar() {
        VBox sidebar = new VBox(4);
        sidebar.setStyle("-fx-background-color: " + Styles.SURFACE + ";" +
                "-fx-min-width: 230; -fx-max-width: 230;" +
                "-fx-border-color: transparent " + Styles.BORDER +
                " transparent transparent; -fx-border-width: 0 1 0 0;");
        sidebar.setPadding(new Insets(28, 0, 24, 0));

        Label sectionLbl = Styles.body("  MENU");
        sectionLbl.setPadding(new Insets(0, 0, 12, 18));

        btnRestaurants = new Button("🍽️   Restaurants");
        btnCart        = new Button("🛒   My Cart");
        btnReviews     = new Button("⭐   Reviews");

        for (Button b : new Button[]{btnRestaurants, btnCart, btnReviews}) {
            b.setMaxWidth(Double.MAX_VALUE);
            b.setStyle(Styles.navBtnStyle(false));
        }
        btnRestaurants.setStyle(Styles.navBtnStyle(true));

        btnRestaurants.setOnAction(e -> {
            setActive(btnRestaurants);
            showPanel(restaurantsPanel);
        });
        btnCart.setOnAction(e -> {
            setActive(btnCart);
            refreshCartPanel();
            showPanel(cartPanel);
        });
        btnReviews.setOnAction(e -> {
            setActive(btnReviews);
            showPanel(reviewsPanel);
        });

        sidebar.getChildren().addAll(sectionLbl,
                btnRestaurants, btnCart, btnReviews);
        return sidebar;
    }

    private void setActive(Button active) {
        for (Button b : new Button[]{btnRestaurants, btnCart, btnReviews}) {
            b.setStyle(Styles.navBtnStyle(b == active));
        }
    }

    private void showPanel(VBox panel) {
        contentArea.getChildren().setAll(panel);
    }

    // ── Restaurants panel ─────────────────────────────────

    private VBox buildRestaurantsPanel() {
        VBox panel = new VBox(20);

        // Header with search controls
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = Styles.h2("🍽️  Restaurants");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by name...");
        searchField.setStyle(Styles.INPUT);
        searchField.setPrefWidth(200);

        TextField areaField = new TextField();
        areaField.setPromptText("Filter by area...");
        areaField.setStyle(Styles.INPUT);
        areaField.setPrefWidth(170);

        Button searchBtn = Styles.primaryButton("Search");
        Button allBtn    = Styles.secondaryButton("Show All");

        header.getChildren().addAll(title, spacer,
                searchField, areaField, searchBtn, allBtn);

        VBox listBox = new VBox(12);

        // Load all restaurants into cards
        Runnable loadAll = () -> {
            listBox.getChildren().clear();
            for (Restaurant r : FoodDeliveryApp.restaurantService.getAll()) {
                listBox.getChildren().add(buildRestaurantCard(r, listBox));
            }
        };

        searchBtn.setOnAction(e -> {
            listBox.getChildren().clear();
            String kw   = searchField.getText().trim();
            String area = areaField.getText().trim();
            List<Restaurant> results;
            if (!area.isEmpty()) {
                results = FoodDeliveryApp.restaurantService.getNearest(area);
            } else if (!kw.isEmpty()) {
                results = FoodDeliveryApp.restaurantService.searchByName(kw);
            } else {
                results = FoodDeliveryApp.restaurantService.getAll();
            }
            if (results.isEmpty()) {
                listBox.getChildren().add(Styles.body("No restaurants found."));
            } else {
                for (Restaurant r : results)
                    listBox.getChildren().add(buildRestaurantCard(r, listBox));
            }
        });

        allBtn.setOnAction(e -> {
            searchField.clear();
            areaField.clear();
            loadAll.run();
        });

        loadAll.run();

        panel.getChildren().addAll(header, listBox);
        return panel;
    }

    private HBox buildRestaurantCard(Restaurant r, VBox listBox) {
        HBox card = new HBox(18);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(18, 22, 18, 22));
        card.setStyle(Styles.CARD);
        card.setOnMouseEntered(e -> card.setStyle(Styles.CARD_HOVER));
        card.setOnMouseExited(e  -> card.setStyle(Styles.CARD));

        Label icon = new Label(Styles.restaurantEmoji(r.getName()));
        icon.setStyle("-fx-font-size: 34px;");

        VBox info = new VBox(5);
        Label name = Styles.h3(r.getName());

        double avg = FoodDeliveryApp.reviewService.getAverageRating(r.getName());
        String ratingTxt = avg > 0
                ? String.format("%.1f  %s", avg, Styles.starsFor(avg))
                : "No reviews yet";
        Label rating = new Label(ratingTxt);
        rating.setStyle("-fx-text-fill: " + Styles.GOLD + "; -fx-font-size: 13px;");

        Label details = Styles.body("📍 " + r.getLocation() +
                "   •   " + r.getMenu().size() + " items");

        info.getChildren().addAll(name, rating, details);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button viewBtn = Styles.primaryButton("View Menu  →");
        viewBtn.setOnAction(e -> openMenuPanel(r, listBox));

        card.getChildren().addAll(icon, info, spacer, viewBtn);
        return card;
    }

    // ── Menu panel (injected into the restaurant list area) ──

    private void openMenuPanel(Restaurant r, VBox parentBox) {
        VBox panel = new VBox(20);

        HBox header = new HBox(14);
        header.setAlignment(Pos.CENTER_LEFT);

        Button backBtn = Styles.secondaryButton("← Back");
        backBtn.setOnAction(e -> showPanel(restaurantsPanel));

        Label title = Styles.h2(Styles.restaurantEmoji(r.getName()) + "  " + r.getName());
        Label loc   = Styles.body("  📍 " + r.getLocation());

        header.getChildren().addAll(backBtn, title, loc);

        VBox menuBox = new VBox(12);

        if (r.getMenu().isEmpty()) {
            menuBox.getChildren().add(Styles.body("No menu items yet."));
        } else {
            for (MenuItem item : r.getMenu()) {
                menuBox.getChildren().add(buildMenuItemCard(item));
            }
        }

        panel.getChildren().addAll(header, menuBox);
        showPanel(panel);
    }

    private HBox buildMenuItemCard(MenuItem item) {
        HBox card = new HBox(18);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(14, 20, 14, 20));
        card.setStyle(Styles.CARD);

        VBox info = new VBox(5);
        Label name = Styles.h3(item.getName());
        String addOnHint = item.hasAddOns()
                ? item.getAddOns().size() + " add-on option(s) available"
                : "No add-ons";
        Label hint = Styles.body(addOnHint);
        info.getChildren().addAll(name, hint);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label price = Styles.accent("৳" + (int) item.getPrice());
        Button addBtn = Styles.successButton("+ Add to Cart");
        addBtn.setOnAction(e -> showAddToCartDialog(item));

        card.getChildren().addAll(info, spacer, price, addBtn);
        return card;
    }

    private void showAddToCartDialog(MenuItem item) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add to Cart");
        dialog.setHeaderText(item.getName() + "  —  ৳" + (int) item.getPrice());
        dialog.getDialogPane().setStyle(
                "-fx-background-color: " + Styles.SURFACE + ";" +
                        "-fx-font-size: 14px;");

        VBox content = new VBox(12);
        content.setPadding(new Insets(8));

        List<CheckBox> addonBoxes = new ArrayList<>();

        if (item.hasAddOns()) {
            content.getChildren().add(Styles.body("Select add-ons:"));
            for (String ao : item.getAddOns()) {
                CheckBox cb = new CheckBox(ao);
                cb.setStyle("-fx-text-fill: " + Styles.TEXT +
                        "; -fx-font-size: 14px;");
                addonBoxes.add(cb);
                content.getChildren().add(cb);
            }
        } else {
            content.getChildren().add(Styles.body("No add-ons for this item."));
        }

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        Button okBtn = (Button) dialog.getDialogPane()
                .lookupButton(ButtonType.OK);
        okBtn.setText("Add to Cart ✔");
        okBtn.setStyle(Styles.primaryBtnStyle());

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            List<String> chosen = new ArrayList<>();
            double extra = 0;
            for (CheckBox cb : addonBoxes) {
                if (cb.isSelected()) {
                    chosen.add(cb.getText());
                    extra += parseAddOnPrice(cb.getText());
                }
            }
            String label = chosen.isEmpty()
                    ? item.getName()
                    : item.getName() + " (" + String.join(", ", chosen) + ")";
            currentOrder.addItem(new MenuItem(label, item.getPrice() + extra, 1));
            updateCartBadge();
            showInfo("Added!", "\"" + item.getName() + "\" added to your cart.");
        }
    }

    private void updateCartBadge() {
        int n = currentOrder.getItems().size();
        btnCart.setText("🛒   My Cart (" + n + ")");
    }

    // ── Cart panel ────────────────────────────────────────

    private VBox buildCartPanel() {
        cartItemsBox  = new VBox(10);
        cartTotalLabel = new Label("Total:  ৳0");
        cartTotalLabel.setStyle("-fx-text-fill: " + Styles.ACCENT +
                "; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label title = Styles.h2("🛒  My Cart");

        // Checkout card
        VBox checkoutCard = new VBox(14);
        checkoutCard.setPadding(new Insets(22, 24, 22, 24));
        checkoutCard.setStyle(Styles.CARD);
        checkoutCard.setMinWidth(280);
        checkoutCard.setMaxWidth(300);

        Label checkoutTitle = Styles.h3("Checkout");

        TextField couponField = new TextField();
        couponField.setPromptText("Coupon code (e.g. SAVE10)");
        couponField.setStyle(Styles.INPUT);

        Label couponMsg = new Label();
        couponMsg.setStyle("-fx-font-size: 13px;");
        couponMsg.setVisible(false);
        couponMsg.setManaged(false);

        Button confirmBtn = Styles.successButton("✔  Confirm Order");
        confirmBtn.setMaxWidth(Double.MAX_VALUE);

        confirmBtn.setOnAction(e -> {
            if (currentOrder.getItems().isEmpty()) {
                showError("Your cart is empty.\nAdd items from a restaurant first.");
                return;
            }
            double finalTotal = currentOrder.calculateTotal();
            String code = couponField.getText().trim();
            if (!code.isEmpty()) {
                try {
                    finalTotal = FoodDeliveryApp.orderService.applyCoupon(
                            currentOrder, code, FoodDeliveryApp.couponService);
                    couponMsg.setText("✔ Coupon \"" + code + "\" applied!");
                    couponMsg.setStyle("-fx-text-fill: " + Styles.SUCCESS +
                            "; -fx-font-size: 13px;");
                    couponMsg.setVisible(true);
                    couponMsg.setManaged(true);
                } catch (Exception ex) {
                    couponMsg.setText("Invalid or inapplicable coupon.");
                    couponMsg.setStyle("-fx-text-fill: " + Styles.DANGER +
                            "; -fx-font-size: 13px;");
                    couponMsg.setVisible(true);
                    couponMsg.setManaged(true);
                }
            }
            double ft = finalTotal;
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Order");
            confirm.setHeaderText("Place your order?");
            confirm.setContentText("Final total:  ৳" + (int) ft);
            confirm.getDialogPane().setStyle(
                    "-fx-background-color: " + Styles.SURFACE + ";");
            confirm.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.OK) {
                    currentOrder.updateStatus("Confirmed");
                    currentOrder.getItems().clear();
                    couponField.clear();
                    couponMsg.setVisible(false);
                    couponMsg.setManaged(false);
                    refreshCartPanel();
                    updateCartBadge();
                    showInfo("Order Placed! 🎉",
                            "Your order has been confirmed.\nTotal:  ৳" + (int) ft);
                }
            });
        });

        checkoutCard.getChildren().addAll(
                checkoutTitle,
                Styles.labeled("Coupon Code", couponField),
                couponMsg,
                cartTotalLabel,
                confirmBtn);

        // Items + checkout side by side
        ScrollPane itemsScroll = new ScrollPane(cartItemsBox);
        itemsScroll.setFitToWidth(true);
        itemsScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        HBox.setHgrow(itemsScroll, Priority.ALWAYS);

        HBox mainArea = new HBox(24, itemsScroll, checkoutCard);

        VBox panel = new VBox(20, title, mainArea);
        refreshCartPanel();
        return panel;
    }

    private void refreshCartPanel() {
        cartItemsBox.getChildren().clear();

        if (currentOrder.getItems().isEmpty()) {
            Label empty = new Label("Your cart is empty. Browse restaurants to add items! 🍔");
            empty.setStyle("-fx-text-fill: " + Styles.MUTED +
                    "; -fx-font-size: 15px; -fx-padding: 20;");
            cartItemsBox.getChildren().add(empty);
            cartTotalLabel.setText("Total:  ৳0");
            return;
        }

        for (MenuItem item : currentOrder.getItems()) {
            HBox row = new HBox(14);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(13, 18, 13, 18));
            row.setStyle(Styles.CARD);

            Label name = new Label(item.getName());
            name.setStyle("-fx-text-fill: " + Styles.TEXT + "; -fx-font-size: 14px;");
            name.setWrapText(true);
            HBox.setHgrow(name, Priority.ALWAYS);

            Label price = Styles.accent("৳" + (int) item.getPrice());

            Button removeBtn = Styles.dangerButton("✕");
            final MenuItem toRemove = item;
            removeBtn.setOnAction(e -> {
                currentOrder.getItems().remove(toRemove);
                refreshCartPanel();
                updateCartBadge();
            });

            row.getChildren().addAll(name, price, removeBtn);
            cartItemsBox.getChildren().add(row);
        }
        cartTotalLabel.setText("Total:  ৳" + (int) currentOrder.calculateTotal());
    }

    // ── Reviews panel ─────────────────────────────────────

    private VBox buildReviewsPanel() {
        VBox panel = new VBox(28);

        Label title = Styles.h2("⭐  Reviews");

        HBox columns = new HBox(32);
        columns.setAlignment(Pos.TOP_LEFT);

        columns.getChildren().addAll(
                buildWriteReviewCard(),
                buildViewReviewsCard()
        );

        panel.getChildren().addAll(title, columns);
        return panel;
    }

    private VBox buildWriteReviewCard() {
        VBox card = new VBox(16);
        card.setPadding(new Insets(22, 24, 22, 24));
        card.setStyle(Styles.CARD);
        card.setMaxWidth(480);

        Label cardTitle = Styles.h3("✍️  Write a Review");

        ComboBox<String> restCombo = new ComboBox<>();
        restCombo.setPromptText("Select a restaurant");
        restCombo.setMaxWidth(Double.MAX_VALUE);
        restCombo.setStyle(Styles.INPUT);
        for (Restaurant r : FoodDeliveryApp.restaurantService.getAll()) {
            restCombo.getItems().add(r.getName());
        }

        // Interactive star rating
        HBox starRow   = new HBox(8);
        Label[] stars  = new Label[5];
        int[]   rating = {0};

        for (int i = 0; i < 5; i++) {
            final int idx = i;
            Label s = new Label("☆");
            s.setStyle("-fx-font-size: 30px; -fx-text-fill: " +
                    Styles.GOLD + "; -fx-cursor: hand;");
            s.setOnMouseEntered(ev -> {
                for (int j = 0; j <= idx; j++) stars[j].setText("★");
                for (int j = idx + 1; j < 5; j++) stars[j].setText("☆");
            });
            s.setOnMouseExited(ev -> {
                for (int j = 0; j < 5; j++)
                    stars[j].setText(j < rating[0] ? "★" : "☆");
            });
            s.setOnMouseClicked(ev -> {
                rating[0] = idx + 1;
                for (int j = 0; j < 5; j++)
                    stars[j].setText(j < rating[0] ? "★" : "☆");
            });
            stars[i] = s;
            starRow.getChildren().add(s);
        }
        Label ratingHint = Styles.body("  Click to rate");
        starRow.getChildren().add(ratingHint);

        TextArea commentArea = new TextArea();
        commentArea.setPromptText("Share your experience...");
        commentArea.setStyle(Styles.INPUT +
                "-fx-control-inner-background: " + Styles.SURFACE2 + ";");
        commentArea.setPrefRowCount(3);
        commentArea.setWrapText(true);
        commentArea.setMaxWidth(Double.MAX_VALUE);

        Label msgLbl = new Label();
        msgLbl.setStyle("-fx-font-size: 13px;");
        msgLbl.setVisible(false);
        msgLbl.setManaged(false);

        Button submitBtn = Styles.primaryButton("Submit Review");
        submitBtn.setOnAction(e -> {
            if (restCombo.getValue() == null) {
                showError("Please select a restaurant.");
                return;
            }
            if (rating[0] == 0) {
                showError("Please select a star rating.");
                return;
            }
            String comment = commentArea.getText().trim();
            if (comment.isEmpty()) comment = "No comment.";

            Review rv = new Review(
                    restCombo.getValue(), username,
                    rating[0], comment,
                    LocalDate.now().toString());
            FoodDeliveryApp.reviewService.addReview(rv);

            msgLbl.setText("✔ Review submitted for " + restCombo.getValue() + "!");
            msgLbl.setStyle("-fx-text-fill: " + Styles.SUCCESS + "; -fx-font-size: 13px;");
            msgLbl.setVisible(true);
            msgLbl.setManaged(true);

            commentArea.clear();
            rating[0] = 0;
            for (Label s : stars) s.setText("☆");
            restCombo.setValue(null);
        });

        card.getChildren().addAll(
                cardTitle,
                Styles.labeled("Restaurant", restCombo),
                Styles.labeled("Your Rating", starRow),
                Styles.labeled("Comment", commentArea),
                msgLbl,
                submitBtn
        );
        return card;
    }

    private VBox buildViewReviewsCard() {
        VBox card = new VBox(16);
        card.setPadding(new Insets(22, 24, 22, 24));
        card.setStyle(Styles.CARD);
        card.setMaxWidth(480);

        Label cardTitle = Styles.h3("📖  View Reviews");

        ComboBox<String> viewCombo = new ComboBox<>();
        viewCombo.setPromptText("Select a restaurant");
        viewCombo.setMaxWidth(Double.MAX_VALUE);
        viewCombo.setStyle(Styles.INPUT);
        for (Restaurant r : FoodDeliveryApp.restaurantService.getAll()) {
            viewCombo.getItems().add(r.getName());
        }

        VBox reviewsList = new VBox(10);

        Button loadBtn = Styles.secondaryButton("Load Reviews");
        loadBtn.setOnAction(e -> {
            String name = viewCombo.getValue();
            if (name == null) { showError("Please select a restaurant."); return; }

            reviewsList.getChildren().clear();
            List<Review> reviews = FoodDeliveryApp.reviewService.getByRestaurant(name);
            double avg = FoodDeliveryApp.reviewService.getAverageRating(name);

            if (reviews.isEmpty()) {
                reviewsList.getChildren().add(
                        Styles.body("No reviews yet for " + name + "."));
                return;
            }

            Label avgLbl = new Label(String.format(
                    "Average: %.1f / 5.0   %s   (%d review%s)",
                    avg, Styles.starsFor(avg),
                    reviews.size(), reviews.size() == 1 ? "" : "s"));
            avgLbl.setStyle("-fx-text-fill: " + Styles.GOLD +
                    "; -fx-font-size: 15px; -fx-font-weight: bold;");
            reviewsList.getChildren().add(avgLbl);

            for (Review rv : reviews) {
                VBox rc = new VBox(6);
                rc.setPadding(new Insets(12, 16, 12, 16));
                rc.setStyle(Styles.CARD);

                Label starsLbl = new Label(rv.stars());
                starsLbl.setStyle("-fx-text-fill: " + Styles.GOLD +
                        "; -fx-font-size: 17px;");
                Label commentLbl = new Label(rv.getComment());
                commentLbl.setStyle("-fx-text-fill: " + Styles.TEXT +
                        "; -fx-font-size: 14px;");
                commentLbl.setWrapText(true);
                Label metaLbl = Styles.body(
                        "by " + rv.getReviewerName() + "   •   " + rv.getDate());

                rc.getChildren().addAll(starsLbl, commentLbl, metaLbl);
                reviewsList.getChildren().add(rc);
            }
        });

        card.getChildren().addAll(
                cardTitle,
                Styles.labeled("Restaurant", viewCombo),
                loadBtn,
                reviewsList
        );
        return card;
    }

    // ── Helpers ───────────────────────────────────────────

    private double parseAddOnPrice(String label) {
        int i = label.lastIndexOf('+');
        if (i >= 0) {
            try { return Double.parseDouble(label.substring(i + 1).trim()); }
            catch (NumberFormatException ignored) {}
        }
        return 0;
    }

    private void showInfo(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setTitle(title);
        a.setHeaderText(null);
        a.getDialogPane().setStyle("-fx-background-color: " + Styles.SURFACE + ";");
        a.showAndWait();
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setTitle("Error");
        a.setHeaderText(null);
        a.getDialogPane().setStyle("-fx-background-color: " + Styles.SURFACE + ";");
        a.showAndWait();
    }
}