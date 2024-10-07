package com.example.appmoviednk.model;

import java.util.Date;

public class CustomerModel {
    private String maKH;
    private String hoTen;
    private String sdt;
    private Date ngaySinh;
    private String email;
    private String authentication;
	private Date time;
	private int statusAuthen;

    public String getAuthentication() {
		return authentication;
	}

	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getStatusAuthen() {
		return statusAuthen;
	}

	public void setStatusAuthen(int statusAuthen) {
		this.statusAuthen = statusAuthen;
	}

	public boolean isTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(boolean tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    private boolean tinhTrang;
    private String tentk;
    private String matKhau;

    public CustomerModel() {
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTentk() {
        return tentk;
    }

    public void setTentk(String tentk) {
        this.tentk = tentk;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public CustomerModel(String hoTen, String email) {
        this.hoTen = hoTen;
        this.email = email;
    }
}
