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
            MovieModel movie = (MovieModel) getArguments().getSerializable("movie_object");

            if (movie != null) {
                // Sử dụng đối tượng MovieModel
                binding.tvName.setText(movie.getTenPhim().toString());
                binding.tvType.setText("Thể loại: ");
                binding.tvDescription.setText(movie.getMoTa().toString());
                binding.tvDirector.setText(movie.getDaoDien());
                binding.tvDate.setText(movie.getNgayKhoiChieu().toString());
                binding.tvDuration.setText(String.valueOf(movie.getThoiLuong()));
                Log.d("Ten phim dang chon: ", movie.getTenPhim().toString());

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
                mainActivity.replaceFragment(trailerFragment);

            }
        });
        return binding.getRoot();
    }




//    private void getSchedules(String phimId) {
//        apiService.getSchedules(phimId).enqueue(new Callback<List<Map<String, Object>>>() {
//            @Override
//            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Map<String, Object>> schedules = response.body();
//                    // Xử lý dữ liệu ở đây (hiển thị lên UI, lưu trữ, v.v.)
//                    for (Map<String, Object> schedule : schedules) {
//                        String ngayChieu = (String) schedule.get("ngayChieu");
//                        Log.d("Schedule", "Ngày Chiếu: " + ngayChieu);
//
//                        List<Map<String, Object>> caChieuList = (List<Map<String, Object>>) schedule.get("caChieu");
//                        for (Map<String, Object> caChieu : caChieuList) {
//                            String thoiGianBatDau = (String) caChieu.get("thoiGianBatDau");
//                            String maCa = (String) caChieu.get("maCa");
//                            Log.d("Shift", "Thời gian bắt đầu: " + thoiGianBatDau + ", Mã ca: " + maCa);
//                        }
//                    }
//                } else {
//                    Log.d("loi log ne:", "111111111111111");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
//                Log.d("API Error", t.getMessage());
//            }
//        });
//    }
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

}

