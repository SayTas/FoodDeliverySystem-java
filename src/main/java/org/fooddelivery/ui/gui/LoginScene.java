package org.fooddelivery.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.fooddelivery.model.AuthUser;

public class LoginScene {

    public static Scene create() {
        HBox root = new HBox();
        root.setStyle("-fx-background-color: " + Styles.BG + ";");

        VBox brand  = buildBrandPanel();
        VBox form   = buildFormPanel();

        HBox.setHgrow(brand, Priority.ALWAYS);
        form.setMinWidth(420);
        form.setMaxWidth(460);

        root.getChildren().addAll(brand, form);
        return new Scene(root, 980, 680);
    }

    // ── Left branding panel ───────────────────────────────

    private static VBox buildBrandPanel() {
        VBox box = new VBox(18);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(60, 50, 60, 50));
        box.setStyle("-fx-background-color: linear-gradient(" +
                "to bottom right, #1A1A2E, #0D0D20);");

        Label emoji = new Label("🍔");
        emoji.setStyle("-fx-font-size: 80px;");

        Label title = new Label("FoodDash");
        title.setStyle("-fx-text-fill: " + Styles.ACCENT +
                "; -fx-font-size: 46px; -fx-font-weight: bold;");

        Label sub = new Label("Delicious food, delivered fast.");
        sub.setStyle("-fx-text-fill: " + Styles.MUTED + "; -fx-font-size: 16px;");

        Label spacer = new Label();

        Label f1 = new Label("🍕   Browse restaurants near you");
        Label f2 = new Label("🛒   Build your cart with add-ons");
        Label f3 = new Label("⭐   Rate your dining experience");
        for (Label f : new Label[]{f1, f2, f3}) {
            f.setStyle("-fx-text-fill: " + Styles.TEXT +
                    "; -fx-font-size: 15px; -fx-padding: 4 0 0 0;");
        }

