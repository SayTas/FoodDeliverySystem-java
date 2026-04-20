package org.fooddelivery.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/** Central styling constants and factory helpers. */
public final class Styles {

    // ── Palette ───────────────────────────────────────────
    public static final String BG       = "#0F0F1A";
    public static final String SURFACE  = "#1A1A2E";
    public static final String SURFACE2 = "#252542";
    public static final String ACCENT   = "#FF6B35";
    public static final String TEXT     = "#FFFFFF";
    public static final String MUTED    = "#B0B0C4";
    public static final String SUCCESS  = "#2EC4B6";
    public static final String DANGER   = "#FF4757";
    public static final String BORDER   = "#30305A";
    public static final String GOLD     = "#FFD700";

    // ── Common style strings ──────────────────────────────

    public static final String ROOT =
            "-fx-background-color: " + BG + ";";

    public static final String CARD =
            "-fx-background-color: " + SURFACE + ";" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-color: " + BORDER + ";" +
                    "-fx-border-radius: 12;" +
                    "-fx-border-width: 1;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.45), 10, 0, 0, 4);";

    public static final String CARD_HOVER =
            "-fx-background-color: " + SURFACE2 + ";" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-color: " + ACCENT + ";" +
                    "-fx-border-radius: 12;" +
                    "-fx-border-width: 1;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.55), 14, 0, 0, 6);";

    public static final String INPUT =
            "-fx-background-color: " + SURFACE2 + ";" +
                    "-fx-text-fill: " + TEXT + ";" +
                    "-fx-border-color: " + BORDER + ";" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 10 14;" +
                    "-fx-font-size: 14px;" +
                    "-fx-prompt-text-fill: " + MUTED + ";";

    // ── Button style strings ──────────────────────────────

    public static String primaryBtnStyle() {
        return "-fx-background-color: " + ACCENT + ";" +
                "-fx-text-fill: white; -fx-font-weight: bold;" +
                "-fx-font-size: 14px; -fx-background-radius: 8;" +
                "-fx-padding: 10 22; -fx-cursor: hand;";
    }

    public static String secondaryBtnStyle() {
        return "-fx-background-color: " + SURFACE2 + ";" +
                "-fx-text-fill: " + TEXT + "; -fx-font-size: 14px;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: " + BORDER + "; -fx-border-radius: 8;" +
                "-fx-padding: 10 22; -fx-cursor: hand;";
    }

    public static String dangerBtnStyle() {
        return "-fx-background-color: " + DANGER + ";" +
                "-fx-text-fill: white; -fx-font-weight: bold;" +
                "-fx-font-size: 13px; -fx-background-radius: 8;" +
                "-fx-padding: 8 16; -fx-cursor: hand;";
    }

    public static String successBtnStyle() {
        return "-fx-background-color: " + SUCCESS + ";" +
                "-fx-text-fill: #0F0F1A; -fx-font-weight: bold;" +
                "-fx-font-size: 14px; -fx-background-radius: 8;" +
                "-fx-padding: 10 22; -fx-cursor: hand;";
    }

    public static String navBtnStyle(boolean active) {
        return "-fx-background-color: " + (active ? SURFACE2 : "transparent") + ";" +
                "-fx-text-fill: " + (active ? TEXT : MUTED) + ";" +
                "-fx-font-size: 14px; -fx-alignment: center-left;" +
                "-fx-padding: 13 20; -fx-cursor: hand;" +
                "-fx-border-color: transparent transparent transparent "
                + (active ? ACCENT : "transparent") + ";" +
                "-fx-border-width: 0 0 0 3;" +
                "-fx-background-radius: 0 8 8 0;";
    }

    // ── Button factories ──────────────────────────────────

    public static Button primaryButton(String text) {
        Button b = new Button(text);
        b.setStyle(primaryBtnStyle());
        b.setOnMouseEntered(e -> b.setStyle(primaryBtnStyle()
                .replace(ACCENT, "#E55A25")));
        b.setOnMouseExited(e -> b.setStyle(primaryBtnStyle()));
        return b;
    }

