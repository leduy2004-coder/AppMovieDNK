package com.example.appmoviednk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmoviednk.databinding.CardMovieHomeBinding;
import com.example.appmoviednk.model.MovieModel;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<MovieModel> movieList;
    private Context mContext;

    public CardAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<MovieModel> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout for each card
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardMovieHomeBinding binding = CardMovieHomeBinding.inflate(inflater, parent, false);
        return new CardViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        // Bind data to views
        MovieModel movie = movieList.get(position);
        if(movie == null){
            return;
        }
        holder.cardMovieHomeBinding.tvName.setText(movie.getTenPhim());
        holder.cardMovieHomeBinding.ivImg.setImageResource(movie.getImageResource());
    }

    @Override
    public int getItemCount() {
        if (movieList != null)
            return movieList.size();
        return 0;
    }

    // ViewHolder class
    public class CardViewHolder extends RecyclerView.ViewHolder {
        CardMovieHomeBinding cardMovieHomeBinding;

        public CardViewHolder(@NonNull CardMovieHomeBinding binding) {
            super(binding.getRoot());
            this.cardMovieHomeBinding = binding; // Binding is set here
        }
    }
}
