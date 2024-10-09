package com.example.appmoviednk.model;

import java.util.List;

public class DetailMovieModel {
    private MovieModel movie;
    private List<DateShowingModel> listDate;
    private String typeMovie;

    public DetailMovieModel(MovieModel movie, List<DateShowingModel> listDate, String typeMovie) {
        this.movie = movie;
        this.listDate = listDate;
        this.typeMovie = typeMovie;
    }



    public MovieModel getMovie() {
        return movie;
    }

    public void setMovie(MovieModel movie) {
        this.movie = movie;
    }

    public String getTypeMovie() {
        return typeMovie;
    }

    public void setTypeMovie(String typeMovie) {
        this.typeMovie = typeMovie;
    }

    public List<DateShowingModel> getListDate() {
        return listDate;
    }

    public void setListDate(List<DateShowingModel> listDate) {
        this.listDate = listDate;
    }
}
