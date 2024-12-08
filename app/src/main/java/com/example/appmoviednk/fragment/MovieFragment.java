package com.example.appmoviednk.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.appmoviednk.DateUtils;
import com.example.appmoviednk.R;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.adapter.ScheduleShowingAdapter;
import com.example.appmoviednk.databinding.FragmentMovieBinding;
import com.example.appmoviednk.model.MovieModel;
import com.example.appmoviednk.model.ScheduleModel;
import com.example.appmoviednk.model.TypeMovieModel;
import com.example.appmoviednk.retrofit.RetrofitClient;
import com.example.appmoviednk.service.MovieService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends Fragment {

    FragmentMovieBinding binding;
    ScheduleShowingAdapter scheduleShowingAdapter;
    MovieService movieService;
    MovieModel movie;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMovieBinding.inflate(inflater, container, false);
        movie = new ViewModelProvider(requireActivity()).get(MovieModel.class);

        // Khởi tạo movieService
        movieService = RetrofitClient.getRetrofitInstance().create(MovieService.class);

        scheduleShowingAdapter = new ScheduleShowingAdapter(requireContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1);
        binding.listSchedule.setLayoutManager(gridLayoutManager);
        binding.listSchedule.setAdapter(scheduleShowingAdapter);

        movie.getSelectedMovie().observe(getViewLifecycleOwner(), new Observer<MovieModel>() {
            @Override
            public void onChanged(MovieModel selectedMovie) {
                if (selectedMovie != null) {
                    movie = selectedMovie;
                    setContent(selectedMovie);
                    getSchedules(selectedMovie.getMaPhim().toString().trim());
                    getGenreByMovie(selectedMovie.getMaPhim().toString().trim());
                    Log.d("23232323", selectedMovie.getMaPhim());
                } else {
                    Log.e("MovieFragment", "Nhận được MovieModel null");
                }
            }
        });

        // Nhấn vào trailer
        binding.frTrailer.setOnClickListener(v -> {
            TrailerFragment trailerFragment = new TrailerFragment();
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                MovieModel movieModel = new ViewModelProvider(mainActivity).get(MovieModel.class);
                movieModel.setSelectedMovie(movie);
                mainActivity.replaceFragment(trailerFragment, true);
            }
        });

        return binding.getRoot();
    }

    private void getGenreByMovie(String phimId) {
        movieService.getGenreByMovie(phimId).enqueue(new Callback<TypeMovieModel>() {
            @Override
            public void onResponse(Call<TypeMovieModel> call, Response<TypeMovieModel> response) {
                if (response.isSuccessful()) {
                    TypeMovieModel typeMovieModel = response.body();
                    binding.tvType.setText("Thể loại: " + typeMovieModel.getTenLPhim());
                }
            }

            @Override
            public void onFailure(Call<TypeMovieModel> call, Throwable t) {
                Log.d("API Error", t.getMessage());
            }
        });
    }

    private void getSchedules(String phimId) {
        movieService.getSchedules(phimId).enqueue(new Callback<List<ScheduleModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ScheduleModel>> call, @NonNull Response<List<ScheduleModel>> response) {
                if (response.isSuccessful()) {
                    List<ScheduleModel> schedules = response.body();
                    // Kiểm tra schedules có khác null không trước khi ghi log
                    if (schedules != null) {
                        for (ScheduleModel schedule : schedules) {
                            schedule.setMaPhim(phimId); // Thiết lập maPhim cho từng đối tượng
                        }
//                        // Gọi phương thức setData để cập nhật RecyclerView
                        scheduleShowingAdapter.setData(schedules);
                    } else {
                        Log.d("Error", "Response body is null");
                    }
                } else {
                    Log.d("Error", "Request failed with status: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ScheduleModel>> call, @NonNull Throwable t) {
                Log.d("API Error", t.getMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setContent(MovieModel movie) {
        binding.tvName.setText(movie.getTenPhim());
        binding.tvDescription.setText(movie.getMoTa());
        binding.tvDirector.setText(movie.getDaoDien());
        Picasso.get()
                .load(movie.getHinhDaiDien()) // URL ảnh cần tải
                .placeholder(R.drawable.img_phim3) // Ảnh hiển thị khi đang tải (tùy chọn)
                .error(R.drawable.img_phim3) // Ảnh hiển thị nếu lỗi (tùy chọn)
                .into(binding.imgProfile);
        Picasso.get()
                .load(movie.getHinhDaiDien())
                .placeholder(R.drawable.img_phim3)
                .error(R.drawable.img_phim3)
                .into(binding.imgCover);
        String formatDate = DateUtils.formatDateString(movie.getNgayKhoiChieu());
        binding.tvDate.setText(formatDate);
        binding.tvDuration.setText(String.valueOf(movie.getThoiLuong()));
    }
}

