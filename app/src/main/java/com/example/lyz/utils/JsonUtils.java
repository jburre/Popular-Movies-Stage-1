package com.example.lyz.utils;

import com.example.lyz.entities.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Lyz on 28.11.2017.
 */

/**
 * {@JsonUtils} helper class for parsing the data received as json
 */
public class JsonUtils {
    private static final String RESULT="results";
    private static final String MOVIE_ID="id";
    private static final String TITLE="title";
    private static final String RATING="vote_average";
    private static final String DESCRIPTION="overview";
    private static final String POSTER_PATH="poster_path";
    private static final String RELEASE="release_date";

    private static final String IMAGE_RESULT="images";
    private static final String IMAGE_BASEURL="base_url";
    private static final String POSTER_SIZES="poster_sizes";


    /**
     * method for parsing the data as an array
     * @param jsonString the json string retrieved by the network
     * @return an array of movies based on the json string
     * @throws JSONException exception
     */
    public static Movie[]getMovieDatafromJsonString(String jsonString) throws JSONException {
        JSONArray resultArray=null;
        JSONObject totalMovieJson = new JSONObject(jsonString);
        resultArray=totalMovieJson.getJSONArray(RESULT);
        return (parseJsonArrayToMovies(resultArray));
    }

    /**
     * helper method for parsing the json data
     * @param resultArray the array of movies the user wanted based on his criteria
     * @return an array of movies
     * @throws JSONException exception
     */
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
                movie.setReleaseDate(result.getString(RELEASE));
                movies[i]=movie;
            }
        }
        return movies;
    }

    public static ArrayList<String> getMovieTrailerDataFromJsonString(String response) throws JSONException {
        if (response!=null && response.length()!=0){
            JSONObject totalTrailersJson = new JSONObject(response);
            JSONArray resultArray=totalTrailersJson.getJSONArray(RESULT);
            ArrayList<String> keys= new ArrayList<>();
            for (int i=0;i<resultArray.length();i++){
                JSONObject tempMovie = (JSONObject) resultArray.get(i);
                if (("Trailer").equals(tempMovie.getString("type"))&&("YouTube").equals(tempMovie.getString("site"))){
                    keys.add(tempMovie.getString("key"));
                }
            }
            return keys;
        }
        return null;
    }

    /**
     * method for parsing the json data and getting the movie details
     * @param response the json data retrieved by the network
     * @return the movie with detailed information for the detail activity
     * @throws JSONException exception
     */
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
            movie.setReleaseDate(result.getString(RELEASE));
        }
        return movie;
    }

    /**
     * method for parsing the json by the configuration API response
     * @param response the response of the api as json string
     * @return a two-dimensional array holding the base url and the size of the poster
     * @throws JSONException exception
     */
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
