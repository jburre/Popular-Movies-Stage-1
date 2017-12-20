package com.example.lyz.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.lyz.entities.Movie;
import com.squareup.picasso.Picasso;

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

}
