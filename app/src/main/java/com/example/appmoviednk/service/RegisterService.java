package com.example.appmoviednk.service;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RegisterService {

    @POST("api/send-verification")
    Call<ResponseBody> sendVerification(@Body HashMap<String, String> email);

    @POST("api/register")
    Call<ResponseBody> registerUser(@Body HashMap<String, String> registrationData);

    @GET("api/get-verification-code/{email}")
    Call<ResponseBody> getVerificationCode(@Path("email") String email);
}
