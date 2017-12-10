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

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler{

    private ProgressBar mProgressBar;
    private TextView mErrorView;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

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

        try{
            loadMovieData(getString(R.string.sortOrderTopRated));
        } catch (Exception e){
            Toast.makeText(this,R.string.main_loading_error,Toast.LENGTH_LONG);
        }

    }

    private void loadMovieData(String sortOrder){
        new FetchMovieDataTask(this).execute(sortOrder);
    }

    @Override
    public void onClick(long id) {
        Context context = this;
        Class destination = detailsActivity.class;
        Intent startIntent= new Intent(context, destination);
        startIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(id));
        startActivity(startIntent);
    }

    public class FetchMovieDataTask extends AsyncTask<String, Void, Movie[]> {

        private Context context;
        private final String TAG = FetchMovieDataTask.class.getSimpleName();

        public FetchMovieDataTask(Context context){
            this.context=context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

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

        @Override
        protected void onPostExecute(Movie[] movies) {
            mProgressBar.setVisibility(View.INVISIBLE);
           if (movies!=null){
               mMovieAdapter.setMovies(movies);
               mRecyclerView.setAdapter(mMovieAdapter);
               showMoviePictures();

           } else {
               showErrorMessage();
           }
        }
    }

    private void showErrorMessage(){
        this.mErrorView.setVisibility(View.VISIBLE);
    }

    private void showMoviePictures(){
        this.mErrorView.setVisibility(View.INVISIBLE);
        this.mRecyclerView.setVisibility(View.VISIBLE);
    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
