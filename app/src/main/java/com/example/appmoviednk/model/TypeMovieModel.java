package com.example.appmoviednk.model;

public class TypeMovieModel {
    private String maLPhim;
    private String tenLPhim;
    private String moTaLP;

    public String getMaLPhim() {
        return maLPhim;
    }

    public void setMaLPhim(String maLPhim) {
        this.maLPhim = maLPhim;
    }

    public String getTenLPhim() {
        return tenLPhim;
    }

    public void setTenLPhim(String tenLPhim) {
        this.tenLPhim = tenLPhim;
    }

    public String getMoTaLP() {
        return moTaLP;
    }

    public void setMoTaLP(String moTaLP) {
        this.moTaLP = moTaLP;
    }

    public TypeMovieModel() {
    }

    @Override
    public String toString() {
        return tenLPhim;
    }
}
