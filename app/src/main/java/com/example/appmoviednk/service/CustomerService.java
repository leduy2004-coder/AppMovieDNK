package com.example.appmoviednk.service;

import com.example.appmoviednk.model.CustomerModel;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CustomerService {

    @POST("customer/updatePoint/{maKH}")
    Call<JsonObject> updatePoint(@Path("maKH") String maKH, @Body Map<String, Integer> diemThuong);


    @GET("customer/getVoucherAndPoint/{maKH}")
    Call<CustomerModel> getSoLuongVoucherVaDiem(@Path("maKH") String maKH);
}
