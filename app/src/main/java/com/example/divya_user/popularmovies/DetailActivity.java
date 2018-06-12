package com.example.divya_user.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.divya_user.popularmovies.databinding.MovieDetailActivityBinding;
import com.example.divya_user.popularmovies.model.Movie;
import com.example.divya_user.popularmovies.utilities.DateUtils;
import com.example.divya_user.popularmovies.utilities.LoaderUtils;
import com.example.divya_user.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements
        TrailerAdapter.TrailerClickListener,
        LoaderManager.LoaderCallbacks<List<String>>

{

    private static final int ID_TRAILER_LOADER = 100;

    private static final String CLICKED_MOVIE_OBJECT = "clicked_movie_object";

    private static final String MOVIE_ID = "MOVIE_ID";

    private MovieDetailActivityBinding mMovieBinding;

    private RecyclerView mTrailerRecyclerView;

    private TrailerAdapter mTrailerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMovieBinding = DataBindingUtil.setContentView(this, R.layout.movie_detail_activity);

        Intent intent = getIntent();
        if(intent.hasExtra(CLICKED_MOVIE_OBJECT)) {
            final Movie movie = intent.getParcelableExtra(CLICKED_MOVIE_OBJECT);

            updateActionBarTitle(movie.getTitle());
            populateMovieData(movie);

            mMovieBinding.trailersError.tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadTrailers(movie.getMovieId());
                }
            });
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

        loadTrailers(movie.getMovieId());

    }

    /**
     * Creates a bundle for the loader and creates or restarts the loader to talk to the movieDB API
     * in a background thread
     *
     * @param movieId This is the movieId for which to retrieve the trailers
     */
    private void loadTrailers(long movieId) {
        Bundle loaderBundle = new Bundle();
        loaderBundle.putLong(MOVIE_ID, movieId);

        mMovieBinding.pbLoading.setVisibility(View.VISIBLE);

        if(NetworkUtils.isOnline(this)) {
            showTrailerLayout();
            if (getSupportLoaderManager().getLoader(ID_TRAILER_LOADER) != null) {
                getSupportLoaderManager().restartLoader(ID_TRAILER_LOADER, loaderBundle, this);
            } else {
                getSupportLoaderManager().initLoader(ID_TRAILER_LOADER, loaderBundle, this);
            }
        } else {
            mMovieBinding.pbLoading.setVisibility(View.INVISIBLE);
            showErrorLayout();
        }
    }

    private void showErrorLayout() {
        mMovieBinding.rvTrailers.setVisibility(View.INVISIBLE);
        mMovieBinding.trailersError.rlErrorLayout.setVisibility(View.VISIBLE);
    }

    private void showTrailerLayout(){
        mMovieBinding.trailersError.rlErrorLayout.setVisibility(View.INVISIBLE);
        mMovieBinding.rvTrailers.setVisibility(View.VISIBLE);
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

     /**
     * Called when a new Loader needs to be
     * created.
     *
     * @param loaderId The loader ID for which we need to create a loader
     * @param bundle   Any arguments supplied by the caller
     * @return A new Loader instance that is ready to start loading.
     */
    @NonNull
    @Override
    public Loader<List<String>> onCreateLoader(int loaderId, @Nullable Bundle bundle) {
        switch (loaderId) {

            case ID_TRAILER_LOADER:
                if(bundle != null) {
                    return new LoaderUtils.TrailersAsyncTaskLoader(bundle.getLong(MOVIE_ID), this);
                }
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    /**
     * Called when a Loader has finished loading its data.
     *
     * @param loader The Loader that has finished.
     * @param trailerList   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(@NonNull Loader<List<String>> loader, List<String> trailerList) {
        mMovieBinding.pbLoading.setVisibility(View.INVISIBLE);
        if(null == trailerList) {
            Toast.makeText(this, "Oops", Toast.LENGTH_LONG).show();
        } else {
            mTrailerAdapter.setTrailerKeys(trailerList);
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus making its data unavailable.
     * Removing reference to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(@NonNull Loader<List<String>> loader) {

    }
}
