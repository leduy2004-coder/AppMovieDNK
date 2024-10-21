package com.example.appmoviednk.model;

import java.util.Date;
import java.util.List;

public class DateShowingModel {
    private String ngayChieu; // Ngày chiếu
    private List<ShiftModel> caChieu; // Danh sách ca chiếu

    public DateShowingModel(String ngayChieu, List<ShiftModel> caChieu) {
        this.ngayChieu = ngayChieu; // Khởi tạo ngày chiếu
        this.caChieu = caChieu; // Khởi tạo danh sách ca chiếu
    }

    public String getNgayChieu() {
        return ngayChieu; // Phương thức lấy ngày chiếu
    }

    public void setNgayChieu(String ngayChieu) {
        this.ngayChieu = ngayChieu;
    }

    public void setCaChieu(List<ShiftModel> caChieu) {
        this.caChieu = caChieu;
    }

    public List<ShiftModel> getCaChieu() {
        return caChieu; // Phương thức lấy danh sách ca chiếu
    }
}

