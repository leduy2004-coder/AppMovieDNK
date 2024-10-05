package com.example.appmoviednk.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appmoviednk.fragment.BookTicketFragment;
import com.example.appmoviednk.fragment.HomeFragment;
import com.example.appmoviednk.R;
import com.example.appmoviednk.fragment.VoucherFragment;
import com.example.appmoviednk.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {
    //không cần findViewById()
    ActivityMainBinding binding;
    ImageView btnMenu;
    ImageView exitNav;
    NavigationView nvgt;
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
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            }else if (item.getItemId() == R.id.ticket) {
                replaceFragment(new BookTicketFragment());
            }else if (item.getItemId() == R.id.gift) {
                replaceFragment(new VoucherFragment());
            }

            return true;
        });

        ImageView imgAccount = findViewById(R.id.img_account);
        ImageView imgLogo = findViewById(R.id.img_logo);
        ImageView imgMenu = findViewById(R.id.img_menu);

        // Thiết lập sự kiện click cho imgAccount
        imgAccount.setOnClickListener(v -> {

        });

        // Thiết lập sự kiện click cho imgLogo
        imgLogo.setOnClickListener(v -> {
            replaceFragment(new HomeFragment());
            binding.bottomNavigationView.setSelectedItemId(R.id.home);
        });

        // Thiết lập sự kiện click cho imgMenu
        imgMenu.setOnClickListener(v -> {

        });

        btnMenu = findViewById(R.id.img_menu);
        nvgt = findViewById(R.id.navigation);
        View headerView = binding.navigation.getHeaderView(0);
        exitNav = headerView.findViewById(R.id.cancelButton);
        drawerLayout = findViewById(R.id.main);
        if (exitNav == null) {
            Log.e("MainActivity", "exitNav is null");
        }
        btnMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(nvgt)) {
                    drawerLayout.closeDrawer(nvgt);
                } else {
                    drawerLayout.openDrawer(nvgt);
                }
            }
        });
        exitNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng navigation drawer nếu nó đang mở

                    drawerLayout.closeDrawer(findViewById(R.id.navigation));
            }
        });
        binding.navigation.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home){
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.nav_calcu){
                replaceFragment(new VoucherFragment());
            }
            drawerLayout.closeDrawer(findViewById(R.id.navigation));
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
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


}