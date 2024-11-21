package com.example.appmoviednk.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appmoviednk.R;
import com.example.appmoviednk.UserSession;
import com.example.appmoviednk.adapter.ViewPagerAdapter;
import com.example.appmoviednk.databinding.ActivityMainBinding;
import com.example.appmoviednk.databinding.HeaderNavigationBinding;
import com.example.appmoviednk.fragment.AccountFragment;
import com.example.appmoviednk.fragment.BookTicketFragment;
import com.example.appmoviednk.fragment.HomeFragment;
import com.example.appmoviednk.fragment.LoginFragment;
import com.example.appmoviednk.fragment.MovieFragment;
import com.example.appmoviednk.fragment.RegisterFragment;
import com.example.appmoviednk.fragment.TrailerFragment;
import com.example.appmoviednk.fragment.VoucherFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  {
    //Không cần findViewById()
    ActivityMainBinding binding;
    PopupWindow popupWindow;
    ViewPagerAdapter viewPagerAdapter;
    List<Fragment> fragments = new ArrayList<>();
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

        replaceFragment(new HomeFragment(), true);

        // chỉ cần binding.id là đươc
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                replaceFragment(new HomeFragment(), true);
            } else if (item.getItemId() == R.id.bottom_ticket) {
                replaceFragment(new AccountFragment(), true);
            } else if (item.getItemId() == R.id.bottom_gift) {
                replaceFragment(new VoucherFragment(), true);
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
            replaceFragment(new HomeFragment(), true);
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
                replaceFragment(new HomeFragment(), true);
                binding.bottomNavigationView.setSelectedItemId(R.id.bottom_home);
            } else if (item.getItemId() == R.id.nav_calcu) {
                replaceFragment(new VoucherFragment(), true);
                binding.bottomNavigationView.setSelectedItemId(R.id.bottom_gift);
            } else if  (item.getItemId() == R.id.nav_accout) {
                replaceFragment(new AccountFragment(), true);
                binding.bottomNavigationView.setSelectedItemId(R.id.bottom_ticket);
            }
            drawerLayout.closeDrawer(findViewById(R.id.navigation));
            return true;
        });
    }




    public void replaceFragment(Fragment fragment, boolean isFromRight) {
        // Lấy FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Bắt đầu một giao dịch Fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Thiết lập animation
        if (isFromRight) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        // Thay thế Fragment hiện tại
        fragmentTransaction.replace(R.id.frameLayout, fragment);

        // Thêm giao dịch vào back stack
        fragmentTransaction.addToBackStack(null);

        // Cam kết giao dịch
        fragmentTransaction.commit();

    }


    private void showAccountPopup(View anchorView) {
        // Inflate layout từ file XML
        View popupView;
        if(UserSession.getInstance().getLoggedInAccount() == null){
            popupView = getLayoutInflater().inflate(R.layout.login_regis, null);
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
                replaceFragment(new LoginFragment(), true);
                popupWindow.dismiss();
            });

            registerButton.setText("Đăng ký");
            registerButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            // Gán sự kiện click cho nút Register
            registerButton.setOnClickListener(view -> {
                replaceFragment(new RegisterFragment(),true);
                popupWindow.dismiss();
            });
            int widthInPx = (int) (150 * getResources().getDisplayMetrics().density);

            // Lấy LayoutParams cho các nút
            ViewGroup.LayoutParams paramsLogin = loginButton.getLayoutParams();
            ViewGroup.LayoutParams paramsRegister = registerButton.getLayoutParams();

            // Gắn chiều dài 50dp cho cả hai nút
            paramsLogin.width = widthInPx;
            paramsRegister.width = widthInPx;

            loginButton.setLayoutParams(paramsLogin);
            registerButton.setLayoutParams(paramsRegister);

        }else {
             popupView = getLayoutInflater().inflate(R.layout.logout, null);
            // Tạo PopupWindow
            popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

            // Hiển thị PopupWindow
            popupWindow.showAsDropDown(anchorView, 0, 60);
            Button logoutButton = popupView.findViewById(R.id.logout).findViewById(R.id.btn_primary);

            logoutButton.setText("Đăng xuất");

            logoutButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            // Gán sự kiện click cho nút Login
            logoutButton.setOnClickListener(view -> {
                UserSession.getInstance().clearSession();
                replaceFragment(new LoginFragment(), true);
                popupWindow.dismiss();
                Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            });
            int widthInPx = (int) (150 * getResources().getDisplayMetrics().density);

            // Lấy LayoutParams cho các nút
            ViewGroup.LayoutParams paramsLogin = logoutButton.getLayoutParams();
            paramsLogin.width = widthInPx;

            logoutButton.setLayoutParams(paramsLogin);
        }

    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main);

        if (currentFragment instanceof BookTicketFragment) {
            Fragment movieFragment = new MovieFragment();
            replaceFragment(movieFragment, true); // Có hiệu ứng
        } else if (currentFragment instanceof TrailerFragment) {
            Fragment movieFragment = new MovieFragment();
            replaceFragment(movieFragment, true); // Không có hiệu ứng
        } else if (currentFragment instanceof LoginFragment) {
            Fragment bookticket = new BookTicketFragment();
            replaceFragment(bookticket, true); // Không có hiệu ứng
        }else {
                super.onBackPressed();
            }
        }
}