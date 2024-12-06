package com.example.appmoviednk.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.appmoviednk.R;
import com.example.appmoviednk.UserSession;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.adapter.CommentAdapter;
import com.example.appmoviednk.adapter.HistoryBookAdapter;
import com.example.appmoviednk.databinding.FragmentAccountBinding;
import com.example.appmoviednk.databinding.FragmentTrailerBinding;
import com.example.appmoviednk.model.CustomerModel;
import com.example.appmoviednk.retrofit.RetrofitClient;
import com.example.appmoviednk.service.LoginService;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends Fragment {
    FragmentAccountBinding binding;
    HistoryBookAdapter historyBookAdapter;
    LoginService loginService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        historyBookAdapter = new HistoryBookAdapter(requireContext());

        // Set data to adapter and bind to RecyclerView

        CustomerModel loggedInAccount = UserSession.getInstance().getLoggedInAccount();
        if(loggedInAccount != null  ){
            binding.accountName.setText("Họ tên: " +loggedInAccount.getHoTen());
            binding.accountPoint.setText(
                    "Số điểm: " +
                            (loggedInAccount.getDiemThuong() == 0
                                    ? "0"
                                    : String.valueOf(loggedInAccount.getDiemThuong()))
            );
            binding.accountEmail.setText("Email: " +(loggedInAccount.getEmail()));
            binding.accountPhone.setText("SDT: " +(loggedInAccount.getSdt()));
            //hien thi lich su ve da book
            String maKH = loggedInAccount.getMaKH();
            fetchCustomerHistory(maKH);
        } else {
            diaLogLogin();
        }


        binding.accoutBtn.btnPrimary.setText("Xem thêm");

        // Return the root view from binding
        return binding.getRoot();
    }

    private void fetchCustomerHistory(String maKH) {
        loginService = RetrofitClient.getRetrofitInstance().create(LoginService.class);
        Call<List<Map<String, Object>>> call = loginService.getCustomerHistory(maKH);

        call.enqueue(new Callback<List<Map<String, Object>>>() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Map<String, Object>> historyList = response.body();

                    if (historyList.isEmpty()) {
                        Toast.makeText(getContext(), "Không có lịch sử vé đặt!", Toast.LENGTH_SHORT).show();
                        return; // Kết thúc luôn nếu dữ liệu trống
                    }
                    // Cập nhật dữ liệu vào adapter
                    HistoryBookAdapter adapter = new HistoryBookAdapter(getContext());
                    adapter.setData(historyList);

                    binding.listBooked.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.listBooked.setAdapter(adapter);

                    if (historyList.size() > 3) {
                        binding.moreItems.setVisibility(View.VISIBLE); // Hiển thị dấu ...

                    }
                    // Bắt sự kiện khi nhấn vào nút "accout_btn"
                    binding.accoutBtn.btnPrimary.setOnClickListener(v -> {
                        // Kiểm tra trạng thái hiển thị hiện tại
                        if (adapter.getItemCount() > 3) {
                            // Đang hiển thị tất cả, giờ chỉ hiển thị 3 mục
                            adapter.setShowAll(false);
                            binding.accoutBtn.btnPrimary.setText("Xem thêm");
                            binding.moreItems.setVisibility(View.VISIBLE); // Hiển thị dấu ...

                        } else {
                            // Đang hiển thị 3 mục, giờ hiển thị toàn bộ
                            adapter.setShowAll(true);
                            binding.accoutBtn.btnPrimary.setText("Rút gọn");
                            binding.moreItems.setVisibility(View.GONE); // Hiển thị dấu ...

                        }
                        // Cập nhật lại adapter để ép buộc render lại
                        adapter.notifyDataSetChanged();
                    });

                    if (adapter.getItemCount() > 3 ){
                        binding.moreItems.setVisibility(View.VISIBLE); // Hiển thị dấu ...
                    }

                } else {
                    Toast.makeText(getContext(), "Không có dữ liệu lịch sử!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                Log.e("CustomerHistory", "Lỗi khi gọi API: ", t);
                Toast.makeText(getContext(), "Lỗi kết nối, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Hiện dialog yêu cầu đăng nhập
    private void diaLogLogin() {
        new AlertDialog.Builder(getContext())
                .setTitle("Đăng nhập")
                .setMessage("Bạn cần đăng nhập để xem lịch sử. ")
                .setPositiveButton("Đăng nhập", (dialog, which) -> {
                    LoginFragment loginFragment = new LoginFragment();
                    Bundle args = new Bundle();
                    args.putBoolean("returnToAccount", true);
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