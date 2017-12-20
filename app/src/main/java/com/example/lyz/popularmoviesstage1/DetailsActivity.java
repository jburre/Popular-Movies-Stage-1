package com.example.lyz.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lyz.asyncTasks.FetchMovieDataTask;
import com.example.lyz.entities.Movie;
import com.example.lyz.utils.ImagePathHelper;
import com.example.lyz.utils.JsonUtils;
import com.example.lyz.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;

public class DetailsActivity extends AppCompatActivity {

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
            if(intentThatStartedThisActivity.hasExtra("movie")){
                Movie movie = intentThatStartedThisActivity.getParcelableExtra("movie");
                Log.d("MOVIE:",movie.toString());
                mTitle.setText(movie.getTitle());
                mRating.setText(String.valueOf(movie.getRating()));
                mSummary.setText(movie.getDescription());
                mReleaseDate.setText(movie.getReleaseDate());
                ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected=activeNetwork!=null&&activeNetwork.isConnectedOrConnecting();
                if (isConnected==true){
                    mProgressBar.setVisibility(View.VISIBLE);
                    Glide.with(this).load(movie.getTotalImagePath())
                            .priority(Priority.IMMEDIATE)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .dontAnimate()
                            .into(mImageView);
                    mProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    showErrorMessage();
                }
            }
            else {
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
