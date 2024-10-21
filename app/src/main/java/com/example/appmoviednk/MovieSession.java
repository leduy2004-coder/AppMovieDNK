package com.example.appmoviednk;

import com.example.appmoviednk.model.MovieModel;

public class MovieSession {
    private static MovieSession instance;
    private MovieModel selectedMovie;

    private MovieSession() {}

    public static MovieSession getInstance() {
        if (instance == null) {
            instance = new MovieSession();
        }
        return instance;
    }

    public void setSelectedMovie(MovieModel movie) {
        this.selectedMovie = movie;
    }

    public MovieModel getSelectedMovie() {
        return selectedMovie;
    }

    public void clearSession() {
        selectedMovie = null;
    }
}
