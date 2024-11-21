package com.example.appmoviednk.model;

import java.time.LocalDate;
import java.util.Date;

public class CustomerModel {
    private String maKH;
    private String hoTen;
    private String sdt;
    private Date ngaySinh;
    private String email;
    private String maDangKi;
	private Date time;
    private boolean tinhTrang;
    private String tenTK;
    private String matKhau;
    private int diemDanh;
    private int soLuongVoucher;
    private Date ngayDiemDanhCuoi;
    private LocalDate ngay;

    public CustomerModel() {
    }

    public CustomerModel(String tenTK, String matKhau) {
        this.tenTK = tenTK;
        this.matKhau = matKhau;
    }

    public CustomerModel(String email, String hoTen, String tenTK, String matKhau, String sdt) {
        this.email = email;
        this.hoTen = hoTen;
        this.tenTK = tenTK;
        this.matKhau = matKhau;
        this.sdt = sdt;
    }

    public String getMaDangKi() {
        return maDangKi;
    }

    public void setMaDangKi(String maDangKi) {
        this.maDangKi = maDangKi;
    }

    public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}


	public boolean isTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(boolean tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public String getMaKH() {
        return maKH;
    }

    public String getTenTK() {
        return tenTK;
    }

    public void setTenTK(String tenTK) {
        this.tenTK = tenTK;
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
        return tenTK;
    }

    public void setTentk(String tentk) {
        this.tenTK = tentk;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public int getDiemDanh() {
        return diemDanh;
    }

    public void setDiemDanh(int diemDanh) {
        this.diemDanh = diemDanh;
    }

    public int getSoLuongVoucher() {
        return soLuongVoucher;
    }

    public void setSoLuongVoucher(int soLuongVoucher) {
        this.soLuongVoucher = soLuongVoucher;
    }

    public Date getNgayDiemDanhCuoi() {
        return ngayDiemDanhCuoi;
    }

    public void setNgayDiemDanhCuoi(Date ngayDiemDanhCuoi) {
        this.ngayDiemDanhCuoi = ngayDiemDanhCuoi;
    }

    public LocalDate getNgay() {
        return ngay;
    }

    public void setNgay(LocalDate ngay) {
        this.ngay = ngay;
    }
}
