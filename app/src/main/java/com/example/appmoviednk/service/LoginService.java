package com.example.appmoviednk.service;



import com.example.appmoviednk.model.CustomerModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LoginService {

    @GET("account")
    Call<List<CustomerModel>> getAllAccounts(); // Lấy danh sách tài khoản

    @POST("account/login")
    Call<CustomerModel> loginAccount(@Body CustomerModel customerModel);




    @GET("account/history/{maKH}")
    Call<List<Map<String, Object>>> getCustomerHistory(@Path("maKH") String maKH);
}
