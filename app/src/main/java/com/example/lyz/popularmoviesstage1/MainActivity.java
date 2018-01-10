package com.example.lyz.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    private static final int MOVIE_LOADER_ID=0;
    private static final String TAG = MainActivity.class.getSimpleName();

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

    private void loadMovieDataFromDatabase(MainActivity mainActivity) {
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
        mMovieAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
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

    private void showLoadingScreen(){
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.INVISIBLE);
    }

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
            loadMovieDataFromDatabase(this);
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
