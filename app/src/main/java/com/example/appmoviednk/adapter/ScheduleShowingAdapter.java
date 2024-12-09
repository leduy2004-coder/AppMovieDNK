package com.example.appmoviednk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmoviednk.DateUtils;
import com.example.appmoviednk.databinding.ItemScheduleShowingMovieBinding;
import com.example.appmoviednk.model.ScheduleModel;

import java.util.List;

public class ScheduleShowingAdapter extends RecyclerView.Adapter<ScheduleShowingAdapter.ScheduleShowingViewHolder> {
    private List<ScheduleModel> dateShowingList;
    private Context mContext;
    private ShiftShowingAdapter shiftShowingAdapter;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<ScheduleModel> dateShowingList) {
        this.dateShowingList = dateShowingList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScheduleShowingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemScheduleShowingMovieBinding binding = ItemScheduleShowingMovieBinding.inflate(inflater, parent, false);
        return new ScheduleShowingViewHolder(binding);
    }

    public ScheduleShowingAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleShowingViewHolder holder, int position) {
        ScheduleModel dateShowingModel = dateShowingList.get(position);
        if (dateShowingModel == null) {
            return;
        }
        String formatDate = DateUtils.formatDateString(dateShowingModel.getNgayChieu());
        holder.itemScheduleMovieBinding.tvDateScreening.setText(formatDate);
        
        shiftShowingAdapter = new ShiftShowingAdapter(mContext, dateShowingModel.getMaPhim());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        holder.itemScheduleMovieBinding.shiftsContainer.setLayoutManager(gridLayoutManager);

        shiftShowingAdapter.setData(dateShowingModel.getCaChieu());
        holder.itemScheduleMovieBinding.shiftsContainer.setAdapter(shiftShowingAdapter);
    }

    @Override
    public int getItemCount() {
        if (dateShowingList != null)
            return dateShowingList.size();
        return 0;
    }

    public static class ScheduleShowingViewHolder extends RecyclerView.ViewHolder {
        ItemScheduleShowingMovieBinding itemScheduleMovieBinding;

        public ScheduleShowingViewHolder(@NonNull ItemScheduleShowingMovieBinding binding) {
            super(binding.getRoot());
            this.itemScheduleMovieBinding = binding; // Binding is set here
        }
    }

}
