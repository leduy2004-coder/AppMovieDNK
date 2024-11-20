package com.example.appmoviednk.model;

import java.sql.Timestamp;

public class CommentModel {
    private int maBinhLuan;
    private String maPhim;
    private CustomerModel khachHang;
    private String noiDung;
    private Timestamp gio;
    private String formattedDateTime;
    public String getMaPhim() {
        return maPhim;
    }
    public void setMaPhim(String maPhim) {
        this.maPhim = maPhim;
    }

    public String getNoiDung() {
        return noiDung;
    }
    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }
    public Timestamp getGio() {
        return gio;
    }
    public void setGio(Timestamp timestamp) {
        this.gio = timestamp;
    }
    public int getMaBinhLuan() {
        return maBinhLuan;
    }
    public void setMaBinhLuan(int maBinhLuan) {
        this.maBinhLuan = maBinhLuan;
    }

    public String getFormattedDateTime() {
        return formattedDateTime;
    }
    public void setFormattedDateTime(String formattedDateTime) {
        this.formattedDateTime = formattedDateTime;
    }


    public CustomerModel getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(CustomerModel khachHang) {
        this.khachHang = khachHang;
    }
}
