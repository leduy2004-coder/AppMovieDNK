package com.example.appmoviednk;

import androidx.core.net.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String formatDateString(String dateString) {
        String formattedDate = "";
        try {
            // Định dạng đầu vào
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            // Định dạng đầu ra
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            // Chuyển đổi chuỗi thành đối tượng Date
            Date date = inputFormat.parse(dateString);
            // Chuyển đổi đối tượng Date thành chuỗi với định dạng mới
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
        return formattedDate;
    }
}
