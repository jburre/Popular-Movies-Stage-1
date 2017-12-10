package com.example.lyz.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lyz on 28.11.2017.
 */

public class Movie {
    private long id;
    private double rating;
    private String imagePath;
    private String totalImagePath;
    private String description;
    private String title;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotalImagePath() {
        return totalImagePath;
    }

    public void setTotalImagePath(String totalImagePath) {
        this.totalImagePath = totalImagePath;
    }

}
