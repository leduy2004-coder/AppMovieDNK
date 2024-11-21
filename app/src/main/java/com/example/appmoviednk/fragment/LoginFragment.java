package com.example.appmoviednk.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.appmoviednk.R;
import com.example.appmoviednk.UserSession;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.databinding.FragmentLoginBinding;

import com.example.appmoviednk.model.CustomerModel;
import com.example.appmoviednk.retrofit.RetrofitClient;
import com.example.appmoviednk.service.LoginService;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {
    EditText inputEmail;
    EditText inputPass;
    FragmentLoginBinding binding;
    LoginService loginService;

    private static final String PREFS_NAME = "User";
    private static final String KEY_EMAIL = "tk";
    private static final String KEY_PASSWORD = "mk";
    private static final String KEY_REMEMBER_ME = "remember_me";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);


        checkLoginStatus();
        binding.btnLogin.btnPrimary.setText(getString(R.string.login));

        binding.linkRegister.setOnClickListener(v -> {
            RegisterFragment registerFragment = new RegisterFragment();

            // Lấy MainActivity từ Fragment
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.replaceFragment(registerFragment, true); // Gọi phương thức replaceFragment
            }
        });
        loginService = RetrofitClient.getRetrofitInstance().create(LoginService.class);

        binding.btnLogin.btnPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();

            }
        });

        return binding.getRoot(); // Trả về view đã sửa đổi
    }

    private void login() {
        String tenTK = binding.editEmail.getText().toString().trim();
        String password = binding.editPass.getText().toString().trim();

        if (tenTK.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("LoginFragment", "Username: " + tenTK + ", Password: " + password);

        // Tạo đối tượng CustomerModel với các tham số chính xác
        CustomerModel customerModel = new CustomerModel(tenTK, password);

        loginService.loginAccount(customerModel).enqueue(new Callback<CustomerModel>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NonNull Call<CustomerModel> call, @NonNull Response<CustomerModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CustomerModel newCustomerModel = response.body();
                    if (newCustomerModel.getNgayDiemDanhCuoi() != null) {
                        newCustomerModel.setNgay(newCustomerModel.getNgayDiemDanhCuoi()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate());
                    } else {
                        newCustomerModel.setNgay(null);
                    }

                    UserSession.getInstance().setLoggedInAccount(newCustomerModel);

                    SharedPreferences sharedPreferences = getContext() != null
                            ? getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                            : null;

                    if (sharedPreferences != null) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (binding.cbCheck.isChecked()) {
                            editor.putString(KEY_EMAIL, tenTK);
                            editor.putString(KEY_PASSWORD, password);
                            editor.putBoolean(KEY_REMEMBER_ME, true);
                        } else {
                            editor.remove(KEY_EMAIL);
                            editor.remove(KEY_PASSWORD);
                            editor.putBoolean(KEY_REMEMBER_ME, false);
                        }
                        editor.apply();
                    }

                    changeFragment();
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        Log.d("thanh conggg", "ỵtanhh congg ");
                    }

                } else {
                    String message = "Sai tài khoản hoặc mật khẩu";
                    if (response.errorBody() != null) {
                        try {
                            JSONObject jsonError = new JSONObject(response.errorBody().string());
                            message = jsonError.getString("message");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (getContext() != null) {
                        Log.d("thattt bvaiii", "ythattt bvaiii ");
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerModel> call, Throwable t) {
                Log.e("LoginFragment", "Lỗi khi gọi API: ", t);
                Log.d("thattt bvaiii api", "ythattt bvaiii ");
                Toast.makeText(getContext(), "Lỗi kết nối, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkLoginStatus() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean rememberMe = sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
        if (rememberMe) {
            String email = sharedPreferences.getString(KEY_EMAIL, null);
            String password = sharedPreferences.getString(KEY_PASSWORD, null);

            if (email != null && password != null) {
                binding.editEmail.setText(email); // Hiển thị email
                binding.editPass.setText(password); // Hiển thị mật khẩu
                binding.cbCheck.setChecked(true); // Đánh dấu CheckBox
            }
        }
    }

    private void changeFragment() {
        boolean returnToBookTicket = getArguments() != null && getArguments().getBoolean("returnToBookTicket", false);
        boolean returnToTrailer = getArguments() != null && getArguments().getBoolean("returnToTrailer", false);
        boolean returnToVoucher = getArguments() != null && getArguments().getBoolean("returnToVoucher", false);
        MainActivity mainActivity = (MainActivity) getActivity();


        if (returnToBookTicket || returnToTrailer) {
            mainActivity = (MainActivity) getActivity();
            if (mainActivity != null && mainActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                mainActivity.onBackPressed(); // Quay lại nếu có Fragment trong ngăn xếp
            }
        } else if (returnToVoucher) {
            assert mainActivity != null;
            mainActivity.replaceFragment(new VoucherFragment(), true);
        } else {
            if (mainActivity != null) {
                mainActivity.replaceFragment(new HomeFragment(), true);

            }
        }
    }


}