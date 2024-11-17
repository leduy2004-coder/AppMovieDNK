package com.example.appmoviednk.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.appmoviednk.adapter.CardAdapter;
import com.example.appmoviednk.databinding.FragmentUpcomingMovieHomeBinding;
import com.example.appmoviednk.model.MovieModel;
import com.example.appmoviednk.retrofit.RetrofitClient;
import com.example.appmoviednk.service.MovieService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpcomingMovieHomeFragment extends Fragment {

    FragmentUpcomingMovieHomeBinding binding;
    CardAdapter cardAdapter;
    MovieService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout using ViewBinding
        binding = FragmentUpcomingMovieHomeBinding.inflate(inflater, container, false);

        // Initialize adapter and layout manager
        cardAdapter = new CardAdapter(requireContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        binding.rcvShowingMovie.setLayoutManager(gridLayoutManager);

        // Set data to adapter and bind to RecyclerView
        binding.rcvShowingMovie.setAdapter(cardAdapter);
        apiService = RetrofitClient.getRetrofitInstance().create(MovieService.class);

        getMoviesFromApi();
        // Return the root view from binding
        return binding.getRoot();
    }

    // Mock data for movies
    private void getMoviesFromApi() {
        apiService.getMoviesChuaChieu().enqueue(new Callback<List<MovieModel>>() {
            @Override
            public void onResponse(Call<List<MovieModel>> call, Response<List<MovieModel>> response) {
                Log.d("API_DEBUG", "Status Code: " + response.code());
                Log.d("API_DEBUG", "Response Body: " + response.body());

                if (response.isSuccessful() && response.body() != null) {
                    List<MovieModel> movies = response.body();
                    cardAdapter.setData(movies);
                    for (MovieModel movie : movies) {

                        Log.d("API_RESPONSE", "Movie ID: " + movie.getTenPhim());
                    }
                } else {
                    Log.e("API_ERROR", "Không lấy được danh sách phim: " + response.errorBody());
                }
            }


            @Override
            public void onFailure(Call<List<MovieModel>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi khi gọi API: " + t.getMessage(), t);
            }
        });
    }
}