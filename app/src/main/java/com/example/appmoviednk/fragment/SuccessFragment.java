package com.example.appmoviednk.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.databinding.FragmentSuccessBinding;
import com.example.appmoviednk.retrofit.RetrofitClient;
import com.example.appmoviednk.service.CustomerService;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuccessFragment extends Fragment {
    FragmentSuccessBinding binding;
    CustomerService customerService;
    int pointAdd, totalPoint, voucher, point;
    Double totalAmount;
    Map<String, Integer> body;
    String maKH, maBook;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSuccessBinding.inflate(inflater, container, false);
        customerService = RetrofitClient.getRetrofitInstance().create(CustomerService.class);

        // Lấy Bundle được truyền qua
        Bundle arguments = getArguments();
        if (arguments != null) {
            totalAmount = arguments.getDouble("totalAmount", 0);
            point = arguments.getInt("point", 0);
            maKH = arguments.getString("maKH", "");
            voucher = arguments.getInt("voucher", 0);
            maBook = arguments.getString("maBook", "");

            Log.d("SuccessFragment", "Total Amount: " + totalAmount + ", Point: " + point);

            pointAdd = (int) ((totalAmount / 100000) * 10); // Ví dụ: 10 điểm cho mỗi 100,000 VNĐ

            totalPoint = pointAdd + point;

        }

        body = new HashMap<>();
        body.put("diemThuong", totalPoint);
        body.put("soLuongVoucher", voucher);


        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MovieFragment movieFragment = new MovieFragment();

                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.replaceFragment(movieFragment, true);
                }
            }
        });
        binding.addPoint.setText("Đã tích được " + pointAdd + " điểm");
        binding.succeesBtn.btnPrimary.setText("Xem chi tiết");

        updatePoint();

        binding.succeesBtn.btnPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TicketInfFragment ticketInfFragment = new TicketInfFragment();
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    Bundle bundle = new Bundle();

                    bundle.putString("maBook", maBook);
                    // Gán Bundle vào Fragment
                    ticketInfFragment.setArguments(bundle);
                    mainActivity.replaceFragment(ticketInfFragment, true);
                }
            }
        });


        return binding.getRoot();
    }

    private void updatePoint() {
        customerService.updatePoint(maKH, body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("UPDATE_POINT", "Cập nhật điểm thành công: " + response.body().toString());
                } else {
                    Log.e("UPDATE_POINT", "Cập nhật điểm thất bại: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.e("UPDATE_POINT", "Lỗi khi gọi API: " + t.getMessage(), t);
            }
        });
    }
}