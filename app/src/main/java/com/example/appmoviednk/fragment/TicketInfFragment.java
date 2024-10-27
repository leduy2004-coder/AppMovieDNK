package com.example.appmoviednk.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appmoviednk.DateUtils;
import com.example.appmoviednk.R;
import com.example.appmoviednk.databinding.FragmentAccountBinding;
import com.example.appmoviednk.databinding.FragmentTicketInfBinding;
import com.example.appmoviednk.model.SharedViewModel;
import com.example.appmoviednk.retrofit.RetrofitClient;
import com.example.appmoviednk.service.ApiService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketInfFragment extends Fragment {
    FragmentTicketInfBinding binding;
    ApiService apiService;
    SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTicketInfBinding.inflate(inflater, container, false);


        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.getMaBook().observe(getViewLifecycleOwner(), maBook -> {
            if (maBook != null) {
                Log.d("SuccessFragment", "Ghế đã chọn: " + maBook);
                getTicketDetails(maBook);
            }
        });

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        if (getArguments() != null){

            getTicketDetails(getArguments().getString("maBook"));
        }
        return  binding.getRoot();
    }

    private void getTicketDetails (String maBook){


        apiService.getTicketDetails(maBook).enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Map<String, Object>> ticketDetails = response.body();
                    // Xử lý dữ liệu
                    for (Map<String, Object> ticket : ticketDetails) {
                        String tenPhim = (String) ticket.get("tenPhim");
                        String tenDaoDien = (String) ticket.get("daoDien");
                        String ngayMua = (String) ticket.get("ngayMua");
                        String formatDate = DateUtils.formatDateString(ngayMua);

                        // Lấy phần nguyên từ thoiLuong
                        String thoiLuong = "";
                        if (ticket.get("thoiLuong") instanceof Double) {
                            thoiLuong = String.valueOf((int) Math.floor((Double) ticket.get("thoiLuong")));
                        } else if (ticket.get("thoiLuong") instanceof Integer) {
                            thoiLuong = String.valueOf(ticket.get("thoiLuong"));
                        }

                        // Lấy phần nguyên từ soLuongVeDaDat
                        String tickets = "";
                        if (ticket.get("soLuongVeDaDat") instanceof Double) {
                            tickets = String.valueOf((int) Math.floor((Double) ticket.get("soLuongVeDaDat")));
                        } else if (ticket.get("soLuongVeDaDat") instanceof Integer) {
                            tickets = String.valueOf(ticket.get("soLuongVeDaDat"));
                        }

                        // Lấy phần nguyên từ maGhe (nếu maGhe có thể là số)
                        String seats = "";
                        if (ticket.get("maGhe") instanceof String) {
                            seats = (String) ticket.get("maGhe");
                        } else if (ticket.get("maGhe") instanceof Double) {
                            seats = String.valueOf((int) Math.floor((Double) ticket.get("maGhe")));
                        }

                        // Lấy phần nguyên từ tongTien
                        double tongTien = 0;
                        if (ticket.get("tongTien") instanceof Double) {
                            tongTien = (Double) ticket.get("tongTien");
                        } else if (ticket.get("tongTien") instanceof Integer) {
                            tongTien = ((Integer) ticket.get("tongTien")).doubleValue();
                        }
                        String stringValue = String.valueOf((int) Math.floor(tongTien)); // Lấy phần nguyên



                        // Cập nhật UI
                        binding.infName.setText(tenPhim);
                        binding.infDirector.setText(tenDaoDien);
                        binding.infDate.setText(formatDate);
                        binding.infDuration.setText(thoiLuong);
                        binding.totalTicket.setText("Số vé đã đặt: " + tickets);
                        binding.infSeats.setText("Ghế: " + seats);
                        binding.totalAmount.setText("Tổng tiền " + stringValue);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {

            }
        });
    }
}