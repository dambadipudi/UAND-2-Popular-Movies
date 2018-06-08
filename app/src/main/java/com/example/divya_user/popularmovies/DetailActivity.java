package com.example.divya_user.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.divya_user.popularmovies.databinding.MovieDetailActivityBinding;
import com.example.divya_user.popularmovies.model.Movie;
import com.example.divya_user.popularmovies.utilities.DateUtils;
import com.example.divya_user.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements
        TrailerAdapter.TrailerClickListener
            {

    private static final String CLICKED_MOVIE_OBJECT = "clicked_movie_object";

    private MovieDetailActivityBinding mMovieBinding;

    private RecyclerView mTrailerRecyclerView;

    private TrailerAdapter mTrailerAdapter;

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
                .placeholder(R.drawable.ic_movie)
                .error(R.drawable.ic_error)
                .into(mMovieBinding.ivMoviePoster);

        //Set the original title
        mMovieBinding.tvOriginalTitle.setText(movie.getOriginalTitle());

        //Set the release month and year
        mMovieBinding.tvReleaseDate.setText(DateUtils.getDateAsFormattedString(movie.getReleaseDate(), "month_year"));

        //Set the user rating in the rating bar
        mMovieBinding.movieReviews.ratingBar.setMax(10);
        mMovieBinding.movieReviews.ratingBar.setStepSize(0.1f);
        mMovieBinding.movieReviews.ratingBar.setRating((float) movie.getUserRating());

        //Set the user rating text
        mMovieBinding.movieReviews.tvUserRating.setText(Double.toString(movie.getUserRating()));

        //Set the total user count
        mMovieBinding.movieReviews.tvUserRatingCount.setText(Integer.toString(movie.getUserRatingCount()));

        //Set the plot synopsis
        mMovieBinding.tvPlotSynopsis.setText(movie.getPlotSynopsis());

        //Set the movie trailers
        mTrailerRecyclerView = mMovieBinding.rvTrailers;

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTrailerRecyclerView.setLayoutManager(layoutManager);

        mTrailerRecyclerView.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter(this, this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        List<String> youtubeKeys = new ArrayList();
        youtubeKeys.add("Z5ezsReZcxU");
        youtubeKeys.add("2-5Wv9UGkN8");
        youtubeKeys.add("xZNBFcwd7zc");
        youtubeKeys.add("D86RtevtfrA");
        youtubeKeys.add("20bpjtCbCz0");
        youtubeKeys.add("bI31WqFDxNs");

        mTrailerAdapter.setTrailerKeys(youtubeKeys);

    }

        @Override
        public void onTrailerClicked(String trailerKey) {

            Uri trailerUri;
            try {
                trailerUri = NetworkUtils.getTrailerUri(trailerKey);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return;
            }

            Intent implicitIntent = new Intent(Intent.ACTION_VIEW, trailerUri);
            if (implicitIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(implicitIntent);
            }
        }
    }
