package com.example.appmoviednk.service;
import com.example.appmoviednk.model.CustomerModel;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RegisterService {

    @POST("email/send-verification")
    Call<ResponseBody> sendVerification(@Body HashMap<String, String> email);

    @POST("email/register")
    Call<ResponseBody> registerUser(@Body CustomerModel registrationData);

    @POST("email/verify-code")
    Call<ResponseBody> verifyCode(@Body HashMap<String, String> codeMap);

}
