package com.example.appmoviednk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmoviednk.DateUtils;
import com.example.appmoviednk.R;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.databinding.ItemHistoryBookBinding;
import com.example.appmoviednk.fragment.SuccessFragment;
import com.example.appmoviednk.fragment.TicketInfFragment;
import com.example.appmoviednk.model.BookTicketModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryBookAdapter  extends  RecyclerView.Adapter<HistoryBookAdapter.HistoryBookViewHolder>{
    private List<Map<String, Object>> listBooked = new ArrayList<>();
    private Context mContext;
    private boolean showAll = false;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Map<String, Object>> listBooked) {
        this.listBooked = listBooked != null ? listBooked : new ArrayList<>();
        notifyDataSetChanged();
    }
    // Hàm cập nhật trạng thái hiển thị
    @SuppressLint("NotifyDataSetChanged")
    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemHistoryBookBinding binding = ItemHistoryBookBinding.inflate(inflater, parent, false);
        return new HistoryBookViewHolder(binding);
    }


    public HistoryBookAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HistoryBookViewHolder holder, int position) {
        if (position < 0 || position >= listBooked.size()) {
            return; // Tránh lỗi IndexOutOfBoundsException
        }

        Map<String, Object> ticket = listBooked.get(position);
        if (ticket == null) {
            return;
        }
        String ngayMua = (String) ticket.get("ngayMua");

        // Định dạng lại chuỗi ngày
        String formattedDate = DateUtils.formatDateString(ngayMua);

        holder.itemHistoryBookBinding.textTenPhim.setText(""+ ticket.get("tenPhim"));
        holder.itemHistoryBookBinding.textTongTien.setText(""+ ticket.get("tongTien"));
        holder.itemHistoryBookBinding.textNgayMua.setText(""+ formattedDate);

        // Đổi màu nền xen kẽ
        if (position % 2 == 0) {
            holder.itemHistoryBookBinding.itemBook.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
        } else {
            holder.itemHistoryBookBinding.itemBook.setBackgroundResource(R.drawable.bg_history);
        }

        holder.itemView.setOnClickListener(v -> {

            if ((mContext instanceof MainActivity)) {
                MainActivity mainActivity = (MainActivity) mContext;
                TicketInfFragment ticketInfFragment = new TicketInfFragment();

                Bundle bundle = new Bundle();


                bundle.putString("maBook", ticket.get("maBook").toString() ); // Truyền MovieModel dưới dạng Serializable
                // Gán Bundle vào Fragment
                ticketInfFragment.setArguments(bundle);
                // Chuyển Fragment
                mainActivity.replaceFragment(ticketInfFragment ,true);
            }


        });
    }

    @Override
    public int getItemCount() {
        // Nếu showAll là true, trả về toàn bộ số lượng item
        if (showAll) {
            return listBooked.size();
        } else
        // Nếu showAll là false, chỉ trả về tối đa 3 item
        {
            return listBooked != null ? 3 : 0;
        }
    }


    public class HistoryBookViewHolder extends RecyclerView.ViewHolder {
        ItemHistoryBookBinding itemHistoryBookBinding;

        public HistoryBookViewHolder(@NonNull ItemHistoryBookBinding binding) {
            super(binding.getRoot());
            this.itemHistoryBookBinding = binding;
        }
    }

}
