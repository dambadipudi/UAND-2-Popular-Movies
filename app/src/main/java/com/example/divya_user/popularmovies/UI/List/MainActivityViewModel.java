package com.example.divya_user.popularmovies.UI.List;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.divya_user.popularmovies.data.MovieRepository;
import com.example.divya_user.popularmovies.data.Room.Entity.Movie;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private MovieRepository mMovieRepository;
    private LiveData<List<Movie>> mFavoriteMovies;

    public MainActivityViewModel(Application application) {
        super(application);
        mMovieRepository = MovieRepository.getInstance(application);
        mFavoriteMovies = mMovieRepository.getFavoriteMovies();
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return mFavoriteMovies;
    }

    public LiveData<Integer> isFavoriteMovie(long movieId) {
           return mMovieRepository.isFavoriteMovie(movieId);
    }
}
