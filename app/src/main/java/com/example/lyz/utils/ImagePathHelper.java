package com.example.lyz.utils;

import com.example.lyz.entities.Movie;

/**
 * Created by Lyz on 30.11.2017.
 */

public class ImagePathHelper {

    public static Movie[] builtTotalImagePaths(Movie[] movies, String[] configs) {
        if (movies.length!=0&&configs.length!=0){
            for(int i=0;i<movies.length;i++){
                movies[i].setTotalImagePath(configs[0]+configs[1]+movies[i].getImagePath());
            }
        }
        return movies;
    }

    public static Movie builtTotalImagePaths(Movie movie, String[] configs) {
        movie.setTotalImagePath(configs[0]+configs[1]+movie.getImagePath());
        return movie;
    }
}
