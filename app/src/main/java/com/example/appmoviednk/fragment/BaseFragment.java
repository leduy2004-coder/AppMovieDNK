package com.example.appmoviednk.fragment;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    protected boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        return username != null;
    }
}
