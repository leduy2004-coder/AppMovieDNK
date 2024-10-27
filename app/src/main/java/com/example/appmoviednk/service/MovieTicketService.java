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
    @GET("movie-tickets/{maPhim}")
    Call<List<Map<String, Object>>> getTicketsByMovie(@Path("maPhim") String maPhim);

    @POST("movie-tickets/bookve")
    Call<JsonObject> insertBookVe(@Body BookTicketModel bookTicketModel);

//    @POST("api/movie/bookghe")
//    Call<JsonObject> insertBookGhe( @Body String maBook, List<String> maGheList);


        @POST("movie-tickets/bookghe")
        Call<JsonObject> insertBookGhe(@Body Map<String, Object> bookGheData);

}
