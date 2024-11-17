package com.example.appmoviednk.service;




import com.example.appmoviednk.model.MovieModel;
import com.example.appmoviednk.model.ScheduleModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface MovieService {

    @GET("movies/phimdangchieu")
    Call<List<MovieModel>> getMoviesDangChieu();

    @GET("movies/phimchuachieu")
    Call<List<MovieModel>> getMoviesChuaChieu();

    @GET("movies/ngaydangchieu")
    Call<List<ScheduleModel>> getNgayDangChieu();

    @GET("movies/get-schedule/{id}")
    Call<List<ScheduleModel>> getSchedules(@Path("id") String maPhim);

    @GET("movies/get-date-schedule/{id}")
    Call<List<ScheduleModel>> getDateSchedules(@Path("id") String maPhim);

    @GET("movies/phim/{ngay}")
    Call<List<MovieModel>> getMoviesByDate(@Path("ngay") String ngay);

    @GET("movies/ticket-details/{maBook}")
    Call<List<Map<String, Object>>> getTicketDetails(@Path("maBook") String maBook);
}

