package com.example.lyz.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import com.example.lyz.utils.JsonUtils;
import com.example.lyz.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lyz on 29.12.2017.
 */

public class FetchMovieTrailerTask extends AsyncTask <String, Void, String[]>{

    private Context context;

    private final String TAG = FetchMovieTrailerTask.class.getSimpleName();
    private AsyncTaskCompleteListener listener;

    public FetchMovieTrailerTask(Context context, AsyncTaskCompleteListener listener){
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
        URL movieTrailerURL = NetworkUtils.builtMovieTrailerURL(movieId,context);
        String response=null;
        String[] keys=null;
        try{
            response = NetworkUtils.getNetworkResponse(movieTrailerURL);
            keys = JsonUtils.getMovieTrailerDataFromJsonString(response);
            return keys;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String[] keys) {
        super.onPostExecute(keys);
        listener.onTaskComplete(keys);
    }
}
