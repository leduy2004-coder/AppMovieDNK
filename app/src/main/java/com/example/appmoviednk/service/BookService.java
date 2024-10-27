package com.example.appmoviednk.service;

import com.example.appmoviednk.model.BookChairModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BookService {
    @GET("book/{maSuat}")
    Call<List<BookChairModel>> getGheDaDat(@Path("maSuat") String maSuat);
}
