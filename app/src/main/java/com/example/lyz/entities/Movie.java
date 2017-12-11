package com.example.lyz.entities;


/**
 * Created by Lyz on 28.11.2017.
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 * an movie class representing the wanted data and making it easy to store
 */
public class Movie implements Parcelable{
    private long id;
    private double rating;
    private String imagePath;
    private String totalImagePath;
    private String description;
    private String title;
    private String release_date;

    public Movie(){
        super();
    }

    protected Movie(Parcel in) {
        id = in.readLong();
        rating = in.readDouble();
        imagePath = in.readString();
        totalImagePath = in.readString();
        description = in.readString();
        title = in.readString();
        release_date = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    /**
     * getter method for the id
     * @return the id of the movie in the TMDB
     */
    public long getId() {
        return id;
    }

    /**
     * setter method of the id
     * @param id the id of the movie
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getter method for the rating
     * @return the user-rating of the movie
     */
    public double getRating() {
        return rating;
    }

    /**
     * setter method for the movie rating
     * @param rating rating of the movie based on data of TMDB
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * getter method for gaining the end URL for using it to build the total path to the image
     * @return the ending part url of the image for the movie
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * setter method for the end url of the image for the movie
     * @param imagePath the end url of the image based on the TMDB API
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * getter method for the description
     * @return returns the overview/description of the movie
     */
    public String getDescription() {
        return description;
    }

    /**
     * setter method of the description
     * @param description the description of the movie in the TMDB
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * getter method for the title of the movie
     * @return the title of the movie
     */
    public String getTitle() {
        return title;
    }

    /**
     * setter method for setting the title
     * @param title the title of the movie
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * getter method for getting the total URL of the image
     * @return the url of the image as string representation
     */
    public String getTotalImagePath() {
        return totalImagePath;
    }

    /**
     * setter method for building the total url of the image
     * @param totalImagePath the total image path based on imageURL and TMBD-api-configuration
     */
    public void setTotalImagePath(String totalImagePath) {
        this.totalImagePath = totalImagePath;
    }

    public void setRelease_date(String date){
        this.release_date=date;
    }

    public String getRelease_date() {
        return release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeDouble(rating);
        parcel.writeString(title);
        parcel.writeString(totalImagePath);
        parcel.writeString(description);
        parcel.writeString(imagePath);
        parcel.writeString(release_date);
    }
}
