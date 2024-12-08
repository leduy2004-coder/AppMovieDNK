package com.example.appmoviednk.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.appmoviednk.R;
import com.example.appmoviednk.UserSession;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.adapter.VoucherAdapter;
import com.example.appmoviednk.databinding.FragmentVoucherBinding;
import com.example.appmoviednk.retrofit.RetrofitClient;
import com.example.appmoviednk.service.VoucherService;
import com.google.gson.JsonObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoucherFragment extends Fragment {
    FragmentVoucherBinding binding;
    VoucherAdapter voucherAdapter;
    VoucherService voucherService;
    int attendance = 0;  //Kiểm tra đã điểm danh trog ngày chưa
    LocalDate selectedDay;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentVoucherBinding.inflate(inflater, container, false);
        voucherService = RetrofitClient.getRetrofitInstance().create(VoucherService.class);

        binding.voucherBtn.btnPrimary.setText("Điểm danh");

        if (UserSession.getInstance().getLoggedInAccount() == null) {
            diaLogLogin();
        } else {
            attendance = UserSession.getInstance().getLoggedInAccount().getDiemDanh();
            selectedDay = UserSession.getInstance().getLoggedInAccount().getNgay();
        }

        // set data
        List<String> items = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            items.add("Ngày " + i);
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
        binding.viewVoucher.setLayoutManager(gridLayoutManager);

        voucherAdapter = new VoucherAdapter(requireContext(), items, attendance);
        binding.viewVoucher.setAdapter(voucherAdapter);

        binding.voucherBtn.btnPrimary.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (UserSession.getInstance().getLoggedInAccount() == null) {
                    diaLogLogin();
                } else {
                    // Lấy ngày hiện tại
                    LocalDate currentDay = LocalDate.now();

                    // Kiểm tra nếu người dùng đã điểm danh ngày này
                    if (selectedDay == null || !currentDay.isEqual(selectedDay)) {
                        // Nếu chưa điểm danh, tiếp tục điểm danh và cập nhật

                        UserSession.getInstance().getLoggedInAccount().setDiemDanh(attendance + 1);
                        UserSession.getInstance().getLoggedInAccount().setNgay(currentDay);
                        selectedDay = currentDay;
                        updateApi();


                        SpannableString title = new SpannableString("Chúc Mừng");
                        title.setSpan(new ForegroundColorSpan(Color.RED), 0, title.length(), 0);

                        // Tạo AlertDialog để thông báo điểm danh thành công
                        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).setIcon(R.drawable.ic_baseline_notifications_active_24).setTitle(title).setMessage("Bạn đã điểm danh ngày " + currentDay + " thành công!").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

                    } else {
                        // Nếu đã điểm danh, thông báo
                        SpannableString title = new SpannableString("Thông Báo");
                        title.setSpan(new ForegroundColorSpan(Color.RED), 0, title.length(), 0);

                        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).setIcon(R.drawable.ic_baseline_notifications_active_24).setTitle(title).setMessage("Bạn đã điểm danh hôm nay rồi!").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                    }
                }

            }
        });


        return binding.getRoot();
    }

    private void updateApi() {
        voucherService.updateVoucher(UserSession.getInstance().getLoggedInAccount()).enqueue(new Callback<JsonObject>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    attendance++;
                    voucherAdapter.updateAttendance(attendance);
                } else {
                    Log.d("Book", "Response body is null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d("API Error ma suat", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    // Hiện dialog yêu cầu đăng nhập
    private void diaLogLogin() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Đăng nhập")
                .setMessage("Bạn cần đăng nhập để điểm danh. ")
                .setPositiveButton("Đăng nhập", (dialog, which) -> {
                    LoginFragment loginFragment = new LoginFragment();
                    Bundle args = new Bundle();
                    args.putBoolean("returnToVoucher", true);
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