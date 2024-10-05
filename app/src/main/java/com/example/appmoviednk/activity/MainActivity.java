package com.example.appmoviednk.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appmoviednk.fragment.BookTicketFragment;
import com.example.appmoviednk.fragment.HomeFragment;
import com.example.appmoviednk.R;
import com.example.appmoviednk.fragment.VoucherFragment;
import com.example.appmoviednk.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    //không cần findViewById()
    ActivityMainBinding binding;

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