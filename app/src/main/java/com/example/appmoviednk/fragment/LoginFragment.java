package com.example.appmoviednk.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.fragment.app.Fragment;

import com.example.appmoviednk.R;
import com.example.appmoviednk.UserSession;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.databinding.FragmentLoginBinding;

import com.example.appmoviednk.model.CustomerModel;
import com.example.appmoviednk.retrofit.RetrofitClient;
import com.example.appmoviednk.service.LoginService;

import org.json.JSONObject;

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

        binding= FragmentLoginBinding.inflate(inflater, container, false);


        checkLoginStatus();
        binding.btnLogin.btnPrimary.setText(getString(R.string.login));

        binding.linkRegister.setOnClickListener(v -> {
            RegisterFragment registerFragment = new RegisterFragment();

            // Lấy MainActivity từ Fragment
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.replaceFragment(registerFragment,true); // Gọi phương thức replaceFragment

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
            Toast.makeText(getContext(), "Vui lòng nhập tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("LoginFragment", "Username: " + tenTK + ", Password: " + password);

        // Tạo đối tượng CustomerModel với các tham số chính xác
        CustomerModel customerModel = new CustomerModel(tenTK, password);

        loginService.loginAccount(customerModel).enqueue(new Callback<CustomerModel>() {
            @Override
            public void onResponse(Call<CustomerModel> call, Response<CustomerModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserSession.getInstance().setLoggedInAccount(response.body());

                    SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if(binding.cbCheck.isChecked()){
                        editor.putString(KEY_EMAIL, tenTK);
                        editor.putString(KEY_PASSWORD, password);
                        editor.putBoolean(KEY_REMEMBER_ME,true);
                    } else {
                        editor.remove(KEY_EMAIL);
                        editor.remove(KEY_PASSWORD);
                        editor.putBoolean(KEY_REMEMBER_ME, false);
                    }
                    editor.apply();


                    changeFragment();
                    Toast.makeText(getContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CustomerModel> call, Throwable t) {
                Log.e("LoginFragment", "Lỗi khi gọi API: ", t);
                Toast.makeText(getContext(), "Lỗi kết nối, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkLoginStatus() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean rememberMe = sharedPreferences.getBoolean(KEY_REMEMBER_ME,false);
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

    private void changeFragment () {
        HomeFragment homeFragment = new HomeFragment();

        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.replaceFragment(homeFragment,true);

        }
    }






}