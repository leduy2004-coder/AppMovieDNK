package com.example.appmoviednk.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appmoviednk.fragment.ShowingMovieHomeFragment;
import com.example.appmoviednk.fragment.UpcomingMovieHomeFragment;

public class TagViewMovieAdapter extends FragmentStateAdapter {
    private int totalTabs;
    public TagViewMovieAdapter(FragmentActivity fa, int totalTabs) {
        super(fa);
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Trả về fragment tương ứng với tab tại vị trí này
        switch (position) {
            case 0:
                return new ShowingMovieHomeFragment();
            case 1:
                return new UpcomingMovieHomeFragment();
            default:
                return new ShowingMovieHomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return totalTabs; // Số lượng tab
    }
}
