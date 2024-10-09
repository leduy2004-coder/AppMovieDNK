package com.example.appmoviednk.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.appmoviednk.R;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.adapter.ScheduleShowingAdapter;
import com.example.appmoviednk.databinding.FragmentMovieBinding;
import com.example.appmoviednk.model.DateShowingModel;
import com.example.appmoviednk.model.DetailMovieModel;
import com.example.appmoviednk.model.MovieModel;
import com.example.appmoviednk.model.ShiftModel;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class MovieFragment extends Fragment {

    FragmentMovieBinding binding;
    ScheduleShowingAdapter scheduleShowingAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMovieBinding.inflate(inflater, container, false);
        DetailMovieModel detailMovieModel = getDetailMovie();
        binding.imgCover.setImageResource(detailMovieModel.getMovie().getImageResource());
        binding.imgProfile.setImageResource(detailMovieModel.getMovie().getImageResource());
        binding.tvName.setText(detailMovieModel.getMovie().getTenPhim());
        binding.tvType.setText("Thể loại: " + detailMovieModel.getTypeMovie());
        binding.tvDate.setText(detailMovieModel.getMovie().getNgayKhoiChieu().toString());
        binding.tvDescription.setText(detailMovieModel.getMovie().getMoTa());
        binding.tvDirector.setText(detailMovieModel.getMovie().getDaoDien());
        binding.tvDuration.setText(String.valueOf(detailMovieModel.getMovie().getThoiLuong()));

        scheduleShowingAdapter = new ScheduleShowingAdapter(requireContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1);
        binding.listSchedule.setLayoutManager(gridLayoutManager);

        scheduleShowingAdapter.setData(detailMovieModel.getListDate());
        binding.listSchedule.setAdapter(scheduleShowingAdapter);

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

    private DetailMovieModel getDetailMovie() {
        String maPhim = "p1";
        String tenPhim = "Chưa bao giờ";
        String daoDien = "Nguyễn Văn A";
        Date ngayKhoiChieu = Date.valueOf("2004-10-02");
        int thoiLuong = 120;
        String moTa = "Là một thanh tra cực kỳ nóng tính, dù có tỷ lệ bắt giữ tội phạm ấn tượng nhưng anh luôn gặp khó khăn trong việc kiểm soát cơn giận của mình. Vì liên tục tấn công các nghi phạm, Cho Su-gwang bị chuyển đến đảo Jeju.";
        int imageResource = R.drawable.img_phim1;

        MovieModel movie = new MovieModel(maPhim, tenPhim, daoDien, ngayKhoiChieu, thoiLuong, moTa, imageResource);

        List<DateShowingModel> listDate = new ArrayList<>();
        Date date1 = Date.valueOf("2024-10-06");
        Date date2 = Date.valueOf("2023-05-20");
        List<ShiftModel> listShift = getListShift();

        listDate.add(new DateShowingModel(date1, listShift));
        listDate.add(new DateShowingModel(date2, listShift));

        return new DetailMovieModel(movie, listDate, "Hành động, Viễn tưởng");
    }

    private List<ShiftModel> getListShift() {
        List<ShiftModel> list = new ArrayList<>();
        Time time1 = Time.valueOf("09:00:00"); // 09:00 AM
        Time time2 = Time.valueOf("13:00:00"); // 01:00 PM
        Time time3 = Time.valueOf("15:00:00"); // 03:00 PM
        list.add(new ShiftModel("ca1", time1));
        list.add(new ShiftModel("ca2", time2));
        list.add(new ShiftModel("ca3", time3));

        return list;
    }

}