package com.example.appmoviednk.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.adapter.CommentAdapter;
import com.example.appmoviednk.databinding.FragmentTrailerBinding;
import com.example.appmoviednk.model.CustomerModel;

import java.util.ArrayList;
import java.util.List;

public class TrailerFragment extends Fragment {
    FragmentTrailerBinding binding;
    CommentAdapter cmtAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentTrailerBinding.inflate(inflater, container, false);

        // Initialize adapter and layout manager
        cmtAdapter = new CommentAdapter(requireContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1);
        binding.listCmt.setLayoutManager(gridLayoutManager);

        // Set data to adapter and bind to RecyclerView
        cmtAdapter.setData(getListComment());
        binding.listCmt.setAdapter(cmtAdapter);


        binding.imgPrev.setOnClickListener(v -> {
            MovieFragment movieFragment = new MovieFragment();
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.replaceFragment(movieFragment);

            }
        });
        // Return the root view from binding
        return binding.getRoot();
    }

    private List<CustomerModel> getListComment() {
        List<CustomerModel> list = new ArrayList<>();
        list.add(new CustomerModel("123", "44444444444444444444444444"));
        list.add(new CustomerModel("123", "444"));
        list.add(new CustomerModel("123", "444"));
        list.add(new CustomerModel("123", "444"));

        return list;
    }
}

