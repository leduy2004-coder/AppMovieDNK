package com.example.appmoviednk.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appmoviednk.DateUtils;
import com.example.appmoviednk.R;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.adapter.SlideAdapter;
import com.example.appmoviednk.adapter.SpinnerAdapter;
import com.example.appmoviednk.adapter.TagViewMovieAdapter;
import com.example.appmoviednk.databinding.FragmentHomeBinding;
import com.example.appmoviednk.model.DisplayTextSpinner;
import com.example.appmoviednk.model.MovieModel;
import com.example.appmoviednk.model.ScheduleModel;
import com.example.appmoviednk.model.ShiftModel;
import com.example.appmoviednk.retrofit.RetrofitClient;
import com.example.appmoviednk.service.MovieService;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    private SpinnerAdapter<MovieModel> movieAdapter;
    private SpinnerAdapter<ScheduleModel> scheduleAdapter;
    private SpinnerAdapter<ShiftModel> shiftAdapter;
    private MovieModel selectedMovie;
    private String selectedDate;
    private TagViewMovieAdapter tagViewAdapter;
    private SlideAdapter slideAdapter;
    // Biến cờ để kiểm soát lần đầu spinner được chọn
    private boolean isMovieSpinnerInitialized = false;
    private boolean isDateSpinnerInitialized = false;
    // auto slide
    private Handler slideHandle = new Handler();
    MovieService apiService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        apiService = RetrofitClient.getRetrofitInstance().create(MovieService.class);


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


        // Truy cập spnMovie hoặc các phần tử khác ở đây

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (binding != null) {
                binding.spnMovie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!isMovieSpinnerInitialized) {
                            isMovieSpinnerInitialized = true;
                            return;
                        }
                        DisplayTextSpinner selectedItem = (DisplayTextSpinner) parent.getItemAtPosition(position);
                        if (selectedItem != null) {
                            String displayText = selectedItem.getDisplayText();
                            if (displayText.equals("Chọn phim")) {
                                checkSelectionDefault();
                                selectedMovie = null;
                            } else if (selectedItem instanceof MovieModel) {
                                MovieModel movie = ((MovieModel) selectedItem);
                                // Gọi API lấy danh sách ngày chiếu theo mã phim
                                getDateShowingFromApi(movie.getMaPhim());

                                // Lưu mã phim vào biến toàn cục hoặc SharedPreferences để kiểm tra ở spinner tiếp theo
                                selectedMovie = movie;

                                // Kiểm tra xem cả hai Spinner đều được chọn chưa
                                checkSelectionAndNavigate();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Không làm gì nếu không có mục nào được chọn
                    }
                });
                binding.spnDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!isDateSpinnerInitialized) {
                            isDateSpinnerInitialized = true;
                            return;
                        }
                        DisplayTextSpinner selectedItem = (DisplayTextSpinner) parent.getItemAtPosition(position);
                        if (selectedItem != null) {
                            String displayText = selectedItem.getDisplayText();
                            if (displayText.equals("Chọn ngày")) {
                                checkSelectionDefault();
                                selectedDate = null;
                            } else if (selectedItem instanceof ScheduleModel) {
                                String date = ((ScheduleModel) selectedItem).getDateString();
                                // Gọi API lấy danh sách phim chiếu theo ngày
                                getMoviesFromApi(date);

                                // Lưu ngày đã chọn vào biến toàn cục hoặc SharedPreferences để kiểm tra ở spinner tiếp theo
                                selectedDate = date;

                                // Kiểm tra xem cả hai Spinner đều được chọn chưa
                                checkSelectionAndNavigate();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Không làm gì nếu không có mục nào được chọn
                    }
                });

                getMoviesFromApi();
                getDateShowingFromApi();
            } else {
                Log.e("BINDING_ERROR", "Binding chưa được khởi tạo!");
            }
        }, 1000);
        return binding.getRoot();
    }

    private List<MovieModel> getListMovie() {
        List<MovieModel> list = new ArrayList<>();
        list.add(new MovieModel("p1", "Chua bao gio", R.drawable.img_phim1));
        list.add(new MovieModel("p2", "hay la la ", R.drawable.img_phim2));
        list.add(new MovieModel("p3", "toi la ai ", R.drawable.img_phim3));

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

    private void getMoviesFromApi() {
        apiService.getMoviesDangChieu().enqueue(new Callback<List<MovieModel>>() {
            @Override
            public void onResponse(Call<List<MovieModel>> call, Response<List<MovieModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MovieModel> list = response.body();

                    movieAdapter = new SpinnerAdapter<>(requireContext(), R.layout.spinner_selected_home, new ArrayList<>());

                    movieAdapter.clear();
                    movieAdapter.addDefaultItem("Chọn phim");
                    movieAdapter.addAll(list);
                    isMovieSpinnerInitialized = false;

                    movieAdapter.notifyDataSetChanged();
                    binding.spnMovie.setAdapter(movieAdapter);
                    Log.d("API_SUCCESS", "Danh sách phim đã được cập nhật");
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

    private void getDateShowingFromApi() {
        apiService.getNgayDangChieu().enqueue(new Callback<List<ScheduleModel>>() {
            @Override
            public void onResponse(Call<List<ScheduleModel>> call, Response<List<ScheduleModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ScheduleModel> list = response.body();
                    // Chuyển đổi định dạng ngày
                    for (ScheduleModel schedule : list) {
                        String originalDate = schedule.getNgayChieu();
                        String formattedDate = DateUtils.formatDateString(originalDate);
                        schedule.setDateString(formattedDate);
                    }
                    scheduleAdapter = new SpinnerAdapter<>(requireContext(), R.layout.spinner_selected_home, new ArrayList<>());

                    scheduleAdapter.clear();
                    scheduleAdapter.addDefaultItem("Chọn ngày");
                    scheduleAdapter.addAll(list);
                    isDateSpinnerInitialized = false;

                    scheduleAdapter.notifyDataSetChanged();

                    binding.spnDate.setAdapter(scheduleAdapter);
                    Log.d("API_SUCCESS", "Danh sách ngày đã được cập nhật");
                } else {
                    Log.e("API_ERROR", "Không lấy được danh sách phim: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<ScheduleModel>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi khi gọi API: " + t.getMessage(), t);
            }
        });
    }

    //Gọi API lấy danh sách ngày theo phim
    private void getDateShowingFromApi(String movieCode) {
        apiService.getDateSchedules(movieCode).enqueue(new Callback<List<ScheduleModel>>() {
            @Override
            public void onResponse(Call<List<ScheduleModel>> call, Response<List<ScheduleModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ScheduleModel> list = response.body();
                    for (ScheduleModel schedule : list) {
                        String originalDate = schedule.getNgayChieu();
                        String formattedDate = DateUtils.formatDateString(originalDate);
                        schedule.setDateString(formattedDate);
                    }
                    // Cập nhật Spinner với danh sách ngày
                    scheduleAdapter = new SpinnerAdapter<>(requireContext(), R.layout.spinner_selected_home, new ArrayList<>());
                    scheduleAdapter.clear();
                    scheduleAdapter.addDefaultItem("Chọn ngày");
                    scheduleAdapter.addAll(list);
                    scheduleAdapter.notifyDataSetChanged();
                    binding.spnDate.setAdapter(scheduleAdapter);

                    Log.d("API_SUCCESS", "Danh sách ngày đã được cập nhật");
                } else {
                    Log.e("API_ERROR", "Không lấy được danh sách ngày: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<ScheduleModel>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi khi gọi API: " + t.getMessage(), t);
            }
        });
    }

    //Gọi API lấy danh sách phim chiếu theo ngày
    private void getMoviesFromApi(String date) {
        apiService.getMoviesByDate(date).enqueue(new Callback<List<MovieModel>>() {
            @Override
            public void onResponse(Call<List<MovieModel>> call, Response<List<MovieModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MovieModel> list = response.body();

                    // Cập nhật Spinner với danh sách phim
                    movieAdapter = new SpinnerAdapter<>(requireContext(), R.layout.spinner_selected_home, new ArrayList<>());
                    movieAdapter.clear();
                    movieAdapter.addDefaultItem("Chọn phim");
                    movieAdapter.addAll(list);
                    movieAdapter.notifyDataSetChanged();
                    binding.spnMovie.setAdapter(movieAdapter);

                    Log.d("API_SUCCESS", "Danh sách phim đã được cập nhật");
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

    private void checkSelectionAndNavigate() {
        // Kiểm tra nếu cả mã phim và ngày đều đã được chọn
        if (selectedMovie != null && !selectedMovie.getMaPhim().isEmpty() && selectedDate != null && !selectedDate.isEmpty()) {
            // Cả hai đã được chọn, chuyển hướng đến Activity khác
            MainActivity mainActivity = (MainActivity) getActivity(); // Assuming mContext is MainActivity
            assert mainActivity != null;
            MovieModel movieModel = new ViewModelProvider(mainActivity).get(MovieModel.class);
            movieModel.setSelectedMovie(selectedMovie); // Lưu movie vào ViewModel

            // Chuyển hướng đến MovieFragment
            mainActivity.replaceFragment(new MovieFragment(), true);
        }
    }

    private void checkSelectionDefault() {
        int selectedMoviePosition = binding.spnMovie.getSelectedItemPosition();
        int selectedDatePosition = binding.spnDate.getSelectedItemPosition();

        // Kiểm tra nếu cả hai spinner đều ở trạng thái mặc định
        if (movieAdapter.isDefaultSelected(selectedMoviePosition, "Chọn phim")
                && scheduleAdapter.isDefaultSelected(selectedDatePosition, "Chọn ngày")) {
            getMoviesFromApi();
            getDateShowingFromApi();
        }
    }

}
