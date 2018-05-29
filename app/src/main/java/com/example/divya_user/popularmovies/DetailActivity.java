package com.example.divya_user.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.divya_user.popularmovies.databinding.MovieDetailActivityBinding;
import com.example.divya_user.popularmovies.model.Movie;
import com.example.divya_user.popularmovies.utilities.DateUtils;
import com.example.divya_user.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String CLICKED_MOVIE_OBJECT = "clicked_movie_object";

    private MovieDetailActivityBinding mMovieBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMovieBinding = DataBindingUtil.setContentView(this, R.layout.movie_detail_activity);

        Intent intent = getIntent();
        if(intent.hasExtra(CLICKED_MOVIE_OBJECT)) {
            Movie movie = intent.getParcelableExtra(CLICKED_MOVIE_OBJECT);

            updateActionBarTitle(movie.getTitle());
            populateMovieData(movie);
        }
    }

    private void updateActionBarTitle(String title) {
        setTitle(title);
    }

    private void populateMovieData(Movie movie) {

        //Set the backdrop image
        Picasso.with(this)
                .load(NetworkUtils.getBaseBackdropImageURL() + movie.getBackdropPath())
                .into(mMovieBinding.ivBackdropPoster);

        //Set the poster image
        Picasso.with(this)
                .load(NetworkUtils.getBasePosterImageURL() + movie.getPosterPath())
                .into(mMovieBinding.ivMoviePoster);

        //Set the original title
        mMovieBinding.tvOriginalTitle.setText(movie.getOriginalTitle());

        //Set the release month and year
        mMovieBinding.tvReleaseDate.setText(DateUtils.getDateAsFormattedString(movie.getReleaseDate(), "month_year"));

        //Set the user rating in the rating bar
        mMovieBinding.ratingBar.setMax(10);
        mMovieBinding.ratingBar.setStepSize(0.1f);
        mMovieBinding.ratingBar.setRating((float) movie.getUserRating());

        //Set the user rating text
        mMovieBinding.tvUserRating.setText(Double.toString(movie.getUserRating()));

        //Set the total user count
        mMovieBinding.tvUserRatingCount.setText(Integer.toString(movie.getUserRatingCount()));

        //Set the plot synopsis
        mMovieBinding.tvPlotSynopsis.setText(movie.getPlotSynopsis());

    }
}
