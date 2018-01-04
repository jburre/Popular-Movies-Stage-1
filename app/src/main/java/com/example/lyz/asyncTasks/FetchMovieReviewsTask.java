package com.example.lyz.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.lyz.utils.JsonUtils;
import com.example.lyz.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Lyz on 30.12.2017.
 */

public class FetchMovieReviewsTask extends AsyncTask<String, Void, String[]>{

    private Context context;
    private final String TAG = FetchMovieReviewsTask.class.getSimpleName();
    private AsyncTaskCompleteListener listener;

    public FetchMovieReviewsTask(Context context, AsyncTaskCompleteListener listener){
        this.context=context;
        this.listener=listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onPreExecuting();
    }

    @Override
    protected String[] doInBackground(String... strings) {
        if (strings.length==0){
            return null;
        }
        String movieId=strings[0];
        URL movieReviewURL = NetworkUtils.builtMovieReviewsURL(movieId, context);
        String response = null;
        String [] reviews;
        try {
            response = NetworkUtils.getNetworkResponse(movieReviewURL);
            reviews = JsonUtils.getMovieReviewsFromJsonString(response);
            return reviews;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
        catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);
        listener.onTaskComplete(strings);
    }
}
