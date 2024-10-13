package com.example.appmoviednk.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.appmoviednk.R;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.databinding.FragmentBookTicketBinding;
import com.example.appmoviednk.databinding.FragmentSuccessBinding;

public class SuccessFragment extends Fragment {
    FragmentSuccessBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSuccessBinding.inflate(inflater, container, false);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MovieFragment movieFragment = new MovieFragment();

                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.replaceFragment(movieFragment);
                }
            }
        });
        return binding.getRoot();
    }
}