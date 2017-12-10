package com.example.lyz.utils;

import com.example.lyz.entities.Movie;

/**
 * Created by Lyz on 30.11.2017.
 */

/**
 *{@ImagePathHelper} helper class for building the image path url
 */
public class ImagePathHelper {

    /**
     * method for building the image path url with the configuration
     * @param movies the movie array we want the whole image path urls for
     * @param configs the configuration of the TMDB API
     * @return the updated movie array
     */
    public static Movie[] builtTotalImagePaths(Movie[] movies, String[] configs) {
        if (movies.length!=0&&configs.length!=0){
            for(int i=0;i<movies.length;i++){
                movies[i].setTotalImagePath(configs[0]+configs[1]+movies[i].getImagePath());
            }
        }
        return movies;
    }

    /**
     * method for building the image path url with the configuration
     * @param movie the movie we want to update the image path url
     * @param configs the configuration of the TMDB API
     * @return the updated movie entity
     */
    public static Movie builtTotalImagePaths(Movie movie, String[] configs) {
        movie.setTotalImagePath(configs[0]+configs[1]+movie.getImagePath());
        return movie;
    }
}
