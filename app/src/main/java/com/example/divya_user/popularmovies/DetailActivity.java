package com.example.divya_user.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.divya_user.popularmovies.model.Movie;

public class DetailActivity extends AppCompatActivity {

    private static final String CLICKED_MOVIE_OBJECT = "clicked_movie_object";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);

        Intent intent = getIntent();
        if(intent.hasExtra(CLICKED_MOVIE_OBJECT)) {
            Movie movie = intent.getParcelableExtra(CLICKED_MOVIE_OBJECT);
            System.out.println(movie.toString());

            setTitle(movie.getTitle());
        }
    }
}
