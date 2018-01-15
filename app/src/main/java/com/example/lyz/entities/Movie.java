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

    private String releaseDate;

    /**
     * basic constructor
     */
    public Movie(){
        super();
    }

    /**
     * implemented parcelable method
     * @param in an Parcel that needs to be parsed
     */
    protected Movie(Parcel in) {
        id = in.readLong();
        rating = in.readDouble();
        title = in.readString();
        totalImagePath = in.readString();
        description = in.readString();
        imagePath = in.readString();
        releaseDate = in.readString();
    }

    /**
     * necessary CREATOR for Parcelable
     */
    public static final Creator CREATOR = new Creator<Movie>() {
        /**
         * creates a Movie by a parcel
         * @param in
         * @return a movie objcet
         */
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        /**
         * creator method for movie array
         * @param size desired size
         * @return a movie array
         */
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * content describer
      * @return currently 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * parcel writer method
     * @param parcel the parcel by the movie will be created
     * @param i unhandled
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeDouble(this.rating);
        parcel.writeString(this.title);
        parcel.writeString(this.totalImagePath);
        parcel.writeString(this.description);
        parcel.writeString(this.imagePath);
        parcel.writeString(this.releaseDate);
    }

    /**
     * generic toString method for debugging purpose currently
     * @return a string representation of the movie object
     */
    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", rating=" + rating +
                ", imagePath='" + imagePath + '\'' +
                ", totalImagePath='" + totalImagePath + '\'' +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", release_date='" + releaseDate + '\'' +
                '}';
    }
}
