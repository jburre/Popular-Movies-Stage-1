package com.example.lyz.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Lyz on 30.12.2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder>{

    private String[]reviews;
    private Context context;

    public ReviewsAdapter(){
        super();
    }

    @Override
    public ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context=parent.getContext();
        int layoutId=R.layout.moviereview;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately=false;
        View view = inflater.inflate(layoutId,parent, shouldAttachToParentImmediately);
        return new ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewsAdapterViewHolder holder, int position) {
        String review = reviews[position];
        TextView textView = holder.mReviewTextView;
        textView.setText(review);
    }

    @Override
    public int getItemCount() {
        if (reviews==null){
            return 0;
        } else {
            return reviews.length;
        }
    }

    public void setReviews(String[] reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView mReviewTextView;

        public ReviewsAdapterViewHolder(View view) {
            super(view);
            this.mReviewTextView=(TextView)view.findViewById(R.id.movieReviewTextView);
        }
    }

}
