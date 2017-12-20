package com.example.lyz.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.example.lyz.entities.Movie;
import com.example.lyz.utils.ImagePathHelper;
import com.example.lyz.utils.JsonUtils;
import com.example.lyz.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Lyz on 20.12.2017.
 */

public class FetchMovieDataTask extends AsyncTask<String, Void, Movie[]>{
    private Context context;
    private final String TAG = FetchMovieDataTask.class.getSimpleName();
    private AsyncTaskCompleteListener<Movie[]> listener;


    public FetchMovieDataTask(Context context, AsyncTaskCompleteListener listener){
        this.listener=listener;
        this.context=context;
    }

    /**
     * method for executing before loading data
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onPreExecuting();
    }

    /**
     * method for loading data from the TMDB website
     * @param strings the wanted sortorder, currently only toprated or mostpopular
     * @return an array of movies based on the sort criteria
     */
    @Override
    protected Movie[] doInBackground(String... strings) {
        if (strings.length==0){
            return null;
        }
        String sortOrder=strings[0];
        URL movieUrl = NetworkUtils.builtMovieUrl(sortOrder, context);
        URL configURL= NetworkUtils.builtConfigURL(context);
        String response=null;
        String[]configs=new String[2];
        Movie[] movies=null;
        try {
            response = NetworkUtils.getNetworkResponse(movieUrl);
            movies = JsonUtils.getMovieDatafromJsonString(response);
            response=NetworkUtils.getNetworkResponse(configURL);
            configs=JsonUtils.getConfigfromJsonString(response);
            movies= ImagePathHelper.builtTotalImagePaths(movies,configs);
            Log.d(TAG,response);
            return movies;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * method for handling the data after achieving it from the website
     * @param movies the movie array based on the data retrieved
     */
    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);
        listener.onTaskComplete(movies);
    }
}