    public static Button secondaryButton(String text) {
        Button b = new Button(text);
        b.setStyle(secondaryBtnStyle());
        b.setOnMouseEntered(e -> b.setStyle(secondaryBtnStyle()
                .replace(SURFACE2, "#303050")));
        b.setOnMouseExited(e -> b.setStyle(secondaryBtnStyle()));
        return b;
    }

    public static Button dangerButton(String text) {
        Button b = new Button(text);
        b.setStyle(dangerBtnStyle());
        return b;
    }

    public static Button successButton(String text) {
        Button b = new Button(text);
        b.setStyle(successBtnStyle());
        return b;
    }

    // ── Label factories ───────────────────────────────────

    public static Label h1(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("System", FontWeight.BOLD, 30));
        l.setStyle("-fx-text-fill: " + TEXT + ";");
        return l;
    }

    public static Label h2(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("System", FontWeight.BOLD, 22));
        l.setStyle("-fx-text-fill: " + TEXT + ";");
        return l;
    }

    public static Label h3(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("System", FontWeight.BOLD, 16));
        l.setStyle("-fx-text-fill: " + TEXT + ";");
        return l;
    }

    public static Label body(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: " + MUTED + "; -fx-font-size: 13px;");
        return l;
    }

    public static Label accent(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: " + ACCENT +
                "; -fx-font-weight: bold; -fx-font-size: 15px;");
        return l;
    }

    // ── Top navigation bar ────────────────────────────────

    public static HBox navBar(String title, String username) {
        HBox bar = new HBox(12);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(0, 24, 0, 24));
        bar.setStyle("-fx-background-color: " + SURFACE + ";" +
                "-fx-border-color: transparent transparent " + BORDER +
                " transparent; -fx-border-width: 0 0 1 0;" +
                "-fx-min-height: 62; -fx-max-height: 62;");

        Label logo = new Label("🍔 FoodDash");
        logo.setStyle("-fx-text-fill: " + ACCENT +
                "; -fx-font-size: 20px; -fx-font-weight: bold;");

        Label sep = new Label("  /  " + title);
        sep.setStyle("-fx-text-fill: " + MUTED + "; -fx-font-size: 15px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLbl = new Label("👤  " + username);
        userLbl.setStyle("-fx-text-fill: " + TEXT + "; -fx-font-size: 13px;");

        bar.getChildren().addAll(logo, sep, spacer, userLbl);
        return bar;
    }

    // ── Utility ───────────────────────────────────────────

    /** Builds a ★★★☆☆ string for a 1-5 avg rating. */
    public static String starsFor(double avg) {
        if (avg <= 0) return "☆☆☆☆☆";
        int full = (int) Math.round(avg);
        full = Math.max(0, Math.min(5, full));
        return "★".repeat(full) + "☆".repeat(5 - full);
    }

    /** Emoji icon for a restaurant name. */
    public static String restaurantEmoji(String name) {
        String n = name.toLowerCase();
        if (n.contains("pizza"))   return "🍕";
        if (n.contains("burger") || n.contains("kfc")) return "🍔";
        if (n.contains("chicken")) return "🍗";
        if (n.contains("biryani") || n.contains("east") || n.contains("curry")) return "🍛";
        if (n.contains("cafe") || n.contains("coffee") || n.contains("north end")) return "☕";
        if (n.contains("sushi") || n.contains("seafood")) return "🍱";
        if (n.contains("pasta") || n.contains("segreto") || n.contains("italian")) return "🍝";
        return "🍴";
    }

    /** Labeled form field wrapper. */
    public static VBox labeled(String labelText, javafx.scene.Node field) {
        VBox box = new VBox(6);
        Label lbl = new Label(labelText);
        lbl.setStyle("-fx-text-fill: " + MUTED +
                "; -fx-font-size: 12px; -fx-font-weight: bold;");
        box.getChildren().addAll(lbl, field);
        return box;
    }

    private Styles() {}
}