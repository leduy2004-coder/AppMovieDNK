package com.example.appmoviednk.service;

import com.example.appmoviednk.model.CustomerModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface VoucherService {
    @POST("voucher/update")
    Call<JsonObject> updateVoucher(@Body CustomerModel customerModel);
}