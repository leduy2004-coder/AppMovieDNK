package com.example.appmoviednk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmoviednk.databinding.ItemCommentBinding;
import com.example.appmoviednk.model.CustomerModel;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<CustomerModel> cmtList;
    private Context mContext;



    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<CustomerModel> cmtList) {
        this.cmtList = cmtList;
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
        CustomerModel cmt = cmtList.get(position);
        if(cmt == null){
            return;
        }
        holder.commentBinding.cmtUser.setText(cmt.getHoTen());
        holder.commentBinding.cmtContent.setText(cmt.getEmail());
    }

    @Override
    public int getItemCount() {
        if (cmtList != null)
            return cmtList.size();
        return 0;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
    ItemCommentBinding commentBinding;

        public CommentViewHolder(@NonNull ItemCommentBinding binding) {
            super(binding.getRoot());
            this.commentBinding = binding; // Binding is set here
        }
    }
}
