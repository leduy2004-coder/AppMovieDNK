package com.example.appmoviednk.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.sql.Time;

public class ShiftModel extends ViewModel implements DisplayTextSpinner {
    private String maCa; // Mã ca
    private String tenCa; // Tên ca
    private String thoiGianBatDau; // Thời gian bắt đầu ca
    private int status; // Trạng thái ca
    private String maSuat;

    private final MutableLiveData<ShiftModel> selectedShift = new MutableLiveData<>();

    public void setSelectedShift(ShiftModel movie) {
        selectedShift.setValue(movie);
    }

    public LiveData<ShiftModel> getSelectedShift() {
        return selectedShift;
    }

    public ShiftModel() {
    }

    public ShiftModel(String maCa, String tenCa, String thoiGianBatDau, int status, String maSuat) {
        this.maCa = maCa;
        this.tenCa = tenCa;
        this.thoiGianBatDau = thoiGianBatDau;
        this.status = status;
        this.maSuat = maSuat;
    }



    public String getMaCa() {
        return maCa;
    }

    public void setMaCa(String maCa) {
        this.maCa = maCa;
    }

    public String getTenCa() {
        return tenCa;
    }

    public void setTenCa(String tenCa) {
        this.tenCa = tenCa;
    }

    public String getThoiGianBatDau() {
        return thoiGianBatDau;
    }

    public void setThoiGianBatDau(String thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMaSuat() {
        return maSuat;
    }

    public void setMaSuat(String maSuat) {
        this.maSuat = maSuat;
    }

    @Override
    public String getDisplayText() {
        return thoiGianBatDau;
    }
}
