package com.example.appmoviednk.model;

public class BookChairModel {
    private String maGhe;
    private String maBook;
    private boolean tinhTrang;
    private boolean isSelected;

    public BookChairModel(String maGhe, boolean tinhTrang) {
        this.maGhe = maGhe;
        this.tinhTrang = tinhTrang;
        this.isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(boolean tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public String getMaGhe() {
        return maGhe;
    }

    public void setMaGhe(String maGhe) {
        this.maGhe = maGhe;
    }

    public String getMaBook() {
        return maBook;
    }

    public void setMaBook(String maBook) {
        this.maBook = maBook;
    }
}
