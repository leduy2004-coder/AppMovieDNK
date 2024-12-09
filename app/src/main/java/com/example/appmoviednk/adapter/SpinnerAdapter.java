package com.example.appmoviednk.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appmoviednk.R;
import com.example.appmoviednk.model.DisplayTextSpinner;

import java.util.List;

public class SpinnerAdapter<T extends DisplayTextSpinner> extends ArrayAdapter<T> {

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
    }

    private static class ViewHolder {
        TextView textView;
    }
    public void addDefaultItem(String defaultDisplayText) {
        boolean hasDefault = false;
        for (int i = 0; i < getCount(); i++) {
            T item = getItem(i);
            if (item != null && defaultDisplayText.equals(item.getDisplayText())) {
                hasDefault = true;
                break;
            }
        }
        if (!hasDefault) {
            DisplayTextSpinner defaultItem = new DisplayTextSpinner() {
                @Override
                public String getDisplayText() {
                    return defaultDisplayText;
                }
            };
            insert((T) defaultItem, 0); // Thêm mục mặc định vào vị trí đầu tiên
        }
    }

    public boolean isDefaultSelected(int selectedPosition, String defaultDisplayText) {
        T item = getItem(selectedPosition);
        return item != null && defaultDisplayText.equals(item.getDisplayText());
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item_home, parent, false);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.tv_category);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        T item = getItem(position);
        Log.d("SpinnerAdapter", "Vị trí: " + position + ", Mục: " + (item != null ? item.getDisplayText() : "null"));
        if (item != null) {
            holder.textView.setText(item.getDisplayText());
        }
        return convertView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_selected_home, parent, false);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.tv_selected);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lấy item từ position, chỉ xử lý khi có item
        T item = getItem(position);
        if (item != null) {
            holder.textView.setText(item.getDisplayText());
        }

        return convertView;
    }

}
