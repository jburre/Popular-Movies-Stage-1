package com.example.lyz.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyz.asyncTasks.AsyncTaskCompleteListener;
import com.example.lyz.asyncTasks.FetchMovieDataTask;
import com.example.lyz.entities.Movie;
import com.example.lyz.utils.ImagePathHelper;
import com.example.lyz.utils.JsonUtils;
import com.example.lyz.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * {@Link MainActivity} class for the main activity of the app
 */
public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler{

    private ProgressBar mProgressBar;
    private TextView mErrorView;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    //https://stackoverflow.com/questions/28236390/recyclerview-store-restore-state-between-activities
    private static final String LAYOUT_STATE= "LAYOUT_STATE";
    private static Bundle mState;

    /**
     * method for creating the activity
     * @param savedInstanceState the saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar= (ProgressBar)findViewById(R.id.pg_main);
        mErrorView=(TextView)findViewById(R.id.main_tv_error);
        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerview_movie);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager;
            layoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(layoutManager);
            mMovieAdapter=new MovieAdapter(this);
            mRecyclerView.setAdapter(mMovieAdapter);
            try{
                loadMovieData(getString(R.string.sortOrderTopRated));
            } catch (Exception e){
                Toast.makeText(this,R.string.main_loading_error,Toast.LENGTH_LONG);
            }
    }

    /**
     * helper method for execting the AsyncTask
     * @param sortOrder
     */
    private void loadMovieData(String sortOrder){

        //Network check based on https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected=activeNetwork!=null&&activeNetwork.isConnectedOrConnecting();
        if (isConnected==true){
            new FetchMovieDataTask(this, new FetchMovieDataTaskListener()).execute(sortOrder);
        }
        else {
            showErrorMessage();
        }
    }

    /**
     * onClick method for responding to clicks of the user and leading to a detailed activity
     * @param id the id of the corresponding movie which is needed for the detailactivity
     */
    @Override
    public void onClick(long id) {
        Context context = this;
        Class destination = DetailsActivity.class;
        Intent startIntent= new Intent(context, destination);
        startIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(id));
        startActivity(startIntent);
    }

    /**
     * An extension of the AsyncTask class for getting the data from the website
     */
    private class FetchMovieDataTaskListener implements AsyncTaskCompleteListener<Movie[]> {
        @Override
        public void onPreExecuting() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTaskComplete(Movie[] movies) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (movies!=null){
                showMoviePictures();
                mMovieAdapter.setMovies(movies);
            } else {
                showErrorMessage();
            }
        }
    }

    /**
     * helper method if something went wrong
     */
    private void showErrorMessage(){
        this.mErrorView.setVisibility(View.VISIBLE);
    }

    /**
     * method for showing up the pictures
     */
    private void showMoviePictures(){
        this.mErrorView.setVisibility(View.INVISIBLE);
        this.mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * method for responding to menu item clicks
     * @param item the menu item clicked
     * @return true if the item responds to id
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_item_mostPopular){
            loadMovieData(getString(R.string.sortOrderPopular));
            return true;
    } else if (item.getItemId()==R.id.menu_item_topRated){
            loadMovieData(getString(R.string.sortOrderTopRated));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mState=new Bundle();
        Parcelable currentState=mRecyclerView.getLayoutManager().onSaveInstanceState();
        mState.putParcelable(LAYOUT_STATE,currentState);
    }

    //TODO Bug: Rotation leads to old sort order instead of new one

    @Override
    protected void onResume() {
        super.onResume();
        if (mState!=null){
            Parcelable oldState = mState.getParcelable(LAYOUT_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(oldState);
        }
    }

    /**
     * method for creating a menu
     * @param menu the menu that wants to be created
     * @return the super method of onCreateOptionsMenu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
