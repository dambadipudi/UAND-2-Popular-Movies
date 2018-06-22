package com.example.divya_user.popularmovies.UI.Detail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.divya_user.popularmovies.data.MovieRepository;
import com.example.divya_user.popularmovies.data.Room.Entity.Movie;

public class DetailActivityViewModel extends AndroidViewModel {

    private MovieRepository mMovieRepository;

    public DetailActivityViewModel(Application application) {
        super(application);
        mMovieRepository = MovieRepository.getInstance(application);
    }

    public LiveData<Integer> isFavoriteMovie(long movieId) {
        return mMovieRepository.isFavoriteMovie(movieId);
    }

    public void saveFavoriteMovie(Movie movie) {
        mMovieRepository.saveFavoriteMovie(movie);
    }

    public void removeFavoriteMovie(Movie movie) {
        mMovieRepository.removeFavoriteMovie(movie);
    }
}
