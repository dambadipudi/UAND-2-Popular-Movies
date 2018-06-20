package com.example.divya_user.popularmovies.data.Room.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.divya_user.popularmovies.data.Room.DAO.MovieDAO;
import com.example.divya_user.popularmovies.data.Room.Entity.Movie;

@Database(entities= {Movie.class}, version = 1, exportSchema = false )
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "popular_movies";
    private static AppDatabase sInstance;

    public abstract MovieDAO movieDAO();

    public static AppDatabase getInstance(Context context) {
        if(sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class,
                        DATABASE_NAME).build();
            }
        }
        return sInstance;
    }
}
