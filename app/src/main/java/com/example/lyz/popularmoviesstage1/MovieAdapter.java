package com.example.lyz.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lyz.entities.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by Lyz on 02.12.2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private Movie[] movies;

    private final MovieAdapterOnClickHandler mClickHandler;
    private Context context;

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        int layoutIdForPoster= R.layout.movie_poster;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately=false;
        View view = inflater.inflate(layoutIdForPoster, parent, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        String imagePath=movies[position].getTotalImagePath();
        Picasso.with(this.context).load(imagePath).into(holder.moviePoster);
    }

    @Override
    public int getItemCount() {
        if (movies==null){
            return 0;
        }
        return movies.length;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(long id);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler){
        mClickHandler=clickHandler;
    }

    public void setMovies(Movie[] movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView moviePoster;

        public MovieAdapterViewHolder(View view){
            super(view);
            moviePoster=(ImageView)view.findViewById(R.id.movie_poster_image);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            int position=getAdapterPosition();
            long id = movies[position].getId();
            mClickHandler.onClick(id);
        }
    }

}
