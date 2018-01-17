package com.example.lyz.popularmoviesstage1;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lyz.data.FavoriteMovieContract;
import com.example.lyz.entities.Movie;

/**
 * Created by Lyz on 02.12.2017.
 */

/**
 * {@MovieAdapter} adapter class for handling the recycler view
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private Movie[] movies;

    private final MovieAdapterOnClickHandler mClickHandler;
    private Context context;

    private Cursor mCursor;

    private static final String TAG = MovieAdapter.class.getName();

    /**
     * method for creating the viewholder
     * @param parent the parent ViewGroup
     * @param viewType the desired viewType
     * @return an MovieAdapterViewHolder storing the view
     */
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        int layoutIdForPoster= R.layout.movie_poster;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately=false;
        View view = inflater.inflate(layoutIdForPoster, parent, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    /**
     * binder method for binding data to a viewholder
     * @param holder the holder we want to bind data to
     * @param position the position of the view
     */
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        String imagePath=movies[position].getTotalImagePath();
        ImageView imageView = holder.moviePoster;
        Glide.with(context).load(imagePath)
                .priority(Priority.IMMEDIATE)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_launcher_foreground)
                .dontAnimate()
                .into(imageView);//Picasso.with(this.context).load(imagePath).into(holder.moviePoster);
    }

    /**
     * method for returning the size of data retrieved
     * @return the total amount of movies retrieved by the network
     */
    @Override
    public int getItemCount() {
        if (movies==null){
            return 0;
        }
        return movies.length;
    }

    /**
     * method to change the displayed movies if the database is being called
     * @param cursor cursor that holds the movies requested by query
     * @return true if the change was successful else false
     */
    public boolean loadDataFromCursor(Cursor cursor) {
            if (cursor!=null&&cursor.getCount()>0){
                this.mCursor=cursor;
                Movie []tempMovies = new Movie[mCursor.getCount()];
                int idIndex= mCursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIEID);
                int releaseIndex=mCursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASEDATE);
                int imagePathIndex=mCursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_IMAGEPATH);
                int ratingIndex=mCursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RATING);
                int totalImagePathIndex=mCursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TOTALIMAGEPATH);
                int titleIndex=mCursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE);
                int descriptionIndex=mCursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_DESCRIPTION);
                int movieCount=0;
                if (mCursor.moveToFirst()){
                    do{
                        tempMovies[movieCount]=new Movie();
                        String movieId =mCursor.getString(idIndex);
                        String rating = mCursor.getString(ratingIndex);
                        tempMovies[movieCount].setId(Long.valueOf(movieId));
                        tempMovies[movieCount].setReleaseDate(mCursor.getString(releaseIndex));
                        tempMovies[movieCount].setImagePath(mCursor.getString(imagePathIndex));
                        tempMovies[movieCount].setTotalImagePath(mCursor.getString(totalImagePathIndex));
                        tempMovies[movieCount].setRating(Double.valueOf(rating));
                        tempMovies[movieCount].setTitle(mCursor.getString(titleIndex));
                        tempMovies[movieCount].setDescription(mCursor.getString(descriptionIndex));
                        movieCount++;
                    }while (cursor.moveToNext());
                }
                this.setMovies(tempMovies);
                return true;
            } else {
                return false;
            }
    }

    /**
     * helper method to set the movies based on a parcelable array
     * @param tempMovies the parcelable array
     */
    public void setMovies(Parcelable[] tempMovies) {
        this.movies= (Movie[]) tempMovies;
    }

    /**
     * interface for click handling
     */
    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    /**
     * constructor for movie adapter
     * @param clickHandler the clickhandler handling the clicks for that item
     */
    public MovieAdapter(MovieAdapterOnClickHandler clickHandler){
        mClickHandler=clickHandler;
    }

    /**
     * setter method for populating the views
     * @param movies
     */
    public void setMovies(Movie[] movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    /**
     * {@MovieAdapterViewHolder} inner class extending the recyclerview.viewholder
     */
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView moviePoster;

        /**
         * constructor for the MovieAdapterViewHolder
         * @param view the view we want to populate
         */
        public MovieAdapterViewHolder(View view){
            super(view);
            moviePoster=(ImageView)view.findViewById(R.id.movie_poster_image);
            view.setOnClickListener(this);
        }

        /**
         * OnClick method for handling the clicks on the view
         * @param view the view clicked by the user
         */
        public void onClick(View view) {
            int position=getAdapterPosition();
            Movie movie = movies[position];
            mClickHandler.onClick(movie);
        }
    }

    /**
     * getter method
     * @return returns the movie array
     */
    public Movie[] getMovies() {
        return movies;
    }
}
