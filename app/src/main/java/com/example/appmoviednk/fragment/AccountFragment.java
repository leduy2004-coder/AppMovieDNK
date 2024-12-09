package com.example.appmoviednk.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.appmoviednk.UserSession;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.adapter.HistoryBookAdapter;
import com.example.appmoviednk.databinding.FragmentAccountBinding;
import com.example.appmoviednk.model.CustomerModel;
import com.example.appmoviednk.retrofit.RetrofitClient;
import com.example.appmoviednk.service.CustomerService;
import com.example.appmoviednk.service.LoginService;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends Fragment {
    FragmentAccountBinding binding;
    HistoryBookAdapter historyBookAdapter;
    LoginService loginService;
    CustomerService customerService;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        historyBookAdapter = new HistoryBookAdapter(requireContext());

        // Set data to adapter and bind to RecyclerView

        CustomerModel loggedInAccount = UserSession.getInstance().getLoggedInAccount();
        if(loggedInAccount != null  ){
            String maKH = loggedInAccount.getMaKH();
            fetchCustomerInf(maKH);
            fetchCustomerHistory(maKH);
            binding.loggedIn.setVisibility(View.VISIBLE);
        } else {
            styleBtn();
            activeInLoggedOut();
            binding.loggedOut.setVisibility(View.VISIBLE);

        }
        binding.accoutBtn.btnPrimary.setText("Xem thêm");


        // Return the root view from binding
        return binding.getRoot();
    }

    private void styleBtn(){
        Button btnLogin = binding.btnLogin.btnPrimary;

        btnLogin.setText("Đăng nhập");

        ViewGroup.LayoutParams loginParams = btnLogin.getLayoutParams();

        int widthInPx = (int) (140 * getResources().getDisplayMetrics().density);
        int heightInPx = (int) (55 * getResources().getDisplayMetrics().density);
        int marginInPx = (int) (16 * getResources().getDisplayMetrics().density);

        // Gắn chiều dài 50dp cho cả hai nút
        loginParams.width = widthInPx;

        loginParams.height = heightInPx;

        if (loginParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) loginParams;

            // Đặt margin (16dp cho tất cả các cạnh)
            marginParams.setMargins(marginInPx, marginInPx, marginInPx, marginInPx);
            btnLogin.setLayoutParams(marginParams);
        }
    }

    private void activeInLoggedOut(){
        Toast.makeText(getContext(), "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
        binding.btnLogin.btnPrimary.setOnClickListener(view -> {
            LoginFragment loginFragment = new LoginFragment();
            Bundle args = new Bundle();
            args.putBoolean("returnToAccount", true);
            loginFragment.setArguments(args);

            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.replaceFragment(loginFragment, true);
            }
        });
    }

    //get thông tin tài khoản
    private void fetchCustomerInf(String maKH){
        customerService = RetrofitClient.getRetrofitInstance().create(CustomerService.class);
        customerService.getInfCustomer(maKH).enqueue(new Callback<CustomerModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<CustomerModel> call, @NonNull Response<CustomerModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CustomerModel customerModel = response.body();
                    Log.d("diem thuong", "onResponse: " + customerModel.getDiemThuong());
                    binding.accountName.setText("Họ tên: " + customerModel.getHoTen());
                    binding.accountPoint.setText("Điểm: " + customerModel.getDiemThuong());
                    binding.accountVoucher.setText("Voucher: " + customerModel.getSoLuongVoucher());
                    binding.accountEmail.setText("Email: " + customerModel.getEmail());
                    binding.accountPhone.setText("SDT: " + customerModel.getSdt());
                } else {
                    Log.e("API_ERROR", "Không lấy được thông tin khách hàng " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomerModel> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Lỗi khi gọi API: " + t.getMessage(), t);
            }
        });


    }
    //get ra lịch sử đặt vé
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
                        Log.d("history list", "onResponse: " + historyList.size());
                        binding.accoutBtn.btnPrimary.setVisibility(View.GONE);
                        binding.table.setVisibility(View.GONE);
                        binding.isEmpty.setVisibility(View.VISIBLE);
                        return; // Kết thúc luôn nếu dữ liệu trống
                    }
                    // Cập nhật dữ liệu vào adapter

                    HistoryBookAdapter adapter = new HistoryBookAdapter(getContext());
                    adapter.setData(historyList);

                    binding.listBooked.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.listBooked.setAdapter(adapter);

                    if (historyList.size() > 3) {
                        binding.moreItems.setVisibility(View.VISIBLE); // Hiển thị dấu ...

                    } else {
                        binding.accoutBtn.btnPrimary.setVisibility(View.GONE);
                    }
                    // Bắt sự kiện khi nhấn vào nút "accout_btn" - xem lịch sử
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
                            binding.moreItems.setVisibility(View.GONE); //ẩn ...

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


}