package com.example.lyz.popularmoviesstage1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.lyz.asyncTasks.AsyncTaskCompleteListener;
import com.example.lyz.asyncTasks.FetchMovieDataTask;
import com.example.lyz.asyncTasks.FetchMovieReviewsTask;
import com.example.lyz.asyncTasks.FetchMovieTrailerTask;
import com.example.lyz.data.FavoriteMovieContract;
import com.example.lyz.entities.Movie;
import com.example.lyz.utils.ImagePathHelper;
import com.example.lyz.utils.JsonUtils;
import com.example.lyz.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity implements TrailerAdapter.TrailerOnClickHandler{

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

    private RecyclerView mTrailerView;
    private TrailerAdapter mTrailerAdapter;

    private RecyclerView mReviewsView;
    private ReviewsAdapter mReviewsAdapter;
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager reviewLayoutManager;
    private Movie movie;

    private static final String LIFECYCLE_MOVIE="movie";
    private static final String LIFECYCLE_TRAILER_CONTENT="trailer content";
    private static final String LIFECYCLE_REVIEWS="reviews";

    /**
     * creator method
     * @param savedInstanceState the sved state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout();
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected=activeNetwork!=null&&activeNetwork.isConnectedOrConnecting();

        if (savedInstanceState!=null){
            if (savedInstanceState.containsKey(LIFECYCLE_MOVIE)
                    && savedInstanceState.containsKey(LIFECYCLE_TRAILER_CONTENT)
            && savedInstanceState.containsKey(LIFECYCLE_REVIEWS)){
                restoreDetailActivity(savedInstanceState);
                mTitle.setText(movie.getTitle());
                mRating.setText(String.valueOf(movie.getRating()));
                mSummary.setText(movie.getDescription());
                mReleaseDate.setText(movie.getReleaseDate());
                if (isConnected==true){
                    mProgressBar.setVisibility(View.VISIBLE);
                    loadMovieImage(movie);
                    mProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    showErrorMessage();
                }
            }
        } else {
            Intent intentThatStartedThisActivity=getIntent();
            if (intentThatStartedThisActivity!=null){
                if(intentThatStartedThisActivity.hasExtra("movie")){
                    this.movie = intentThatStartedThisActivity.getParcelableExtra("movie");
                    mTitle.setText(movie.getTitle());
                    mRating.setText(String.valueOf(movie.getRating()));
                    mSummary.setText(movie.getDescription());
                    mReleaseDate.setText(movie.getReleaseDate());
                    if (isConnected==true){
                        mProgressBar.setVisibility(View.VISIBLE);
                        loadMovieImage(movie);
                        loadTrailer(movie.getId());
                        loadReviews(movie.getId());
                        mProgressBar.setVisibility(View.INVISIBLE);
                    } else {
                    showErrorMessage();
                    }
                }
            } else {
                showErrorMessage();
            }
        }
    }

    /**
     * method to help setup the layout
     */
    private void setupLayout() {
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
        mTrailerView =(RecyclerView)findViewById(R.id.recyclerview_trailer);
        layoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mTrailerAdapter= new TrailerAdapter(this);
        mTrailerView.setLayoutManager(layoutManager);
        mTrailerView.setAdapter(mTrailerAdapter);

        mReviewsView= (RecyclerView)findViewById(R.id.recyclerview_reviews);

        reviewLayoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mReviewsView.setLayoutManager(reviewLayoutManager);
        mReviewsAdapter=new ReviewsAdapter();
        mReviewsView.setAdapter(mReviewsAdapter);
    }

    /**
     * method to restore the contents of the details activity
     * @param savedInstanceState the saved state of the activity
     */
    private void restoreDetailActivity(Bundle savedInstanceState) {
        this.movie=savedInstanceState.getParcelable(LIFECYCLE_MOVIE);
        this.mTrailerAdapter.setKeys(savedInstanceState.getStringArray(LIFECYCLE_TRAILER_CONTENT));
        this.mReviewsAdapter.setReviews(savedInstanceState.getStringArray(LIFECYCLE_REVIEWS));
    }

    /**
     * saves the state during the android lifecycle
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIFECYCLE_MOVIE,this.movie);
        outState.putStringArray(LIFECYCLE_TRAILER_CONTENT, mTrailerAdapter.getKeys());
        outState.putStringArray(LIFECYCLE_REVIEWS,mReviewsAdapter.getReviews());
    }

    /**
     * helper method to retrieve the reviews for the movie
     * @param id the movie id based on the api
     */
    private void loadReviews(long id) {
        new FetchMovieReviewsTask(this, new FetchMovieReviewsTaskListener()).execute(String.valueOf(id));
    }

    /**
     * helper method to retrieve the trailer for the movie
     * @param id the movie id based on the api
     */
    private void loadTrailer(long id) {
        new FetchMovieTrailerTask(this, new FetchMovieTrailerTaskListener()).execute(String.valueOf(id));
    }

    private void loadMovieImage(Movie movie) {
        Glide.with(this).load(movie.getTotalImagePath())
                .priority(Priority.IMMEDIATE)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_launcher_foreground)
                .dontAnimate()
                .into(mImageView);
    }

    /**
     * method to show an error if something has happened
     */
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

    @Override
    public void onClick(String key) {
        Uri youtubeUri = Uri.parse(getString(R.string.baseYoutubeURL)+getString(R.string.youtubeQueryParam)+key).buildUpon().build();
        Intent intent = new Intent(Intent.ACTION_VIEW, youtubeUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private class FetchMovieTrailerTaskListener implements AsyncTaskCompleteListener <String[]>{

        @Override
        public void onPreExecuting() {
            //nothing to do
        }

        @Override
        public void onTaskComplete(String[] result) {
            if (result.length!=0){
                    mTrailerAdapter.setKeys(result);
            }
        }
    }

    /**
     * helper class to retrieve data from the api
     */
    private class FetchMovieReviewsTaskListener implements AsyncTaskCompleteListener <String[]>{

        @Override
        public void onPreExecuting() {
            //nothing to do since both tasks are already wrapped in a loading
        }

        /**
         * implementation of the AsyncCompletelistener. Binds the results to the adapter
         * @param result
         */
        @Override
        public void onTaskComplete(String[] result) {
            if (result==null||result.length==0){
                //showErrorMessage();
            } else{
                mReviewsAdapter.setReviews(result);
            }
        }

    }

    /**
     * adds a movie to the database
     * @param view
     */
    public void onFavoriteSelected(View view){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIEID, String.valueOf(movie.getId()));
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_DESCRIPTION,movie.getDescription());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RATING, String.valueOf(movie.getRating()));
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_IMAGEPATH, movie.getImagePath());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TOTALIMAGEPATH, movie.getTotalImagePath());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASEDATE,movie.getReleaseDate());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE, movie.getTitle());
        Uri uri = getContentResolver().insert(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,contentValues);
        if (uri!=null){
            Toast.makeText(getBaseContext(), getString(R.string.favoriteResponse), Toast.LENGTH_LONG).show();
        }
    }
}
