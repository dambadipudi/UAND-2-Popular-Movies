package com.example.divya_user.popularmovies.data.Room.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.divya_user.popularmovies.data.Room.Entity.Movie;

import java.util.List;

@Dao
public interface MovieDAO {

    @Query("SELECT * from favorite_movie")
    LiveData<List<Movie>> getFavoriteMovies();

}
