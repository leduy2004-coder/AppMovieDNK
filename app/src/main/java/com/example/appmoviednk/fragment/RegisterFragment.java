package com.example.appmoviednk.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.appmoviednk.R;
import com.example.appmoviednk.activity.MainActivity;

public class RegisterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        Button button = view.findViewById(R.id.btn_register).findViewById(R.id.btn_primary);
        TextView txtRes = view.findViewById(R.id.link_login);

        txtRes.setOnClickListener(v -> {
            LoginFragment loginFragment = new LoginFragment();

            // Lấy MainActivity từ Fragment
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.replaceFragment(loginFragment); // Gọi phương thức replaceFragment
            }
        });
        // Thay đổi văn bản của button
        button.setText(getString(R.string.register));

        return view; // Trả về view đã sửa đổi
    }
}