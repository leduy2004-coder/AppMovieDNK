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

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<CommentModel> cmtList) {
        this.cmtList = cmtList;
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
        CommentModel cmt = cmtList.get(position);
        if(cmt == null){
            return;
        }
        holder.commentBinding.cmtUser.setText(cmt.getKhachHang().getHoTen());
        holder.commentBinding.cmtContent.setText(cmt.getNoiDung());
    }

    @Override
    public int getItemCount() {
        // Nếu showAll là true, trả về toàn bộ số lượng item
        if (showAll) {
            return cmtList.size();
        } else
        // Nếu showAll là false, chỉ trả về tối đa 4 item
        {
            return cmtList != null ? 4 : 0;
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
