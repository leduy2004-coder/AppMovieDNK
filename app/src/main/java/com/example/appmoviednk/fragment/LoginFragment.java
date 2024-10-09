package com.example.appmoviednk.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.appmoviednk.R;
import com.example.appmoviednk.activity.MainActivity;


public class LoginFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Button button = view.findViewById(R.id.btn_login).findViewById(R.id.btn_primary);
        TextView txtRes = view.findViewById(R.id.link_register);

        txtRes.setOnClickListener(v -> {
            RegisterFragment registerFragment = new RegisterFragment();

            // Lấy MainActivity từ Fragment
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.replaceFragment(registerFragment); // Gọi phương thức replaceFragment

            }
        });

        // Thay đổi văn bản của button
        button.setText(getString(R.string.login));

        return view; // Trả về view đã sửa đổi
    }
}