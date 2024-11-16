package com.example.appmoviednk.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmoviednk.R;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.databinding.FragmentLoginBinding;
import com.example.appmoviednk.databinding.FragmentRegisterBinding;
import com.example.appmoviednk.model.CustomerModel;
import com.example.appmoviednk.retrofit.RetrofitClient;
import com.example.appmoviednk.service.RegisterService;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {
    FragmentRegisterBinding binding;
    RegisterService registerService;
    CustomerModel customerModel;

    String verificationCode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        binding.linkLogin.setOnClickListener(v -> {
            LoginFragment loginFragment = new LoginFragment();

            // Lấy MainActivity từ Fragment
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.replaceFragment(loginFragment, true); // Gọi phương thức replaceFragment
            }
        });

        registerService = RetrofitClient.getRetrofitInstance().create(RegisterService.class);

        // Thay đổi văn bản của button
        binding.btnRegister.btnPrimary.setText(getString(R.string.register));
        binding.btnRegister.btnPrimary.setOnClickListener(view -> {
            String email = binding.regisEmail.getText().toString();
            String name = binding.regisPhone.getText().toString();
            String phone = binding.regisHoten.getText().toString();
            String account = binding.regisAccoutName.getText().toString();
            String pass = binding.regisPass.getText().toString();
            if (!email.isEmpty() && !name.isEmpty() && !phone.isEmpty() && !account.isEmpty() && !pass.isEmpty()) {
                sendVerificationCode(email); // Gửi mã xác minh qua email
                showVerificationDialog();   // Hiện hộp thoại xác nhận
            } else {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }

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

    private void showVerificationDialog() {
        // Tạo một hộp thoại
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_verify_code, null);
        builder.setView(dialogView);

        // Khai báo các thành phần trong hộp thoại
        EditText editTextCode = dialogView.findViewById(R.id.editTextCode);
        Button btnConfirm = dialogView.findViewById(R.id.btn_primary);
        btnConfirm.setText("Xác nhận");
        // Tạo hộp thoại
        AlertDialog dialog = builder.create();

        // Xử lý sự kiện khi nhấn nút xác nhận
        btnConfirm.setOnClickListener(v -> {
            String verificationCode = editTextCode.getText().toString().trim();
            if (!verificationCode.isEmpty()) {
                // Gọi hàm để xác thực mã (giả sử có hàm verifyCode)
                verifyCode(binding.regisEmail.getText().toString(), verificationCode, dialog);

            } else {
                Toast.makeText(getContext(), "Vui lòng nhập mã xác thực", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show(); // Hiện hộp thoại
    }

    private void verifyCode(String email, String verificationCode, AlertDialog dialog) {
        // Tạo một đối tượng HashMap để gửi dữ liệu
        HashMap<String, String> codeMap = new HashMap<>();
        codeMap.put("email", email);
        codeMap.put("code", verificationCode);

        // Gọi API xác thực mã xác nhận
        registerService.verifyCode(codeMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Mã xác nhận hợp lệ!", Toast.LENGTH_SHORT).show();

                    dataRegister();
                    register();
                    dialog.dismiss();

                    changeFragment();
                } else {
                    Toast.makeText(getContext(), "Mã xác nhận không hợp lệ hoặc đã hết hạn.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dataRegister() {
        String hoTen = binding.regisHoten.getText().toString(); // Sửa ở đây
        String email = binding.regisEmail.getText().toString(); // Sửa ở đây
        String tenTK = binding.regisAccoutName.getText().toString(); // Sửa ở đây
        String matKhau = binding.regisPass.getText().toString(); // Sửa ở đây
        String sdt = binding.regisPhone.getText().toString();
        customerModel = new CustomerModel(email, hoTen, tenTK, matKhau, sdt);
    }

    private void register() {
        registerService.registerUser(customerModel).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                } else {
                    // In ra phản hồi chi tiết
                    try {
                        String errorResponse = response.errorBody().string();
                        Log.e("RegisterFragment", "Error response: " + errorResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void changeFragment() {
        LoginFragment loginFragment = new LoginFragment();

        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.replaceFragment(loginFragment, true);

        }
    }


}