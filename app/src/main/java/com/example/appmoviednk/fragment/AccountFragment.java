package com.example.appmoviednk.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appmoviednk.R;
import com.example.appmoviednk.adapter.CommentAdapter;
import com.example.appmoviednk.adapter.HistoryBookAdapter;
import com.example.appmoviednk.databinding.FragmentAccountBinding;
import com.example.appmoviednk.databinding.FragmentTrailerBinding;
import com.example.appmoviednk.model.BookTicketModel;
import com.example.appmoviednk.model.CustomerModel;

import java.util.ArrayList;
import java.util.List;


public class AccountFragment extends Fragment {
    FragmentAccountBinding binding;
    HistoryBookAdapter historyBookAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        historyBookAdapter = new HistoryBookAdapter(requireContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1);
        binding.listBooked.setLayoutManager(gridLayoutManager);

        // Set data to adapter and bind to RecyclerView
        historyBookAdapter.setData(getListHistory());
        binding.listBooked.setAdapter(historyBookAdapter);

        // Return the root view from binding
        return binding.getRoot();
    }
    private List<BookTicketModel> getListHistory() {
        List<BookTicketModel> list = new ArrayList<>();
        list.add(new BookTicketModel("123","444444444444444444444444",442323232));
        list.add(new BookTicketModel("123","444444444444444444444444",442323232));
        list.add(new BookTicketModel("123","444444444444444444444444",442323232));

        return list;
    }

}