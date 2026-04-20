package org.fooddelivery.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class Review implements Serializable {

    private String restaurantName;
    private String reviewerName;
    private int    rating;       // 1 – 5
    private String comment;
    private String date;         // ISO date string e.g. "2026-04-18"

    public Review() {}

    public Review(String restaurantName, String reviewerName,
                  int rating, String comment, String date) {
        this.restaurantName = restaurantName;
        this.reviewerName   = reviewerName;
        this.rating         = rating;
        this.comment        = comment;
        this.date           = date;
    }

    // ── Getters ────────────────────────────────────────────
    public String getRestaurantName() { return restaurantName; }
    public String getReviewerName()   { return reviewerName;   }
    public int    getRating()         { return rating;         }
    public String getComment()        { return comment;        }
    public String getDate()           { return date;           }

    // ── Setters (required by XMLEncoder / JAXB) ────────────
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }
    public void setReviewerName(String reviewerName)     { this.reviewerName   = reviewerName;   }
    public void setRating(int rating)                    { this.rating         = rating;         }
    public void setComment(String comment)               { this.comment        = comment;        }
    public void setDate(String date)                     { this.date           = date;           }

    /** Returns a Unicode star string e.g. "★★★★☆" for rating 4 */
    public String stars() {
        return "★".repeat(Math.max(0, rating)) + "☆".repeat(Math.max(0, 5 - rating));
    }

    @Override
    public String toString() {
        return String.format("[%s] %s  by %s  (%s)",
                stars(), comment, reviewerName, date);
    }
}