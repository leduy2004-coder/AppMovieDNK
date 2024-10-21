package com.example.appmoviednk.service;

import com.example.appmoviednk.model.BookTicketModel;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MovieTicketService {
    @GET("api/movie/ticket/{maPhim}")
    Call<List<Map<String, Object>>> getTicketsByMovie(@Path("maPhim") String maPhim);

    @POST("api/movie/bookve")
    Call<JsonObject> insertBookVe(@Body BookTicketModel bookTicketModel);

//    @POST("api/movie/bookghe")
//    Call<JsonObject> insertBookGhe( @Body String maBook, List<String> maGheList);


        @POST("api/movie/bookghe")
        Call<JsonObject> insertBookGhe(@Body Map<String, Object> bookGheData);

}
