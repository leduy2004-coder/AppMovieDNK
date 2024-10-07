package com.example.appmoviednk.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.appmoviednk.R;
import com.example.appmoviednk.adapter.CardAdapter;
import com.example.appmoviednk.databinding.FragmentShowingMovieHomeBinding;
import com.example.appmoviednk.databinding.FragmentUpcomingMovieHomeBinding;
import com.example.appmoviednk.model.MovieModel;

import java.util.ArrayList;
import java.util.List;


public class UpcomingMovieHomeFragment extends Fragment {

    FragmentUpcomingMovieHomeBinding binding;
    CardAdapter cardAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout using ViewBinding
        binding = FragmentUpcomingMovieHomeBinding.inflate(inflater, container, false);

        // Initialize adapter and layout manager
        cardAdapter = new CardAdapter(requireContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        binding.rcvShowingMovie.setLayoutManager(gridLayoutManager);

        // Set data to adapter and bind to RecyclerView
        cardAdapter.setData(getListMovie());
        binding.rcvShowingMovie.setAdapter(cardAdapter);

        // Return the root view from binding
        return binding.getRoot();
    }

    // Mock data for movies
    private List<MovieModel> getListMovie() {
        List<MovieModel> list = new ArrayList<>();

        list.add(new MovieModel("p1", "Chua bao gio", R.drawable.img_phim1));
        list.add(new MovieModel("p2", "Hay la la", R.drawable.img_phim2));
        list.add(new MovieModel("p3", "Toi la ai", R.drawable.img_phim3));

        return list;
    }
}