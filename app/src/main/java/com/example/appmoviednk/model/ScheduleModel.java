package com.example.appmoviednk.model;

import java.sql.Date;
import java.util.List;

public class ScheduleModel implements DisplayTextSpinner{
        private String maSuat;
        private String maPhim;
        private String maPhong;
        private String maCa;
        private String ngayChieu;
        private boolean tinhTrang;
        private String dateString;
    private List<ShiftModel> caChieu;

    public ScheduleModel(String maSC, String ngayChieu, String maCa,  List<ShiftModel> caChieu) {
        this.maSuat = maSC;
        this.ngayChieu = ngayChieu;
        this.maCa = maCa;
        this.caChieu = caChieu; // Liên kết với ShiftModel
    }

    public ScheduleModel(String ngayChieu, List<ShiftModel> caChieu) {
        this.ngayChieu = ngayChieu;
        this.caChieu = caChieu;
    }

    public ScheduleModel(String maSC, String ngayChieu) {
        this.maSuat = maSC;
        this.ngayChieu = ngayChieu;
    }

    public String getMaSuat() {
        return maSuat;
    }

    public List<ShiftModel> getCaChieu() {
        return caChieu;
    }

    public void setCaChieu(List<ShiftModel> caChieu) {
        this.caChieu = caChieu;
    }

    public void setMaSuat(String maSuat) {
        this.maSuat = maSuat;
    }



    // Getters và Setters


    public boolean isTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(boolean tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public String getMaSC() {
        return maSuat;
    }

    public void setMaSC(String maSC) {
        this.maSuat = maSC;
    }

    public String getMaPhim() {
        return maPhim;
    }

    public void setMaPhim(String maPhim) {
        this.maPhim = maPhim;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getMaCa() {
        return maCa;
    }

    public void setMaCa(String maCa) {
        this.maCa = maCa;
    }

    public String getNgayChieu() {
        return ngayChieu;
    }

    public void setNgayChieu(String ngayChieu) {
        this.ngayChieu = ngayChieu;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    @Override
    public String getDisplayText() {
        return this.dateString;
    }
}