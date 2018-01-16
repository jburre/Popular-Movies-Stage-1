package com.example.lyz.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lyz on 29.12.2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder>{
    private String[] keys;

    private final TrailerOnClickHandler mClickHandler;
    private Context context;

    public TrailerAdapter(TrailerOnClickHandler clickHandler){
        this.mClickHandler=clickHandler;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
        notifyDataSetChanged();
    }

    public String[] getKeys() {
        return keys;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context=parent.getContext();
        int layoutIdForTrailer = R.layout.movietrailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately=false;
        View view = inflater.inflate(layoutIdForTrailer,parent, shouldAttachToParentImmediately);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        String key = keys[position];
        TextView textView = holder.mTrailerTextView;
        position=position+1;
        textView.append(" "+position);
    }

    @Override
    public int getItemCount() {
        if (keys==null){
            return 0;
        }
        return keys.length;
    }

    public interface TrailerOnClickHandler {
        void onClick(String key);
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mTrailerTextView;

        public TrailerAdapterViewHolder(View view) {
            super(view);
            this.mTrailerTextView = (TextView)view.findViewById(R.id.movieTrailer);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            String key = keys[position];
            mClickHandler.onClick(key);
        }
    }
}
