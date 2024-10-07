package com.example.appmoviednk.model;

import java.sql.Date;

public class ScheduleModel implements DisplayTextSpinner{
    private String maSC;
    private String maPhim;
    private String maPhong;
    private String maCa;
    private Date ngayChieu;
    private boolean tinhTrang;
    private String dateString;

    public ScheduleModel(String maSC, Date ngayChieu) {
        this.maSC = maSC;
        this.ngayChieu = ngayChieu;
    }

    public boolean isTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(boolean tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public String getMaSC() {
        return maSC;
    }

    public void setMaSC(String maSC) {
        this.maSC = maSC;
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

    public Date getNgayChieu() {
        return ngayChieu;
    }

    public void setNgayChieu(Date ngayChieu) {
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
        return this.ngayChieu.toString();
    }
}