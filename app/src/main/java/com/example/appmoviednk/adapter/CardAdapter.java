package com.example.appmoviednk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmoviednk.R;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.databinding.CardMovieHomeBinding;
import com.example.appmoviednk.fragment.MovieFragment;
import com.example.appmoviednk.model.MovieModel;
import com.example.appmoviednk.service.MovieService;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<MovieModel> movieList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private MovieModel movieModel;
    MovieService apiService;
    // Interface để lắng nghe sự kiện click item
    public interface OnItemClickListener {
        void onItemClick(MovieModel movie);
    }

    // Phương thức để thiết lập listener từ bên ngoài adapter
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public CardAdapter(Context mContext) {
        this.movieModel = new ViewModelProvider((MainActivity) mContext).get(MovieModel.class);
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
        if (movie == null) {
            return;
        }

        holder.cardMovieHomeBinding.tvName.setText(movie.getTenPhim());
        //Hinh dại dien
        Picasso.get()
                .load(movie.getHinhDaiDien()) // URL ảnh cần tải
                .placeholder(R.drawable.img_phim3) // Ảnh hiển thị khi đang tải (tùy chọn)
                .error(R.drawable.img_phim3) // Ảnh hiển thị nếu lỗi (tùy chọn)
                .into(holder.cardMovieHomeBinding.ivImg);
        // Click card
        holder.itemView.setOnClickListener(v -> {
            if (mContext instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) mContext;

                movieModel.setSelectedMovie(movie);
                mainActivity.replaceFragment(new MovieFragment() ,true);
            }

            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (movieList != null)
            return movieList.size();
        return 0;
    }

    // ViewHolder class
    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardMovieHomeBinding cardMovieHomeBinding;

        public CardViewHolder(@NonNull CardMovieHomeBinding binding) {
            super(binding.getRoot());
            this.cardMovieHomeBinding = binding; // Binding is set here
        }
    }

}
