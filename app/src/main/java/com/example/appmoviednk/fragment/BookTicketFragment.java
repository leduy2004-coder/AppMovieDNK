package com.example.appmoviednk.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.appmoviednk.R;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.databinding.FragmentBookTicketBinding;


public class BookTicketFragment extends Fragment {

    FragmentBookTicketBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookTicketBinding.inflate(inflater, container, false);

        binding.btnBookTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo SpannableString để thay đổi màu tiêu đề
                SpannableString title = new SpannableString("Thông báo");
                title.setSpan(new ForegroundColorSpan(Color.RED), 0, title.length(), 0);
                new AlertDialog.Builder(requireContext())
                        .setIcon(R.drawable.ic_baseline_notifications_active_24)
                        .setTitle(title)
                        .setMessage("Bạn muốn đặt vé không?")
                        .setCancelable(false)
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(requireContext(), "Huỷ thành công", Toast.LENGTH_SHORT).show();
                            }
                        })

                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SuccessFragment successFragment = new SuccessFragment();

                                MainActivity mainActivity = (MainActivity) getActivity();
                                if (mainActivity != null) {
                                    mainActivity.replaceFragment(successFragment);
                                }

                            }
                        })
                        .show();
            }
        });

        return binding.getRoot();
    }
}