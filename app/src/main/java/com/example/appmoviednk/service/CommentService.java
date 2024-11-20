package com.example.appmoviednk.service;


import com.example.appmoviednk.model.CommentModel;
import com.example.appmoviednk.model.CommentResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface CommentService {

    @GET("comment/{maPhim}")
    Call<List<CommentModel>> getCommentsByMovieId(@Path("maPhim") String maPhim);

    @POST("comment/send")
    Call<CommentResponse> insertComment(@Body CommentModel commentModel);
}

