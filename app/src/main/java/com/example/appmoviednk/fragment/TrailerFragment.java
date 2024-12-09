package com.example.appmoviednk.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.appmoviednk.R;
import com.example.appmoviednk.UserSession;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.adapter.CommentAdapter;
import com.example.appmoviednk.databinding.FragmentTrailerBinding;
import com.example.appmoviednk.model.CommentModel;
import com.example.appmoviednk.model.CommentResponse;
import com.example.appmoviednk.model.MovieModel;
import com.example.appmoviednk.retrofit.RetrofitClient;
import com.example.appmoviednk.service.CommentService;
import com.example.appmoviednk.service.MovieService;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrailerFragment extends Fragment {

    FragmentTrailerBinding binding;
    CommentAdapter cmtAdapter;
    YouTubePlayerView youTubePlayerView;  // Khai báo YouTubePlayerView
    MovieModel movie;
    CommentService apiService;
    List<CommentModel> commentModels;

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTrailerBinding.inflate(inflater, container, false);
        apiService = RetrofitClient.getRetrofitInstance().create(CommentService.class);

        // Khởi tạo ViewModel và quan sát dữ liệu
        MovieModel movieModel = new ViewModelProvider(requireActivity()).get(MovieModel.class);
        movieModel.getSelectedMovie().observe(getViewLifecycleOwner(), selectedMovie -> {
            if (selectedMovie != null) {
                movie = selectedMovie;

                // Tải video
                youTubePlayerView = binding.videoTrailer;
                youTubePlayerView.setEnableAutomaticInitialization(false);
                getLifecycle().addObserver(youTubePlayerView);
                // Custom UI
                View customPlayerUi = youTubePlayerView.inflateCustomPlayerUi(R.layout.custom_control_video);
                YouTubePlayerListener listener = new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        // Tạo điều khiển video tùy chỉnh
                        ControlVideo customPlayerUiController = new ControlVideo(customPlayerUi, youTubePlayer, youTubePlayerView);
                        youTubePlayer.addListener(customPlayerUiController);
                        YouTubePlayerUtils.loadOrCueVideo(youTubePlayer, getLifecycle(), movie.getVideo(), 0F);
                    }
                };
                binding.nameMovie.setText("Tên phim: " + movie.getTenPhim());
                IFramePlayerOptions options = new IFramePlayerOptions.Builder().controls(0).build();
                youTubePlayerView.initialize(listener, options);

                //Xet grid cho comment
                cmtAdapter = new CommentAdapter(requireContext());
                GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1);
                binding.listCmt.setLayoutManager(gridLayoutManager);


                binding.listCmt.setAdapter(cmtAdapter);

                // Gọi API lấy comment
                getComments(movie.getMaPhim());

            } else {
                Log.e("MovieFragment", "MovieModel null");
            }
        });
        // Sự kiện gửi quay lại
        binding.imgPrev.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).onBackPressed();
            }
        });
        // Sự kiện gửi comment
        binding.imgSend.setOnClickListener(v -> {
            checkBeforeSendComment();
        });

        // Bắt sự kiện khi nhấn vào nút "accout_btn"
        binding.btnSeen.setOnClickListener(v -> {
            // Kiểm tra trạng thái hiển thị hiện tại
            if (cmtAdapter.getItemCount() > 4) {
                // Đang hiển thị tất cả, giờ chỉ hiển thị 4 mục
                cmtAdapter.setShowAll(false);
                binding.btnSeen.setText("Xem thêm");
            } else {
                // Đang hiển thị 4 mục, giờ hiển thị toàn bộ
                cmtAdapter.setShowAll(true);
                binding.btnSeen.setText("Rút gọn");

            }
            // Cập nhật lại adapter để ép buộc render lại
            cmtAdapter.notifyDataSetChanged();
        });
        return binding.getRoot();
    }

    private void getComments(String maPhim) {
        apiService.getCommentsByMovieId(maPhim).enqueue(new Callback<List<CommentModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<List<CommentModel>> call, @NonNull Response<List<CommentModel>> response) {
                if (response.isSuccessful()) {
                    commentModels = response.body();

                    if (commentModels != null && !commentModels.isEmpty()) {
                        Collections.reverse(commentModels);
                        binding.countComment.setText(commentModels.size() + " bình luận");
                        cmtAdapter.setData(commentModels);

                        if(commentModels.size() > 4){
                            binding.btnSeen.setVisibility(View.VISIBLE);
                        }else {
                            binding.btnSeen.setVisibility(View.GONE);
                        }
                    } else if (commentModels.isEmpty()){
                        // Nếu không có bình luận
                        binding.countComment.setText("0 bình luận");
                        cmtAdapter.setData(Collections.emptyList()); // Set danh sách bình luận rỗng
                        binding.btnSeen.setVisibility(View.GONE);
                    }
                } else {
                    binding.countComment.setText("0 bình luận");
                    cmtAdapter.setData(Collections.emptyList());
                    binding.btnSeen.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CommentModel>> call, Throwable t) {
                binding.countComment.setText("Chưa có bình luận nào");
                cmtAdapter.setData(Collections.emptyList());
            }
        });
    }

    // Hàm gửi bình luận
    private void checkBeforeSendComment() {
        // Kiểm tra xem người dùng có đăng nhập và phim có hợp lệ không
        if (movie != null && UserSession.getInstance().getLoggedInAccount() != null) {
            String commentContent = binding.commentContent.getText().toString().trim();
            if (!commentContent.isEmpty()) {
                CommentModel commentModel = new CommentModel();
                commentModel.setMaPhim(movie.getMaPhim());
                commentModel.setNoiDung(commentContent);
                commentModel.setKhachHang(UserSession.getInstance().getLoggedInAccount());
                postComment(commentModel);

            } else {
                // Hiển thị thông báo nếu nội dung bình luận trống
                Toast.makeText(getContext(), "Vui lòng nhập bình luận", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Hiển thị dialog yêu cầu người dùng đăng nhập nếu chưa đăng nhập
            diaLogLogin();
        }
    }
    private void postComment(CommentModel commentModel) {
        apiService.insertComment(commentModel).enqueue(new Callback<CommentResponse>() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<CommentResponse> call, @NonNull Response<CommentResponse> response) {
                if (response.isSuccessful()) {
                    // Dữ liệu trả về từ server
                    CommentResponse commentResponse = response.body();

                    // Lấy message
                    assert commentResponse != null;
                    String message = commentResponse.getMessage();
                    Log.d("API", "Message: " + message);

                    // Lấy comment mới
                    CommentModel newComment = commentResponse.getComment();
                    newComment.setKhachHang(UserSession.getInstance().getLoggedInAccount());

                    // Nếu commentModels là null hoặc rỗng, khởi tạo danh sách
                    if (commentModels == null) {
                        commentModels = new ArrayList<>();
                    }

                    // Thêm bình luận mới vào đầu danh sách
                    commentModels.add(0, newComment);

                    // Cập nhật Adapter để làm mới RecyclerView
                    cmtAdapter.setData(commentModels);
                    cmtAdapter.notifyItemInserted(0);  // Chỉ thông báo thay đổi khi thêm 1 mục mới vào đầu danh sách

                    // Làm sạch nội dung bình luận
                    binding.commentContent.setText("");

                    binding.countComment.setText(commentModels.size() + " bình luận");
                    getComments(movie.getMaPhim());


                    // Thông báo thành công
                    Toast.makeText(getContext(), "Bình luận thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Insert Comment Error", response.message());
                    Toast.makeText(getContext(), "Bình luận thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommentResponse> call, @NonNull Throwable t) {
                Log.e("Error", Objects.requireNonNull(t.getMessage()));
                Toast.makeText(getContext(), "Thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Hiện dialog yêu cầu đăng nhập
    private void diaLogLogin() {
        new AlertDialog.Builder(getContext())
                .setTitle("Đăng nhập")
                .setMessage("Bạn cần đăng nhập để bình luận. ")
                .setPositiveButton("Đăng nhập", (dialog, which) -> {
                    LoginFragment loginFragment = new LoginFragment();
                    Bundle args = new Bundle();
                    args.putBoolean("returnToTrailer", true);
                    loginFragment.setArguments(args);

                    MainActivity mainActivity = (MainActivity) getActivity();
                    if (mainActivity != null) {
                        mainActivity.replaceFragment(loginFragment, true);
                    }
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
}

