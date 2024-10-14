package com.example.appmoviednk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appmoviednk.R;
import com.example.appmoviednk.databinding.ActivityMainBinding;
import com.example.appmoviednk.databinding.HeaderNavigationBinding;
import com.example.appmoviednk.databinding.LoginRegisBinding;
import com.example.appmoviednk.fragment.AccountFragment;
import com.example.appmoviednk.fragment.BookTicketFragment;
import com.example.appmoviednk.fragment.HomeFragment;
import com.example.appmoviednk.fragment.LoginFragment;
import com.example.appmoviednk.fragment.RegisterFragment;
import com.example.appmoviednk.fragment.TrailerFragment;
import com.example.appmoviednk.fragment.VoucherFragment;


public class MainActivity extends AppCompatActivity {
    //Không cần findViewById()
    ActivityMainBinding binding;
    PopupWindow popupWindow;

    // Đặt tên HeaderNavigationBinding vì phải đặt giống tên của xml
    HeaderNavigationBinding headerBinding;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Tạo đối tượng binding từ layout XML
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // Gán root view của layout vào Activity
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());

        // chỉ cần binding.id là đươc
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.bottom_ticket) {
                replaceFragment(new AccountFragment());
            } else if (item.getItemId() == R.id.bottom_gift) {
                replaceFragment(new VoucherFragment());
            }
            return true;
        });

        // Thiết lập sự kiện click cho imgAccount
        ImageView imgAccount = findViewById(R.id.img_account);
        imgAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị layout với các nút login và register
                showAccountPopup(v);
            }
        });

        // Thiết lập sự kiện click cho imgLogo
        binding.imgLogo.setOnClickListener(v -> {
            replaceFragment(new HomeFragment());
            binding.bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        });

        drawerLayout = binding.main;

        // Thiết lập sự kiện click cho imgMenu
        binding.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(binding.navigation)) {
                    drawerLayout.closeDrawer(binding.navigation);
                } else {
                    drawerLayout.openDrawer(binding.navigation);
                }
            }
        });

        headerBinding = HeaderNavigationBinding.bind(binding.navigation.getHeaderView(0));
        headerBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng navigation drawer nếu nó đang mở
                drawerLayout.closeDrawer(binding.navigation);
            }
        });
        binding.navigation.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                replaceFragment(new HomeFragment());
                binding.bottomNavigationView.setSelectedItemId(R.id.bottom_home);
            } else if (item.getItemId() == R.id.nav_calcu) {
                replaceFragment(new VoucherFragment());
                binding.bottomNavigationView.setSelectedItemId(R.id.bottom_gift);
            } else if  (item.getItemId() == R.id.nav_accout) {
                replaceFragment(new AccountFragment());
                binding.bottomNavigationView.setSelectedItemId(R.id.bottom_ticket);
            }
            drawerLayout.closeDrawer(findViewById(R.id.navigation));
            return true;
        });
    }

    public void replaceFragment(Fragment fragment) {
        // FragmentManager là thành phần quan trọng giúp bạn thực hiện các thao tác thêm, thay thế, hoặc loại bỏ Fragment.
        FragmentManager fragmentManager = getSupportFragmentManager();

        // FragmentTransaction cho phép bạn thực hiện nhiều thay đổi trên Fragment một cách an toàn và hiệu quả.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Thay thế Fragment hiện tại trong container (ở đây là R.id.frameLayout) bằng Fragment mới
        // Điều này giúp cập nhật giao diện mà không cần phải tạo lại Activity.
        fragmentTransaction.replace(R.id.frameLayout, fragment);

        // Gọi commit() để áp dụng tất cả các thay đổi đã chỉ định trong giao dịch.
        fragmentTransaction.commit();
    }

    private void showAccountPopup(View anchorView) {
        // Inflate layout từ file XML
        View popupView = getLayoutInflater().inflate(R.layout.login_regis, null);

        // Tạo PopupWindow
        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        // Hiển thị PopupWindow
        popupWindow.showAsDropDown(anchorView, 0, 60);

        // Lấy button từ layout của button_primary
        Button loginButton = popupView.findViewById(R.id.login).findViewById(R.id.btn_primary);
        Button registerButton = popupView.findViewById(R.id.register).findViewById(R.id.btn_primary);

        loginButton.setText("Đăng nhập");
        loginButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        // Gán sự kiện click cho nút Login
        loginButton.setOnClickListener(view -> {
            replaceFragment(new LoginFragment());
            popupWindow.dismiss();
        });

        registerButton.setText("Đăng ký");
        registerButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        // Gán sự kiện click cho nút Register
        registerButton.setOnClickListener(view -> {
            replaceFragment(new RegisterFragment());
            popupWindow.dismiss();
        });

    }

}