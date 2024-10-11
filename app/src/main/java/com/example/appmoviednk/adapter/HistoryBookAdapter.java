package com.example.appmoviednk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmoviednk.R;
import com.example.appmoviednk.databinding.ItemCommentBinding;
import com.example.appmoviednk.databinding.ItemHistoryBookBinding;
import com.example.appmoviednk.model.BookTicketModel;
import com.example.appmoviednk.model.CustomerModel;

import java.util.List;

public class HistoryBookAdapter  extends  RecyclerView.Adapter<HistoryBookAdapter.HistoryBookViewHolder>{
    private List<BookTicketModel> listBooked;
    private Context mContext;


    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<BookTicketModel> listBooked) {
        this.listBooked = listBooked;
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

    @Override
    public void onBindViewHolder(@NonNull HistoryBookViewHolder holder, int position) {
        BookTicketModel ticket = listBooked.get(position);
        if(ticket == null){
            return;
        }
        holder.itemHistoryBookBinding.itemBook.setText(ticket.getMaVe() + "  " + ticket.getMaVe() + "  " + ticket.getTongTien());

        // Đổi màu nền xen kẽ: màu trắng cho vị trí chẵn và màu đen cho vị trí lẻ
        if (position % 2 == 0) {
            holder.itemHistoryBookBinding.itemBook.setBackgroundColor(mContext.getResources().getColor(android.R.color.white)); // Màu trắng
        } else {
            holder.itemHistoryBookBinding.itemBook.setBackgroundResource(R.drawable.bg_history); // Màu đen
        }
    }

    @Override
    public int getItemCount() {
        if (listBooked != null)
            return listBooked.size();
        return 0;
    }


    public class HistoryBookViewHolder extends RecyclerView.ViewHolder {
        ItemHistoryBookBinding itemHistoryBookBinding;

        public HistoryBookViewHolder(@NonNull ItemHistoryBookBinding binding) {
            super(binding.getRoot());
            this.itemHistoryBookBinding = binding;
        }
    }
}
