package com.example.appmoviednk.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appmoviednk.fragment.AccountFragment;
import com.example.appmoviednk.fragment.HomeFragment;
import com.example.appmoviednk.fragment.VoucherFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private List<Fragment> fragments;

    public ViewPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
        fragments = new ArrayList<>();
    }

//    @NonNull
//    @Override
//    public Fragment createFragment(int position) {
//        // Tạo và trả về Fragment tương ứng với vị trí
//        switch (position) {
//            case 0:
//                return new HomeFragment();
//            case 1:
//                return new AccountFragment();
//            case 2:
//                return new VoucherFragment();
//            default:
//                return new HomeFragment();
//        }
//    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("YourFragmentAdapter", "Creating fragment for position: " + position);
        return fragments.get(position);
    }

    // Hàm cập nhật danh sách Fragment mới
    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
