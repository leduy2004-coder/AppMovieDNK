package com.example.appmoviednk.service;



import com.example.appmoviednk.model.CustomerModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LoginService {

    @GET("api/accounts")
    Call<List<CustomerModel>> getAllAccounts(); // Lấy danh sách tài khoản

    @GET("api/accounts/{email}/{password}")
    Call<CustomerModel> login(
            @Path("email") String email,
            @Path("password") String password
    );
    @GET("api/history/{maKH}")
    Call<List<Map<String, Object>>> getCustomerHistory(@Path("maKH") String maKH);
}
