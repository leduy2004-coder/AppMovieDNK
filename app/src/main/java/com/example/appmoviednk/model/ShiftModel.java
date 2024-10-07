package com.example.appmoviednk.model;

import java.sql.Time;

public class ShiftModel implements DisplayTextSpinner{
    private String maCa;
    private String tenCa;
    private Time tgBD;
    private int status;

    public ShiftModel() {
    }

    public ShiftModel(String maCa, String tenCa) {
        this.maCa = maCa;
        this.tenCa = tenCa;
    }

    public ShiftModel(String maCa, Time tgBD) {
        this.maCa = maCa;
        this.tgBD = tgBD;
    }

    public Time getTgBD() {
        return tgBD;
    }

    public void setTgBD(Time tgBD) {
        this.tgBD = tgBD;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public String getDisplayText() {
        return this.tgBD.toString();
    }
}
