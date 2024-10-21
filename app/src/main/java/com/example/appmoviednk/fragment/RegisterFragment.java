package com.example.appmoviednk.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmoviednk.R;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.databinding.FragmentLoginBinding;
import com.example.appmoviednk.databinding.FragmentRegisterBinding;
import com.example.appmoviednk.retrofit.RetrofitClient;
import com.example.appmoviednk.service.RegisterService;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {
    FragmentRegisterBinding binding;
    RegisterService registerService;

    String verificationCode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentRegisterBinding.inflate(inflater, container, false);

        binding.linkLogin.setOnClickListener(v -> {
            LoginFragment loginFragment = new LoginFragment();

            // Lấy MainActivity từ Fragment
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.replaceFragment(loginFragment); // Gọi phương thức replaceFragment
            }
        });

        registerService = RetrofitClient.getRetrofitInstance().create(RegisterService.class);

        // Thay đổi văn bản của button
        binding.btnRegister.btnPrimary.setText(getString(R.string.register));
        binding.btnRegister.btnPrimary.setOnClickListener(view -> {
            String email = binding.regisEmail.getText().toString();
            sendVerificationCode(email);
        });


        return binding.getRoot(); // Trả về view đã sửa đổi
    }

    // Hàm gửi mã xác thực đến email
    private void sendVerificationCode(String email) {
        // Giả sử chúng ta tạo mã xác thực ngẫu nhiên 6 chữ số
        HashMap<String, String> emailMap = new HashMap<>();
        emailMap.put("email", email);

        registerService.sendVerification(emailMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Mã xác nhận đã được gửi tới email!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Lỗi khi gửi mã xác nhận: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

}