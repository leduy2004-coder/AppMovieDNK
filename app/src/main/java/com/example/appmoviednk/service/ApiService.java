package com.example.appmoviednk.service;




import com.example.appmoviednk.model.DateShowingModel;
import com.example.appmoviednk.model.MovieModel;
import com.example.appmoviednk.model.ScheduleModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {

    @GET("api/movies")
    Call<List<MovieModel>> getAllStudents();

    @GET("api/movies/phimdangchieu")
    Call<List<MovieModel>> getMoviesDangChieu();

    @GET("api/movies/phimchuachieu")
    Call<List<MovieModel>> getMoviesChuaChieu();

    @GET("api/get-schedule/{id}")
    Call<List<ScheduleModel>> getSchedules(@Path("id") String maPhim);

//    @GET("api/get-schedule/{id}")
//    Call<List<Map<String, Object>>> getSchedules(@Path("id") String maPhim);
}

