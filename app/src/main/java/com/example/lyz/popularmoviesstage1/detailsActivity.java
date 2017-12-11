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
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;

public class detailsActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private TextView mTitle;
    private TextView mTitleHeader;
    private TextView mRating;
    private TextView mRatingHeader;
    private TextView mSummary;
    private TextView mSummaryHeader;
    private ImageView mImageView;
    private TextView mReleaseDate;
    private TextView mReleaseDateHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mProgressBar=(ProgressBar)findViewById(R.id.pg_detail);
        mTitle=(TextView)findViewById(R.id.detail_title_content);
        mRating=(TextView)findViewById(R.id.detail_rating_content);
        mSummary=(TextView)findViewById(R.id.detail_summary_content);
        mImageView=(ImageView)findViewById(R.id.detail_image_view);

        mTitleHeader=(TextView)findViewById(R.id.detail_title);
        mRatingHeader=(TextView)findViewById(R.id.detail_rating);
        mSummaryHeader=(TextView)findViewById(R.id.detail_summary);
        mReleaseDate=(TextView)findViewById(R.id.detail_release_date_content);
        mReleaseDateHeader=(TextView)findViewById(R.id.detail_release_date);

        Intent intentThatStartedThisActivity=getIntent();
        if (intentThatStartedThisActivity!=null){
            if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)){
                String id = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                loadMovieDetails(id);
            }
            else {
                showErrorMessage();
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
            mTitle.setVisibility(View.INVISIBLE);
            mRating.setVisibility(View.INVISIBLE);
            mSummary.setVisibility(View.INVISIBLE);
            mImageView.setVisibility(View.INVISIBLE);
            mTitleHeader.setVisibility(View.INVISIBLE);
            mRatingHeader.setVisibility(View.INVISIBLE);
            mSummaryHeader.setVisibility(View.INVISIBLE);
            mReleaseDate.setVisibility(View.INVISIBLE);
            mReleaseDateHeader.setVisibility(View.INVISIBLE);
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
            if (movie!=null){
                mProgressBar.setVisibility(View.INVISIBLE);
                mTitle.setVisibility(View.VISIBLE);
                mRating.setVisibility(View.VISIBLE);
                mSummary.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.VISIBLE);
                mTitleHeader.setVisibility(View.VISIBLE);
                mRatingHeader.setVisibility(View.VISIBLE);
                mSummaryHeader.setVisibility(View.VISIBLE);
                mReleaseDate.setVisibility(View.VISIBLE);
                mReleaseDateHeader.setVisibility(View.VISIBLE);
                mTitle.setText(movie.getTitle());
                mRating.setText(String.valueOf(movie.getRating()));
                mSummary.setText(movie.getDescription());
                mReleaseDate.setText(movie.getRelease_date());
                Picasso.with(this.context).load(movie.getTotalImagePath()).into(mImageView);
            } else {
                showErrorMessage();
            }
        }
    }
    public void showErrorMessage(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mTitle.setVisibility(View.INVISIBLE);
        mRating.setVisibility(View.INVISIBLE);
        mSummary.setVisibility(View.INVISIBLE);
        mImageView.setVisibility(View.INVISIBLE);
        mTitleHeader.setVisibility(View.INVISIBLE);
        mRatingHeader.setVisibility(View.INVISIBLE);
        mSummaryHeader.setVisibility(View.INVISIBLE);
        mReleaseDate.setVisibility(View.INVISIBLE);
        mReleaseDateHeader.setVisibility(View.INVISIBLE);
        Toast.makeText(this,getString(R.string.main_loading_error),Toast.LENGTH_LONG).show();
    }
}
