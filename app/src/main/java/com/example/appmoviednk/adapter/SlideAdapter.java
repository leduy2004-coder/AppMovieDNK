package com.example.appmoviednk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmoviednk.databinding.ItemCommentBinding;
import com.example.appmoviednk.databinding.SlideMovieHomeBinding;
import com.example.appmoviednk.model.MovieModel;

import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {
    private List<MovieModel> list;
    private Context mContext;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<MovieModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SlideMovieHomeBinding binding = SlideMovieHomeBinding.inflate(inflater, parent, false);
        return new SlideViewHolder(binding);
    }

    public SlideAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        int realPosition = position % list.size(); // Tính vị trí thực trong danh sách
        MovieModel cmt = list.get(realPosition);

        if (cmt == null) {
            return;
        }
        holder.slideMovieHomeBinding.imgSlide.setImageResource(cmt.getImageResource());
    }


    @Override
    public int getItemCount() {
        // Trả về số lớn để ViewPager có thể cuộn vô hạn
        if (list != null)
            return Integer.MAX_VALUE;
        return 0;
    }


    public class SlideViewHolder extends RecyclerView.ViewHolder {
        SlideMovieHomeBinding slideMovieHomeBinding;

        public SlideViewHolder(@NonNull SlideMovieHomeBinding binding) {
            super(binding.getRoot());
            this.slideMovieHomeBinding = binding; // Binding is set here
        }
    }

    private Runnable runnable = new Runnable() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void run() {
            notifyDataSetChanged();
        }
    };
}
