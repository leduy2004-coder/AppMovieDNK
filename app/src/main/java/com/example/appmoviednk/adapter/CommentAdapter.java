package com.example.appmoviednk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmoviednk.databinding.ItemCommentBinding;
import com.example.appmoviednk.model.CommentModel;
import com.example.appmoviednk.model.CustomerModel;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<CommentModel> cmtList;
    private Context mContext;
    private boolean showAll = false;

    // Hàm cập nhật dữ liệu
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<CommentModel> cmtList) {
        if (cmtList != null) {
            this.cmtList = cmtList;
            notifyDataSetChanged();
        }
    }

    // Hàm cập nhật trạng thái hiển thị
    @SuppressLint("NotifyDataSetChanged")
    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCommentBinding binding = ItemCommentBinding.inflate(inflater, parent, false);
        return new CommentViewHolder(binding);
    }

    public CommentAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        if (cmtList == null || cmtList.isEmpty()) {
            return;  // Nếu danh sách null hoặc rỗng, không làm gì cả
        }

        CommentModel cmt = cmtList.get(position);
        if (cmt == null) {
            return; // Nếu phần tử null, không làm gì cả
        }

        holder.commentBinding.cmtUser.setText(cmt.getKhachHang().getHoTen());
        holder.commentBinding.cmtContent.setText(cmt.getNoiDung());
    }

    @Override
    public int getItemCount() {
        if (cmtList == null) {
            return 0; // Nếu danh sách null, trả về 0
        }
        // Nếu showAll là true, trả về toàn bộ số lượng item
        if (showAll) {
            return cmtList.size();
        } else {
            // Nếu showAll là false, chỉ trả về tối đa 4 item
            return Math.min(cmtList.size(), 4);
        }
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ItemCommentBinding commentBinding;

        public CommentViewHolder(@NonNull ItemCommentBinding binding) {
            super(binding.getRoot());
            this.commentBinding = binding; // Binding is set here
        }
    }
}