        box.getChildren().addAll(emoji, title, sub, spacer, f1, f2, f3);
        return box;
    }

    // ── Right form panel ──────────────────────────────────

    private static VBox buildFormPanel() {
        VBox panel = new VBox(26);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(48, 52, 48, 52));
        panel.setStyle("-fx-background-color: " + Styles.SURFACE + ";");

        Label welcome = Styles.h1("Welcome Back 👋");

        // ── Tab toggle ────────────────────────────────────
        ToggleGroup tg = new ToggleGroup();
        ToggleButton loginTab = new ToggleButton("Login");
        ToggleButton regTab   = new ToggleButton("Register");
        loginTab.setToggleGroup(tg);
        regTab.setToggleGroup(tg);
        loginTab.setSelected(true);

        String activeTab   = "-fx-background-color: " + Styles.ACCENT  + "; -fx-text-fill: white;"
                + "-fx-font-weight: bold; -fx-font-size: 14px;"
                + "-fx-padding: 10 30; -fx-cursor: hand; -fx-border-width: 0;";
        String inactiveTab = "-fx-background-color: " + Styles.SURFACE2 + "; -fx-text-fill: " + Styles.MUTED + ";"
                + "-fx-font-size: 14px; -fx-padding: 10 30; -fx-cursor: hand; -fx-border-width: 0;";

        loginTab.setStyle(activeTab   + "-fx-background-radius: 8 0 0 8;");
        regTab.setStyle  (inactiveTab + "-fx-background-radius: 0 8 8 0;");

        HBox tabs = new HBox(loginTab, regTab);
        tabs.setAlignment(Pos.CENTER);

        // ── Forms ─────────────────────────────────────────
        VBox loginForm = buildLoginForm();
        VBox regForm   = buildRegisterForm();
        regForm.setVisible(false);
        regForm.setManaged(false);

        tg.selectedToggleProperty().addListener((obs, old, nw) -> {
            boolean isLogin = (nw == loginTab);
            loginForm.setVisible(isLogin);
            loginForm.setManaged(isLogin);
            regForm.setVisible(!isLogin);
            regForm.setManaged(!isLogin);

            if (isLogin) {
                loginTab.setStyle(activeTab   + "-fx-background-radius: 8 0 0 8;");
                regTab.setStyle  (inactiveTab + "-fx-background-radius: 0 8 8 0;");
            } else {
                loginTab.setStyle(inactiveTab + "-fx-background-radius: 8 0 0 8;");
                regTab.setStyle  (activeTab   + "-fx-background-radius: 0 8 8 0;");
            }
        });

        panel.getChildren().addAll(welcome, tabs, loginForm, regForm);
        return panel;
    }

    private static VBox buildLoginForm() {
        VBox form = new VBox(14);

        TextField     userField = new TextField();
        PasswordField passField = new PasswordField();
        userField.setPromptText("Username");
        passField.setPromptText("Password");
        userField.setStyle(Styles.INPUT);
        passField.setStyle(Styles.INPUT);
        userField.setMaxWidth(Double.MAX_VALUE);
        passField.setMaxWidth(Double.MAX_VALUE);

        Label errorLbl = new Label();
        errorLbl.setStyle("-fx-text-fill: " + Styles.DANGER + "; -fx-font-size: 13px;");
        errorLbl.setVisible(false);
        errorLbl.setManaged(false);

        Button loginBtn = Styles.primaryButton("Login  →");
        loginBtn.setMaxWidth(Double.MAX_VALUE);

        loginBtn.setOnAction(e -> {
            String u = userField.getText().trim();
            String p = passField.getText().trim();
            if (u.isEmpty() || p.isEmpty()) {
                errorLbl.setText("Please enter your username and password.");
                errorLbl.setVisible(true);
                errorLbl.setManaged(true);
                return;
            }
            AuthUser user = FoodDeliveryApp.authService.login(u, p);
            if (user == null) {
                errorLbl.setText("Invalid credentials. Please try again.");
                errorLbl.setVisible(true);
                errorLbl.setManaged(true);
            } else {
                errorLbl.setVisible(false);
                errorLbl.setManaged(false);
                switch (user.getRole()) {
                    case "USER"       -> FoodDeliveryApp.navigate(UserScene.create(user.getUsername()));
                    case "ADMIN"      -> FoodDeliveryApp.navigate(AdminScene.create(user.getUsername()));
                    case "RESTAURANT" -> FoodDeliveryApp.navigate(RestaurantScene.create(user.getUsername()));
                    case "RIDER"      -> FoodDeliveryApp.navigate(RiderScene.create(user.getUsername()));
                    default           -> FoodDeliveryApp.navigate(UserScene.create(user.getUsername()));
                }
            }
        });

        passField.setOnAction(loginBtn.getOnAction());

        form.getChildren().addAll(
                Styles.labeled("Username", userField),
                Styles.labeled("Password", passField),
                errorLbl,
                loginBtn
        );
        return form;
    }

    private static VBox buildRegisterForm() {
        VBox form = new VBox(14);

        TextField     userField = new TextField();
        PasswordField passField = new PasswordField();
        userField.setPromptText("Choose a username");
        passField.setPromptText("Choose a password");
        userField.setStyle(Styles.INPUT);
        passField.setStyle(Styles.INPUT);
        userField.setMaxWidth(Double.MAX_VALUE);
        passField.setMaxWidth(Double.MAX_VALUE);

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("USER", "RESTAURANT", "RIDER", "ADMIN");
        roleBox.setValue("USER");
        roleBox.setMaxWidth(Double.MAX_VALUE);
        roleBox.setStyle(Styles.INPUT);

        Label msgLbl = new Label();
        msgLbl.setStyle("-fx-font-size: 13px;");
        msgLbl.setVisible(false);
        msgLbl.setManaged(false);

        Button regBtn = Styles.primaryButton("Create Account");
        regBtn.setMaxWidth(Double.MAX_VALUE);

        regBtn.setOnAction(e -> {
            String u = userField.getText().trim();
            String p = passField.getText().trim();
            if (u.isEmpty() || p.isEmpty()) {
                msgLbl.setText("Please fill in all fields.");
                msgLbl.setStyle("-fx-text-fill: " + Styles.DANGER + "; -fx-font-size: 13px;");
                msgLbl.setVisible(true);
                msgLbl.setManaged(true);
                return;
            }
            FoodDeliveryApp.authService.register(u, p, roleBox.getValue());
            msgLbl.setText("✔ Account created! Please switch to Login.");
            msgLbl.setStyle("-fx-text-fill: " + Styles.SUCCESS + "; -fx-font-size: 13px;");
            msgLbl.setVisible(true);
            msgLbl.setManaged(true);
            userField.clear();
            passField.clear();
        });

        form.getChildren().addAll(
                Styles.labeled("Username", userField),
                Styles.labeled("Password", passField),
                Styles.labeled("Role", roleBox),
                msgLbl,
                regBtn
        );
        return form;
    }
}