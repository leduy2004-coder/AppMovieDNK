package com.example.appmoviednk.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

public class MovieModel extends ViewModel implements DisplayTextSpinner, Serializable {
    private String maPhim;
    private String tenPhim;
    private String daoDien;
    private int doTuoi;
    private String ngayKhoiChieu;
    private int thoiLuong;
    private boolean tinhTrang;
    private String hinhDaiDien;
    private String video;
    private String moTa;
    private int imageResource;
    private String encode;
    private String dateString;

    public MovieModel() {

    }
    private final MutableLiveData<MovieModel> selectedMovie = new MutableLiveData<>();

    public void setSelectedMovie(MovieModel movie) {
        selectedMovie.setValue(movie);
    }

    public LiveData<MovieModel> getSelectedMovie() {
        return selectedMovie;
    }

    public String getMaPhim() {
        return maPhim;
    }

    public void setMaPhim(String maPhim) {
        this.maPhim = maPhim;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public String getDaoDien() {
        return daoDien;
    }

    public void setDaoDien(String daoDien) {
        this.daoDien = daoDien;
    }

    public int getDoTuoi() {
        return doTuoi;
    }

    public void setDoTuoi(int doTuoi) {
        this.doTuoi = doTuoi;
    }

    public String getNgayKhoiChieu() {
        return ngayKhoiChieu;
    }

    public void setNgayKhoiChieu(String ngayKhoiChieu) {
        this.ngayKhoiChieu = ngayKhoiChieu;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public boolean isTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(boolean tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public String getHinhDaiDien() {
        return hinhDaiDien;
    }

    public void setHinhDaiDien(String hinhDaiDien) {
        this.hinhDaiDien = hinhDaiDien;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public MovieModel(String maPhim, String tenPhim, int imageResource) {
        this.maPhim = maPhim;
        this.tenPhim = tenPhim;
        this.imageResource = imageResource;
    }

    public MovieModel(String maPhim, String tenPhim, String daoDien, String ngayKhoiChieu, int thoiLuong, String moTa, int imageResource) {
        this.maPhim = maPhim;
        this.tenPhim = tenPhim;
        this.daoDien = daoDien;
        this.ngayKhoiChieu = ngayKhoiChieu;
        this.thoiLuong = thoiLuong;
        this.moTa = moTa;
        this.imageResource = imageResource;
    }

    @Override
    public String toString() {
        return tenPhim;
    }


    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    @Override
    public String getDisplayText() {
        return this.tenPhim;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
