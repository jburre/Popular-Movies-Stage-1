package com.example.lyz.utils;

import com.example.lyz.entities.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lyz on 28.11.2017.
 */

public class JsonUtils {
    private static final String RESULT="results";
    private static final String MOVIE_ID="id";
    private static final String TITLE="title";
    private static final String RATING="vote_average";
    private static final String DESCRIPTION="overview";
    private static final String POSTER_PATH="poster_path";

    private static final String IMAGE_RESULT="images";
    private static final String IMAGE_BASEURL="base_url";
    private static final String POSTER_SIZES="poster_sizes";

    public static Movie[]getMovieDatafromJsonString(String jsonString) throws JSONException {
        JSONArray resultArray=null;
        JSONObject totalMovieJson = new JSONObject(jsonString);
        resultArray=totalMovieJson.getJSONArray(RESULT);
        return (parseJsonArrayToMovies(resultArray));
    }

    private static Movie[] parseJsonArrayToMovies(JSONArray resultArray) throws JSONException {
        Movie[] movies = null;
        if (resultArray!=null&&resultArray.length()>0){
            movies=new Movie[resultArray.length()];
            for (int i=0;i<resultArray.length();i++){
                JSONObject result= (JSONObject) resultArray.get(i);
                Movie movie = new Movie();
                movie.setId(Long.parseLong( result.getString(MOVIE_ID)));
                movie.setDescription(result.getString(DESCRIPTION));
                movie.setRating(result.getDouble(RATING));
                movie.setTitle(result.getString(TITLE));
                movie.setImagePath(result.getString(POSTER_PATH));
                movies[i]=movie;
            }
        }
        return movies;
    }

    public static Movie getMovieDetailDatafromJsonString(String response) throws JSONException {
        Movie movie=null;
        if (response !=null&&!response.equals("")){
            movie=new Movie();
            JSONObject result = new JSONObject(response);
            movie.setId(Long.valueOf(result.getInt("id")));
            movie.setTitle(result.getString(TITLE));
            movie.setDescription(result.getString(DESCRIPTION));
            movie.setRating(result.getDouble(RATING));
            movie.setImagePath(result.getString(POSTER_PATH));
        }
        return movie;
    }

    public static String[] getConfigfromJsonString(String response) throws JSONException{
        String [] result = new String[2];
        JSONObject config;
        if (response!=null||response.equals("")){
            config=new JSONObject(response);
            JSONObject images=config.getJSONObject(IMAGE_RESULT);
            result[0]=images.getString(IMAGE_BASEURL);
            JSONArray sizes =images.getJSONArray(POSTER_SIZES);
            result[1]=sizes.getString(2);
        }
        return result;
    }


}
