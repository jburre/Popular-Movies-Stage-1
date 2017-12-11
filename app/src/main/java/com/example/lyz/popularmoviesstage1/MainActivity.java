package com.example.lyz.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
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
        new FetchMovieDataTask(this).execute(sortOrder);
    }

    /**
     * onClick method for responding to clicks of the user and leading to a detailed activity
     * @param id the id of the corresponding movie which is needed for the detailactivity
     */
    @Override
    public void onClick(long id) {
        Context context = this;
        Class destination = detailsActivity.class;
        Intent startIntent= new Intent(context, destination);
        startIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(id));
        startActivity(startIntent);
    }

    /**
     * An extension of the AsyncTask class for getting the data from the website
     */
    public class FetchMovieDataTask extends AsyncTask<String, Void, Movie[]> {

        private Context context;
        private final String TAG = FetchMovieDataTask.class.getSimpleName();

        /**
         * constructor for class
         * @param context context of the activity for getting resources
         */
        public FetchMovieDataTask(Context context){
            this.context=context;
        }

        /**
         * method for executing before loading data
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
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
