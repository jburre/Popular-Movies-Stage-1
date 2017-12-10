package com.example.lyz.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import com.example.lyz.popularmoviesstage1.BuildConfig;
import com.example.lyz.popularmoviesstage1.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Lyz on 27.11.2017.
 */

public final class NetworkUtils {

    private static String basequery;
    private final static String queryParam="api_key";
    private static String baseDetailsQuery;

    /**
     * method for generating the URL for querying the movies
     * @param desiredSorting the desired sort criteria currently only topRated and most popular are featured
     * @param context the context of the application
     * @return the URL for the movies based on the search criteria
     */
    public static URL builtMovieUrl(String desiredSorting, Context context){
        Resources resources = context.getResources();
        URL url = null;
        if(desiredSorting.equals("topRated")){
            basequery=resources.getString(R.string.baseQueryTopRatedMovies);
        } else {
            basequery=resources.getString(R.string.baseQueryPopularMovies);
        }
        Uri uri = Uri.parse(basequery).buildUpon()
                .appendQueryParameter(queryParam, BuildConfig.THE_MOVIE_DB_API_TOKEN)
                .build();
        try {
            url=new URL(uri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    /**
     * method for generating the URL of the movie details endpoint of the TMDB API
     * @param id the id of the movie the details wanted
     * @param context the context of the application
     * @return the URL for the detailed movie endpoint of the TMDB API
     */
    public static URL builtMovieDetailsUrl(String id, Context context){
        Resources resources= context.getResources();
        URL url = null;
        baseDetailsQuery=resources.getString(R.string.baseQueryMovieDetails);
        baseDetailsQuery+=id;
        Uri uri = Uri.parse(baseDetailsQuery).buildUpon()
                .appendQueryParameter(queryParam,BuildConfig.THE_MOVIE_DB_API_TOKEN)
                .build();
        try{
            url=new URL(uri.toString());
        } catch(MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    /**
     * method for creating the url of the configuration API point of the TMDB
     * @param context the context of the application for getting access to resources
     * @return the url of the configuration API endpoint
     */
    public static URL builtConfigURL(Context context) {
        URL url = null;
        basequery=context.getResources().getString(R.string.configBaseQuery);
        Uri uri = Uri.parse(basequery).buildUpon()
                .appendQueryParameter(queryParam,BuildConfig.THE_MOVIE_DB_API_TOKEN)
                .build();
        try{
            url = new URL(uri.toString());
        } catch(MalformedURLException e){
            e.printStackTrace();
        }
        return url;

    }

    /**
     * method as presented in the udacity course part one
     * @param url the url the connection needs to be established
     * @return a string representation of the data the connection retrieves
     * @throws IOException an exception if there was not data connection or the data could not be retrieved
     */
    public static String getNetworkResponse(URL url)throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


}
