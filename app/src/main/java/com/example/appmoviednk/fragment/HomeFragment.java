package com.example.appmoviednk.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appmoviednk.R;
import com.example.appmoviednk.adapter.SlideAdapter;
import com.example.appmoviednk.adapter.SpinnerAdapter;
import com.example.appmoviednk.adapter.TagViewMovieAdapter;
import com.example.appmoviednk.databinding.FragmentHomeBinding;
import com.example.appmoviednk.model.MovieModel;
import com.example.appmoviednk.model.ScheduleModel;
import com.example.appmoviednk.model.ShiftModel;
import com.google.android.material.tabs.TabLayoutMediator;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    private SpinnerAdapter<MovieModel> movieAdapter;
    private SpinnerAdapter<ScheduleModel> scheduleAdapter;
    private SpinnerAdapter<ShiftModel> shiftAdapter;
    private TagViewMovieAdapter tagViewAdapter;
    private SlideAdapter slideAdapter;

    // auto slide
    private Handler slideHandle = new Handler();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        movieAdapter = new SpinnerAdapter<>(requireContext(), R.layout.spinner_selected_home, getListMovie());
        movieAdapter.addDefaultItem("Chọn phim");
        scheduleAdapter = new SpinnerAdapter<>(requireContext(), R.layout.spinner_selected_home, getListSchedule());
        scheduleAdapter.addDefaultItem("Chọn ngày");
        shiftAdapter = new SpinnerAdapter<>(requireContext(), R.layout.spinner_selected_home, getListShift());
        shiftAdapter.addDefaultItem("Chọn ca");

        binding.spnMovie.setAdapter(movieAdapter);
        binding.spnDate.setAdapter(scheduleAdapter);
        binding.spnShift.setAdapter(shiftAdapter);

        // Khởi tạo TabLayout và ViewPager2
        tagViewAdapter = new TagViewMovieAdapter(requireActivity(), 2);
        binding.viewPager.setAdapter(tagViewAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Phim Đang Chiếu");
                            break;
                        case 1:
                            tab.setText("Phim Sắp Chiếu");
                            break;
                    }
                }).attach();
        setTabDividers();

        // Slide
        slideAdapter = new SlideAdapter(requireContext());
        slideAdapter.setData(getListMovie());
        binding.viewSlider.setAdapter(slideAdapter);

        binding.viewSlider.setClipToPadding(false);
        binding.viewSlider.setClipChildren(false);
        binding.viewSlider.setOffscreenPageLimit(3);
        binding.viewSlider.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(30));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f); // Hiệu ứng zoom in/out
                page.setAlpha(0.5f + r * 0.5f); // Hiệu ứng mờ dần

            }
        });
        binding.viewSlider.setPageTransformer(compositePageTransformer);
        binding.viewSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandle.removeCallbacks(slideRunnable);
                slideHandle.postDelayed(slideRunnable, 2000);
            }
        });
        return binding.getRoot();
    }

    private List<MovieModel> getListMovie() {
        List<MovieModel> list = new ArrayList<>();

        list.add(new MovieModel("p1", "Chua bao gio", R.drawable.img_phim1));
        list.add(new MovieModel("p2", "hay la la ", R.drawable.img_phim2));
        list.add(new MovieModel("p3", "toi la ai ", R.drawable.img_phim3));

        return list;
    }

    private List<ScheduleModel> getListSchedule() {
        List<ScheduleModel> list = new ArrayList<>();

        list.add(new ScheduleModel("sc1", Date.valueOf("2004-10-02")));
        list.add(new ScheduleModel("sc2", Date.valueOf("2001-12-02")));

        return list;
    }

    private List<ShiftModel> getListShift() {
        List<ShiftModel> list = new ArrayList<>();

        Time time1 = Time.valueOf("09:00:00"); // 09:00 AM
        Time time2 = Time.valueOf("13:00:00");
        list.add(new ShiftModel("ca1", time1));
        list.add(new ShiftModel("ca2", time2));

        return list;
    }

    // Tạo đường kẻ trong tab view
    private void setTabDividers() {
        View root = binding.tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Color.WHITE);
            drawable.setSize(30, 1);
            ((LinearLayout) root).setDividerPadding(20);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }
    }

    private Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            int nextItem = binding.viewSlider.getCurrentItem() + 1;
            if (nextItem >= slideAdapter.getItemCount()) {
                nextItem = 0; // Quay lại trang đầu tiên nếu đã đến trang cuối cùng
            }
            binding.viewSlider.setCurrentItem(nextItem);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Hủy các lệnh gọi Handler để tránh rò rỉ bộ nhớ
        slideHandle.removeCallbacks(slideRunnable);
        binding = null;
    }

}
