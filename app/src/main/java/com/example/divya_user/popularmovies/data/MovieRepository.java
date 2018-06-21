package com.example.divya_user.popularmovies.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.divya_user.popularmovies.data.Room.DAO.MovieDAO;
import com.example.divya_user.popularmovies.data.Room.DB.AppDatabase;
import com.example.divya_user.popularmovies.data.Room.Entity.Movie;

import java.util.List;

public class MovieRepository {

    private static final Object LOCK = new Object();
    private final MovieDAO mMovieDAO;
    private static MovieRepository sInstance;

    private MovieRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application.getApplicationContext());
        mMovieDAO = appDatabase.movieDAO();
    }

    public synchronized static MovieRepository getInstance(Application application) {
        if(sInstance == null) {
            synchronized(LOCK) {
                sInstance = new MovieRepository(application);
            }
        }
        return sInstance;
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return mMovieDAO.getFavoriteMovies();
    }

    public LiveData<Integer> isFavoriteMovie(long movieId) {
        return mMovieDAO.isFavoriteMovie(movieId);
    }
}
