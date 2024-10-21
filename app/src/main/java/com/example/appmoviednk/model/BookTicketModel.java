package com.example.appmoviednk.model;

import java.util.Date;

public class BookTicketModel {
    private String maBook;
    private String maKH;
    private String maNV;
    private String maSuat;
    private String maVe;
    private int tongTien;
    private Date ngayMua;

    public BookTicketModel() {
    }

    public BookTicketModel(String maVe, String maKH, int tongTien) {
        this.maVe = maVe;
        this.maKH = maKH;
        this.tongTien = tongTien;
    }

    public BookTicketModel(String maBook, String maKH, String maSuat, String maVe, int tongTien, Date ngayMua) {
        this.maBook = maBook;
        this.maKH = maKH;
        this.maSuat = maSuat;
        this.maVe = maVe;
        this.tongTien = tongTien;
        this.ngayMua = ngayMua;
    }

    public String getMaBook() {
        return maBook;
    }

    public void setMaBook(String maBook) {
        this.maBook = maBook;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaSuat() {
        return maSuat;
    }

    public void setMaSuat(String maSuat) {
        this.maSuat = maSuat;
    }

    public String getMaVe() {
        return maVe;
    }

    public void setMaVe(String maVe) {
        this.maVe = maVe;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    public Date getNgayMua() {
        return ngayMua;
    }

    public void setNgayMua(Date ngayMua) {
        this.ngayMua = ngayMua;
    }
}
