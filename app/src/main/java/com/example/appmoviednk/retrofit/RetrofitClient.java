package com.example.appmoviednk.retrofit;


import com.example.appmoviednk.service.ApiService;
import com.example.appmoviednk.service.BookService;
import com.example.appmoviednk.service.LoginService;
import com.example.appmoviednk.service.MovieTicketService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.0.2.2:3000/";
//    private static final String BASE_URL = "http://172.20.10.3:3000/";
    private static Retrofit retrofit;

        public static Retrofit getRetrofitInstance() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }

        public static ApiService getApiService() {
            return getRetrofitInstance().create(ApiService.class);
        }
        public static LoginService getLoginService(){
            return getRetrofitInstance().create(LoginService.class);
        }

        public static BookService getBookService(){
            return getRetrofitInstance().create(BookService.class);
        }

    public static MovieTicketService getMovieTicketService(){
        return getRetrofitInstance().create(MovieTicketService.class);
    }

}