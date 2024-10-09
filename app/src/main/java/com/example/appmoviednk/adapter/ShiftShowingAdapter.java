package com.example.appmoviednk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.databinding.ItemShiftShowingMovieBinding;
import com.example.appmoviednk.fragment.BookTicketFragment;
import com.example.appmoviednk.fragment.MovieFragment;
import com.example.appmoviednk.model.CustomerModel;
import com.example.appmoviednk.model.ShiftModel;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;

public class ShiftShowingAdapter extends RecyclerView.Adapter<ShiftShowingAdapter.ShiftShowingViewHolder> {
    private List<ShiftModel> shiftList;
    private Context mContext;


    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<ShiftModel> shiftList) {
        this.shiftList = shiftList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShiftShowingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemShiftShowingMovieBinding binding = ItemShiftShowingMovieBinding.inflate(inflater, parent, false);
        return new ShiftShowingViewHolder(binding);
    }

    public ShiftShowingAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftShowingViewHolder holder, int position) {
        ShiftModel shift = shiftList.get(position);
        if (shift == null) {
            return;
        }

        holder.itemShiftShowingMovieBinding.btnShift.setText(convertTimeToString(shift.getTgBD()));

        // Click button
        holder.itemShiftShowingMovieBinding.btnShift.setOnClickListener(v -> {
            if (mContext instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) mContext;
                BookTicketFragment bookTicketFragment = new BookTicketFragment();
                mainActivity.replaceFragment(bookTicketFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (shiftList != null)
            return shiftList.size();
        return 0;
    }

    public class ShiftShowingViewHolder extends RecyclerView.ViewHolder {
        ItemShiftShowingMovieBinding itemShiftShowingMovieBinding;

        public ShiftShowingViewHolder(@NonNull ItemShiftShowingMovieBinding binding) {
            super(binding.getRoot());
            this.itemShiftShowingMovieBinding = binding; // Binding is set here
        }
    }

    public String convertTimeToString(Time time) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(time);
    }
}
