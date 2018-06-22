package com.example.divya_user.popularmovies.data.Room.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.divya_user.popularmovies.data.Room.Entity.Movie;

import java.util.List;

@Dao
public interface MovieDAO {

    @Query("SELECT * FROM favorite_movie")
    LiveData<List<Movie>> getFavoriteMovies();

    @Query("SELECT EXISTS(SELECT * FROM favorite_movie WHERE movieId = :movieId)")
    LiveData<Integer> isFavoriteMovie(long movieId);

    @Insert
    void saveFavoriteMovie(Movie movie);

    @Delete
    void removeFavoriteMovie(Movie movie);

}