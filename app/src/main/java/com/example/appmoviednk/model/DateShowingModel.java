package com.example.appmoviednk.model;

import java.sql.Date;
import java.util.List;

public class DateShowingModel {
    private Date date;
    private List<ShiftModel> listShift;

    public DateShowingModel(Date date, List<ShiftModel> listShift) {
        this.date = date;
        this.listShift = listShift;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<ShiftModel> getListShift() {
        return listShift;
    }

    public void setListShift(List<ShiftModel> listShift) {
        this.listShift = listShift;
    }
}
