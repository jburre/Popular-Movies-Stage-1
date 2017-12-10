package com.example.lyz.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

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
