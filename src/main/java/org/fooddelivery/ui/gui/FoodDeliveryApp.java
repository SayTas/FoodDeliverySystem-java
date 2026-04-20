package org.fooddelivery.ui.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.fooddelivery.model.Coupon;
import org.fooddelivery.service.*;

public class FoodDeliveryApp extends Application {

    // ── Shared services (one instance per app session) ─────
    public static RestaurantService restaurantService;
    public static ReviewService     reviewService;
    public static OrderService      orderService;
    public static CouponService     couponService;
    public static AuthService       authService;

    static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        initServices();

        stage.setTitle("🍔 FoodDash — Food Delivery System");
        stage.setMinWidth(960);
        stage.setMinHeight(680);
        stage.setScene(LoginScene.create());
        stage.show();
    }

    private static void initServices() {
        restaurantService = new RestaurantService();
        reviewService     = new ReviewService();
        orderService      = new OrderService();
        couponService     = new CouponService();
        authService       = new AuthService();

        // Seed coupons
        couponService.addCoupon(new Coupon("SAVE10", 10, 100));
        couponService.addCoupon(new Coupon("FLAT50", 50, 200));
    }

    /** Central scene-switcher — call from any scene. */
    public static void navigate(Scene scene) {
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }
}