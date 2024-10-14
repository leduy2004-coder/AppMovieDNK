package com.example.appmoviednk.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.appmoviednk.R;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.databinding.FragmentAccountBinding;
import com.example.appmoviednk.databinding.FragmentVoucherBinding;

public class VoucherFragment extends Fragment {
    FragmentVoucherBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentVoucherBinding.inflate(inflater, container, false);

        binding.voucherBtn.btnPrimary.setText("Điểm danh");

        binding.voucherBtn.btnPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpannableString title = new SpannableString("Chúc mừng");
                title.setSpan(new ForegroundColorSpan(Color.RED), 0, title.length(), 0);

                // Tạo AlertDialog
                AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                        .setIcon(R.drawable.ic_baseline_notifications_active_24)
                        .setTitle(title)
                        .setMessage("Bạn đã điểm danh ngày thứ 1!!!")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show(); // Hiển thị AlertDialog
            }
        });

        return binding.getRoot();
    }
}