package com.example.appmoviednk.adapter;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmoviednk.MovieSession;
import com.example.appmoviednk.R;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.databinding.CardMovieHomeBinding;
import com.example.appmoviednk.fragment.BookTicketFragment;
import com.example.appmoviednk.fragment.MovieFragment;
import com.example.appmoviednk.model.MovieModel;
import com.example.appmoviednk.service.ApiService;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<MovieModel> movieList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    ApiService apiService;
    // Interface để lắng nghe sự kiện click item
    public interface OnItemClickListener {
        void onItemClick(MovieModel movie);
    }

    // Phương thức để thiết lập listener từ bên ngoài adapter
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
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
        if (movie == null) {
            return;
        }

        holder.cardMovieHomeBinding.tvName.setText(movie.getTenPhim());
        holder.cardMovieHomeBinding.ivImg.setImageResource(movie.getImageResource()); // Thay đổi thành ảnh mặc định của bạn


        // Click card
        holder.itemView.setOnClickListener(v -> {
            if (mContext instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) mContext;

                MovieSession.getInstance().clearSession();
                MovieSession.getInstance().setSelectedMovie(movie);


                // Tạo Fragment mới
                MovieFragment movieFragment = new MovieFragment();
                // Tạo Bundle và truyền đối tượng MovieModel
                Bundle moveBundle = new Bundle();
                moveBundle.putSerializable("movie_object", movie); // Truyền MovieModel dưới dạng Serializable
                // Gán Bundle vào Fragment
                movieFragment.setArguments(moveBundle);
                // Chuyển Fragment
                mainActivity.replaceFragment(movieFragment ,true);
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
    public class CardViewHolder extends RecyclerView.ViewHolder {
        CardMovieHomeBinding cardMovieHomeBinding;

        public CardViewHolder(@NonNull CardMovieHomeBinding binding) {
            super(binding.getRoot());
            this.cardMovieHomeBinding = binding; // Binding is set here
        }
    }
//    private void getInforMovie (String maPhim){
//        apiService.getInforMovie(maPhim).enqueue(new Callback<MovieModel>() {
//            @Override
//            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    MovieModel movie = response.body();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieModel> call, Throwable t) {
//               Log.d( "Lỗi kết nối, vui lòng thử lại!","loi");
//            }
//        });
//    }
}
