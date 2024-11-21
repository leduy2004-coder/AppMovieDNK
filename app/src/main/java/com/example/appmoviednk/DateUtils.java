package com.example.appmoviednk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String formatDateString(String dateString) {
        String formattedDate = "";
        try {
            Date date;
            // Kiểm tra độ dài của chuỗi để xác định định dạng
            if (dateString.length() == 10) { // Định dạng yyyy-MM-dd
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                date = inputFormat.parse(dateString);
            } else { // Định dạng yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                date = inputFormat.parse(dateString);
            }

            // Định dạng đầu ra (dd/MM/yyyy)
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            assert date != null;
            formattedDate = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }
}
