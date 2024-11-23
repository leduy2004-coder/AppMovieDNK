package com.example.appmoviednk.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appmoviednk.DateUtils;
import com.example.appmoviednk.UserSession;
import com.example.appmoviednk.databinding.FragmentTicketInfBinding;
import com.example.appmoviednk.model.SharedViewModel;
import com.example.appmoviednk.retrofit.RetrofitClient;
import com.example.appmoviednk.service.MovieService;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketInfFragment extends Fragment {
    FragmentTicketInfBinding binding;
    MovieService apiService;
    SharedViewModel sharedViewModel;
    private String movieName, customerName, seats, totalAmount, tickets;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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

        apiService = RetrofitClient.getRetrofitInstance().create(MovieService.class);
        if (getArguments() != null){

            getTicketDetails(getArguments().getString("maBook"));
        }

        return  binding.getRoot();
    }

    private void getTicketDetails(String maBook) {
        apiService.getTicketDetails(maBook).enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(@NonNull Call<List<Map<String, Object>>> call, @NonNull Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Map<String, Object>> ticketDetails = response.body();
                    for (Map<String, Object> ticket : ticketDetails) {
                        movieName = (String) ticket.get("tenPhim");
                        customerName = UserSession.getInstance().getLoggedInAccount().getHoTen();
                        String ngayMua = (String) ticket.get("ngayMua");
                        String formatDate = DateUtils.formatDateString(ngayMua);

                        // Lấy phần nguyên từ thoiLuong
                        String thoiLuong = "";
                        if (ticket.get("thoiLuong") instanceof Double) {
                            thoiLuong = String.valueOf((int) Math.floor((Double) ticket.get("thoiLuong")));
                        } else if (ticket.get("thoiLuong") instanceof Integer) {
                            thoiLuong = String.valueOf(ticket.get("thoiLuong"));
                        }

                        // Lấy số vé
                        tickets = "";
                        if (ticket.get("soLuongVeDaDat") instanceof Double) {
                            tickets = String.valueOf((int) Math.floor((Double) ticket.get("soLuongVeDaDat")));
                        } else if (ticket.get("soLuongVeDaDat") instanceof Integer) {
                            tickets = String.valueOf(ticket.get("soLuongVeDaDat"));
                        }

                        // Lấy ghế
                        seats = "";
                        if (ticket.get("maGhe") instanceof String) {
                            seats = (String) ticket.get("maGhe");
                        } else if (ticket.get("maGhe") instanceof Double) {
                            seats = String.valueOf((int) Math.floor((Double) ticket.get("maGhe")));
                        }

                        // Lấy tổng tiền
                        double tongTien = 0;
                        if (ticket.get("tongTien") instanceof Double) {
                            tongTien = (Double) ticket.get("tongTien");
                        } else if (ticket.get("tongTien") instanceof Integer) {
                            tongTien = ((Integer) ticket.get("tongTien")).doubleValue();
                        }
                        totalAmount = String.valueOf((int) Math.floor(tongTien));

                        // Cập nhật UI
                        binding.infName.setText(movieName);
                        binding.infDirector.setText((String) ticket.get("daoDien"));
                        binding.infDate.setText(formatDate);
                        binding.infDuration.setText(thoiLuong);
                        binding.totalTicket.setText("Số vé đã đặt: " + tickets);
                        binding.infSeats.setText("Ghế: " + seats);
                        binding.totalAmount.setText("Tổng tiền: " + totalAmount);

                        // Tạo mã QR sau khi nhận đủ thông tin
                        generateQR();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Map<String, Object>>> call, @NonNull Throwable t) {
                Log.e("TicketDetails", "Lỗi khi gọi API: " + t.getMessage());
            }
        });
    }

    private void generateQR() {
        if (movieName != null && customerName != null && seats != null && totalAmount != null && tickets != null) {
            // Chuỗi thông tin để mã hóa
            String qrData = "Phim: " + movieName + "\n"
                    + "Khách hàng: " + customerName + "\n"
                    + "Ghế: " + seats + "\n"
                    + "Số vé: " + tickets + "\n"
                    + "Tổng tiền: " + totalAmount + " VND";

            MultiFormatWriter writer = new MultiFormatWriter();
            try {
                // Đảm bảo mã hóa UTF-8
                Map<EncodeHintType, Object> hints = new HashMap<>();
                hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

                BitMatrix matrix = writer.encode(qrData,
                        com.google.zxing.BarcodeFormat.QR_CODE,
                        500, 500, hints);

                BarcodeEncoder encoder = new BarcodeEncoder();
                Bitmap bitmap = encoder.createBitmap(matrix);
                binding.imgQr.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.e("GenerateQR", "Dữ liệu chưa đầy đủ để tạo mã QR.");
        }
    }

}