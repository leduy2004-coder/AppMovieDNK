package com.example.appmoviednk.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.appmoviednk.MovieSession;
import com.example.appmoviednk.R;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.databinding.FragmentBookTicketBinding;
import com.example.appmoviednk.databinding.FragmentSuccessBinding;
import com.example.appmoviednk.model.MovieModel;

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

        binding.succeesBtn.btnPrimary.setText("Xem chi tiết");
        binding.succeesBtn.btnPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TicketInfFragment ticketInfFragment = new TicketInfFragment();
                MainActivity mainActivity =(MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.replaceFragment(ticketInfFragment);
                }
            }
        });


        return binding.getRoot();
    }
}