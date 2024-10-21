package com.example.appmoviednk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmoviednk.R;
import com.example.appmoviednk.databinding.FragmentMovieBinding;
import com.example.appmoviednk.databinding.ItemChairBinding;
import com.example.appmoviednk.model.BookChairModel;

import java.util.ArrayList;
import java.util.List;

public class BookChairAdapter extends RecyclerView.Adapter<BookChairAdapter.SeatViewHolder> {
    private List<BookChairModel> seats;
    private Context context;
    private OnSeatClickListener onSeatClickListener; // Giao diện để xử lý sự kiện click ghế

    // Khai báo giao diện để lắng nghe sự kiện click ghế
    public interface OnSeatClickListener {
        void onSeatClick(BookChairModel seat);
    }
    public BookChairAdapter(Context context, OnSeatClickListener onChairClickListener) {
        this.seats = new ArrayList<>();
        this.onSeatClickListener = onChairClickListener;
        this.context = context;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<BookChairModel> seats) {
        this.seats.clear();
        this.seats.addAll(seats);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemChairBinding binding = ItemChairBinding.inflate(inflater, parent, false);
        return new BookChairAdapter.SeatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        if (seats == null || seats.size() <= position) return;
        BookChairModel seat = seats.get(position);

        if(seat.isTinhTrang()){
            holder.itemChairBinding.imageViewSeat.setImageResource(R.drawable.img_seatselled);
            holder.itemChairBinding.getRoot().setEnabled(false);
        }

        holder.itemChairBinding.getRoot().setOnClickListener(v -> {
            // Chuyển đổi trạng thái chọn (toggle)
            seat.setSelected(!seat.isSelected());

            // Cập nhật ảnh ghế dựa trên trạng thái chọn
            if (seat.isSelected()) {
                holder.itemChairBinding.imageViewSeat.setImageResource(R.drawable.img_seatselected);
            } else {
                holder.itemChairBinding.imageViewSeat.setImageResource(R.drawable.img_seat);
            }

            // Gọi sự kiện click ghế
            if (onSeatClickListener != null) {
                onSeatClickListener.onSeatClick(seat); // Gửi ghế đã click về cho Fragment
            }


        });



    }

    @Override
    public int getItemCount() {
        return seats != null ? seats.size() : 0;
    }

    public class SeatViewHolder extends RecyclerView.ViewHolder {
        ItemChairBinding itemChairBinding;

        public SeatViewHolder(@NonNull ItemChairBinding binding) {
            super(binding.getRoot());
            this.itemChairBinding = binding; // Binding is set here
        }
    }

}
