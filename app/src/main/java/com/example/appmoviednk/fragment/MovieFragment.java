package com.example.appmoviednk.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.appmoviednk.R;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.adapter.CardAdapter;
import com.example.appmoviednk.adapter.ScheduleShowingAdapter;
import com.example.appmoviednk.databinding.FragmentMovieBinding;
import com.example.appmoviednk.model.DateShowingModel;
import com.example.appmoviednk.model.MovieModel;
import com.example.appmoviednk.model.ScheduleModel;
import com.example.appmoviednk.model.ShiftModel;
import com.example.appmoviednk.retrofit.RetrofitClient;
import com.example.appmoviednk.service.ApiService;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends Fragment {

    FragmentMovieBinding binding;
    ScheduleShowingAdapter scheduleShowingAdapter;
    CardAdapter cardAdapter;
    ApiService apiService;
    MovieModel movie;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMovieBinding.inflate(inflater, container, false);


        scheduleShowingAdapter = new ScheduleShowingAdapter(requireContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1);
        binding.listSchedule.setLayoutManager(gridLayoutManager);


        binding.listSchedule.setAdapter(scheduleShowingAdapter);

        if (getArguments() != null) {
             movie = (MovieModel) getArguments().getSerializable("movie_object");

            if (movie != null) {
                // set text
                setTextTitle();

                apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
                // Thay thế bằng ID phim thực tế
                getSchedules(movie.getMaPhim());

            }
        }


        // Click trailer
        binding.frTrailer.setOnClickListener(v -> {
            TrailerFragment trailerFragment = new TrailerFragment();

            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.replaceFragment(trailerFragment,true);

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
                    Log.d("thong tin", schedules.toString());
                    if (schedules != null) {
                        for (ScheduleModel schedule : schedules) {
                            schedule.setMaPhim(phimId); // Thiết lập maPhim cho từng đối tượng
                        }
                        // Gọi phương thức setData để cập nhật RecyclerView
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

    private void setTextTitle(){
        binding.tvName.setText(movie.getTenPhim().toString());
        binding.tvType.setText("Thể loại: ");
        binding.tvDescription.setText(movie.getMoTa().toString());
        binding.tvDirector.setText(movie.getDaoDien());
        binding.tvDate.setText(movie.getNgayKhoiChieu().toString());
        binding.tvDuration.setText(String.valueOf(movie.getThoiLuong()));

    }

}

