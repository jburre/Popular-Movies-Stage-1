package com.example.lyz.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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
import com.example.lyz.data.FavoriteMovieContract;
import com.example.lyz.entities.Movie;

/**
 * {@Link MainActivity} class for the main activity of the app
 */
public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor>{

    private ProgressBar mProgressBar;
    private TextView mErrorView;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private GridLayoutManager layoutManager;
    private static final int MOVIE_LOADER_ID=0;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String LIFECYCLE_CALLBACKS="onSaveInstanceState";
    private static final String LIFECYCLE_MOVIES="movies";

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
        mMovieAdapter=new MovieAdapter(this);
        layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mMovieAdapter);
        if (savedInstanceState!=null){
            if (savedInstanceState.containsKey(LIFECYCLE_CALLBACKS)&&savedInstanceState.containsKey(LIFECYCLE_MOVIES)){
                restorePreviousState(savedInstanceState);
            }
        } else {
            try{
                loadMovieData(getString(R.string.sortOrderTopRated));
            } catch (Exception e){
                Toast.makeText(this,R.string.main_loading_error,Toast.LENGTH_LONG);
            }
        }
    }

    /**
     * method to save the state of the activity during the android lifecycle
     * @param outState the state that will hold the state of the activity
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable state= layoutManager.onSaveInstanceState();
        outState.putParcelable(LIFECYCLE_CALLBACKS,state);
        outState.putParcelableArray(LIFECYCLE_MOVIES,mMovieAdapter.getMovies());
    }

    /**
     * method to restore the previous state of the activity
     * @param savedInstanceState the saved state of the activity
     */
    private void restorePreviousState(Bundle savedInstanceState) {
        Parcelable state = savedInstanceState.getParcelable(LIFECYCLE_CALLBACKS);
        mRecyclerView.getLayoutManager().onRestoreInstanceState(state);
        Parcelable[]movies = savedInstanceState.getParcelableArray(LIFECYCLE_MOVIES);
        mMovieAdapter.setMovies(movies);
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
     * helper method to initialize the loading from the content provider
     */
    private void loadMovieDataFromDatabase() {
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID,null,this);
    }

    /**
     * onClick method for responding to clicks of the user and leading to a detailed activity
     * @param movie the id of the corresponding movie which is needed for the detailactivity
     */
    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destination = DetailsActivity.class;
        Intent startIntent= new Intent(context, destination);
        startIntent.putExtra("movie", movie);
        startActivity(startIntent);
    }

    /**
     * creation method for loader
     * @param id
     * @param bundle current used bundle
     * @return a new AsyncTaskLoader
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new AsyncTaskLoader<Cursor>(this){

            Cursor mMovieData=null;

            @Override
            protected void onStartLoading() {
                showLoadingScreen();
                if (mMovieData!=null){
                    deliverResult(mMovieData);
                } else{
                    forceLoad();
                }
            }
            @Override
            public Cursor loadInBackground() {
                try {
                    String[] projection=new String[]{FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASEDATE,
                            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RATING, FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE,
                            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_DESCRIPTION, FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TOTALIMAGEPATH,
                            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIEID, FavoriteMovieContract.FavoriteMovieEntry.COLUMN_IMAGEPATH
                            };
                        return getContentResolver().query(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,projection, null, null, null);
                    } catch (Exception e){
                        Log.e(TAG, "Failed to retrieve data from database");
                        e.printStackTrace();
                        return null;
                }
            }

            @Override
            public void deliverResult(Cursor data) {
                mMovieData=data;
                super.deliverResult(data);
            }
        };
    }
    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if (mMovieAdapter.loadDataFromCursor(data)){
            showMoviePictures();
        } else {
            showErrorMessage();
        }
    }

    /**
     * method to reset the cursor
     * @param loader current used loader in the class
     */
    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mMovieAdapter.loadDataFromCursor(null);
    }

    /**
     * An extension of the AsyncTask class for getting the data from the website
     */
    private class FetchMovieDataTaskListener implements AsyncTaskCompleteListener<Movie[]> {
        @Override
        public void onPreExecuting() {
            showLoadingScreen();
        }

        @Override
        public void onTaskComplete(Movie[] movies) {
            endLoadingScreen();
            if (movies!=null){
                showMoviePictures();
                mMovieAdapter.setMovies(movies);
            } else {
                showErrorMessage();
            }
        }
    }

    /**
     * helper method to show the loading of the activity
     */
    private void showLoadingScreen(){
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.INVISIBLE);
    }

    /**
     * helper method to end progressbar and errorview
     */
    private void endLoadingScreen(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorView.setVisibility(View.INVISIBLE);
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
        this.mProgressBar.setVisibility(View.INVISIBLE);
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
        }
        else if (item.getItemId()==R.id.menu_item_favorites){
            loadMovieDataFromDatabase();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
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
