package com.example.appmoviednk.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.appmoviednk.R;
import com.example.appmoviednk.UserSession;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.databinding.FragmentLoginBinding;

import com.example.appmoviednk.model.CustomerModel;
import com.example.appmoviednk.retrofit.RetrofitClient;
import com.example.appmoviednk.service.LoginService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {
    EditText inputEmail;
    EditText inputPass;
    FragmentLoginBinding binding;
    LoginService loginService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentLoginBinding.inflate(inflater, container, false);


        binding.btnLogin.btnPrimary.setText(getString(R.string.login));

        binding.linkRegister.setOnClickListener(v -> {
            RegisterFragment registerFragment = new RegisterFragment();

            // Lấy MainActivity từ Fragment
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.replaceFragment(registerFragment); // Gọi phương thức replaceFragment

            }
        });
        loginService = RetrofitClient.getRetrofitInstance().create(LoginService.class);

        binding.btnLogin.btnPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
                Log.e("ne",binding.editEmail.getText().toString());
            }
        });

        return binding.getRoot(); // Trả về view đã sửa đổi
    }

    private void login() {
        String email = binding.editEmail.getText().toString().trim();
        String password = binding.editPass.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        loginService.getAllAccounts().enqueue(new Callback<List<CustomerModel>>() {
            @Override
            public void onResponse(Call<List<CustomerModel>> call, Response<List<CustomerModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CustomerModel> account = response.body();
                    CustomerModel loggedInAccount = authenticateUser(account, email, password);

                    if (loggedInAccount != null) {
                        // Lưu thông tin tài khoản vào UserSession
                        UserSession.getInstance().setLoggedInAccount(loggedInAccount);
                        Toast.makeText(getContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi khi nhận dữ liệu từ máy chủ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CustomerModel>> call, Throwable t) {
                Log.e("LoginFragment", "Lỗi khi gọi API: ", t);
                Toast.makeText(getContext(), "Lỗi kết nối, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private CustomerModel authenticateUser(List<CustomerModel> customers, String email, String password) {
        for (CustomerModel customer : customers) {
            String tentk = customer.getTentk();
            String matKhau = customer.getMatKhau();

            Log.d("AccountInfo", "ten Tk: " + tentk + ", mk: " + matKhau);

            // Kiểm tra null trước khi so sánh
            if (tentk != null && tentk.equals(email) && matKhau != null && matKhau.equals(password)) {
                return customer; // Trả về tài khoản đã đăng nhập
            }
        }
        return null; // Trả về null nếu không tìm thấy tài khoản
    }

}