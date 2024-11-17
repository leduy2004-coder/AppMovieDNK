package com.example.appmoviednk.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.appmoviednk.DateUtils;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.adapter.ScheduleShowingAdapter;
import com.example.appmoviednk.databinding.FragmentMovieBinding;
import com.example.appmoviednk.model.MovieModel;
import com.example.appmoviednk.model.ScheduleModel;
import com.example.appmoviednk.retrofit.RetrofitClient;
import com.example.appmoviednk.service.MovieService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends Fragment {

    FragmentMovieBinding binding;
    ScheduleShowingAdapter scheduleShowingAdapter;
    MovieService apiService;
    MovieModel movie;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMovieBinding.inflate(inflater, container, false);
        movie = new ViewModelProvider(requireActivity()).get(MovieModel.class);

        // Khởi tạo apiService
        apiService = RetrofitClient.getRetrofitInstance().create(MovieService.class);

        scheduleShowingAdapter = new ScheduleShowingAdapter(requireContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1);
        binding.listSchedule.setLayoutManager(gridLayoutManager);
        binding.listSchedule.setAdapter(scheduleShowingAdapter);

        movie.getSelectedMovie().observe(getViewLifecycleOwner(), new Observer<MovieModel>() {
            @Override
            public void onChanged(MovieModel selectedMovie) {
                if (selectedMovie != null) {
                    setTextTitle(selectedMovie);
                    getSchedules(selectedMovie.getMaPhim().toString().trim());
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
                mainActivity.replaceFragment(trailerFragment, true);
            }
        });

        return binding.getRoot();
    }

    private void getSchedules(String phimId) {
        apiService.getSchedules(phimId).enqueue(new Callback<List<ScheduleModel>>() {
            @Override
            public void onResponse(Call<List<ScheduleModel>> call, Response<List<ScheduleModel>> response) {
                if (response.isSuccessful()) {
                    List<ScheduleModel> schedules = response.body();
                    // Kiểm tra schedules có khác null không trước khi ghi log
                    if (schedules != null) {
                        Log.d("thong tin", schedules.toString());
                        for (ScheduleModel schedule : schedules) {
                            schedule.setMaPhim(phimId); // Thiết lập maPhim cho từng đối tượng
                            Log.d("yonh yin 2", schedule.getMaPhim());
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
            public void onFailure(Call<List<ScheduleModel>> call, Throwable t) {
                Log.d("API Error", t.getMessage());
            }
        });
    }

    private void setTextTitle(MovieModel movie) {
        binding.tvName.setText(movie.getTenPhim());
        binding.tvType.setText("Thể loại: " );
        binding.tvDescription.setText(movie.getMoTa());
        binding.tvDirector.setText(movie.getDaoDien());
        String formatDate = DateUtils.formatDateString(movie.getNgayKhoiChieu());
        binding.tvDate.setText(formatDate);
        binding.tvDuration.setText(String.valueOf(movie.getThoiLuong()));
    }
}

