package com.example.lyz.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyz.entities.Movie;
import com.example.lyz.utils.ImagePathHelper;
import com.example.lyz.utils.JsonUtils;
import com.example.lyz.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;


public class detailsActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private TextView mTitle;
    private TextView mRating;
    private TextView mSummary;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mProgressBar=(ProgressBar)findViewById(R.id.pg_detail);
        mTitle=(TextView)findViewById(R.id.detail_title_content);
        mRating=(TextView)findViewById(R.id.detail_rating_content);
        mSummary=(TextView)findViewById(R.id.detail_summary_content);
        mImageView=(ImageView)findViewById(R.id.detail_image_view);

        Intent intentThatStartedThisActivity=getIntent();
        if (intentThatStartedThisActivity!=null){
            if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)){
                String id = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                loadMovieDetails(id);
            }
        }
    }

    private void loadMovieDetails(String id) {
        new FetchMovieDetailsTask(this).execute(id);
    }

    public class FetchMovieDetailsTask extends AsyncTask<String, Void, Movie> {

        private Context context;
        private final String TAG = MainActivity.FetchMovieDataTask.class.getSimpleName();

        public FetchMovieDetailsTask(Context context){
            this.context=context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie doInBackground(String... strings) {
            if (strings.length==0){
                return null;
            }
                String id = strings[0];
                URL detailMovieURL = NetworkUtils.builtMovieDetailsUrl(id, this.context);
                URL configURL= NetworkUtils.builtConfigURL(context);
                String response=null;
                Movie movie=null;
                String[]configs=new String[2];
                try {
                    response = NetworkUtils.getNetworkResponse(detailMovieURL);
                    movie = JsonUtils.getMovieDetailDatafromJsonString(response);
                    response=NetworkUtils.getNetworkResponse(configURL);
                    configs=JsonUtils.getConfigfromJsonString(response);
                    movie= ImagePathHelper.builtTotalImagePaths(movie,configs);
                    Log.d(TAG,response);
                    return movie;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
        }

        @Override
        protected void onPostExecute(Movie movie) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (movie!=null){
                mTitle.setText(movie.getTitle());
                mRating.setText(String.valueOf(movie.getRating()));
                mSummary.setText(movie.getDescription());
                Picasso.with(this.context).load(movie.getTotalImagePath()).into(mImageView);

            } else {
                showErrorMessage();
            }
        }
    }
    public void showErrorMessage(){
        Toast.makeText(this,"An error occured while trying to lad the page",Toast.LENGTH_LONG);
    }
}
