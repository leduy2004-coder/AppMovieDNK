package com.example.appmoviednk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmoviednk.R;
import com.example.appmoviednk.databinding.ItemVoucherBinding;

import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {

    private final List<String> items;
    private Context context;
    private int attendance;   // Số lượng voucher đã điểm danh

    public VoucherAdapter(Context context, List<String> items, int attendance) {
        this.context = context;
        this.items = items;
        this.attendance = attendance;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateAttendance(int newAttendance) {
        this.attendance = newAttendance;
        notifyDataSetChanged(); // Cập nhật giao diện khi dữ liệu thay đổi
    }
    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemVoucherBinding binding = ItemVoucherBinding.inflate(inflater, parent, false);
        return new VoucherViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        String item = items.get(position);
        holder.itemVoucherBinding.itemText.setText(item);

        // Kiểm tra nếu ngày đã điểm danh
        if (position < attendance) {
            holder.itemVoucherBinding.itemText.setTextColor(Color.parseColor("#388E3C"));
            holder.itemVoucherBinding.frameLayout.setBackgroundResource(R.drawable.bg_rounded_background_2);
            holder.itemVoucherBinding.itemImage.setImageResource(R.drawable.img_check); // Hình đã điểm danh
        } else {
            holder.itemVoucherBinding.itemImage.setImageResource(R.drawable.img_voucher); // Hình chưa điểm danh
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class VoucherViewHolder extends RecyclerView.ViewHolder {
        ItemVoucherBinding itemVoucherBinding;

        public VoucherViewHolder(@NonNull ItemVoucherBinding binding) {
            super(binding.getRoot());
            this.itemVoucherBinding = binding;
        }
    }
}
