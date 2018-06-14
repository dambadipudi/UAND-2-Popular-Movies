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
        ReviewAdapter.ReviewClickListener {

    private static final int ID_TRAILER_LOADER = 100;

    private static final int ID_REVIEW_LOADER = 110;

    private static final String CLICKED_MOVIE_OBJECT = "clicked_movie_object";

    private static final String MOVIE_ID = "MOVIE_ID";

    private MovieDetailActivityBinding mMovieBinding;

    private RecyclerView mTrailerRecyclerView;

    private TrailerAdapter mTrailerAdapter;

    private RecyclerView mReviewRecyclerView;

    private ReviewAdapter mReviewAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMovieBinding = DataBindingUtil.setContentView(this, R.layout.movie_detail_activity);

        Intent intent = getIntent();
        if (intent.hasExtra(CLICKED_MOVIE_OBJECT)) {
            final Movie movie = intent.getParcelableExtra(CLICKED_MOVIE_OBJECT);

            updateActionBarTitle(movie.getTitle());
            populateMovieData(movie);

            mMovieBinding.trailers.error.tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadTrailers(movie.getMovieId());
                }
            });

            mMovieBinding.reviews.error.tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadReviews(movie.getMovieId());
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
        mMovieBinding.reviews.ratingBar.setMax(10);
        mMovieBinding.reviews.ratingBar.setStepSize(0.1f);
        mMovieBinding.reviews.ratingBar.setRating((float) movie.getUserRating());

        //Set the user rating text
        mMovieBinding.reviews.tvUserRating.setText(Double.toString(movie.getUserRating()));

        //Set the total user count
        mMovieBinding.reviews.tvUserRatingCount.setText(Integer.toString(movie.getUserRatingCount()));

        //Set the plot synopsis
        mMovieBinding.tvPlotSynopsis.setText(movie.getPlotSynopsis());

        //Set the movie trailers
        mTrailerRecyclerView = mMovieBinding.trailers.rvTrailers;

        LinearLayoutManager trailerLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTrailerRecyclerView.setLayoutManager(trailerLayoutManager);

        mTrailerRecyclerView.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter(this, this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        loadTrailers(movie.getMovieId());

        //Set the movie reviews
        mReviewRecyclerView = mMovieBinding.reviews.rvReviews;

        LinearLayoutManager reviewLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewRecyclerView.setLayoutManager(reviewLayoutManager);

        mReviewRecyclerView.setHasFixedSize(true);

        mReviewAdapter = new ReviewAdapter(this, this);
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        loadReviews(movie.getMovieId());

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

        mMovieBinding.trailers.pbLoading.setVisibility(View.VISIBLE);

        if (NetworkUtils.isOnline(this)) {
            showTrailerLayout();
            if (getSupportLoaderManager().getLoader(ID_TRAILER_LOADER) != null) {
                getSupportLoaderManager().restartLoader(ID_TRAILER_LOADER, loaderBundle, trailersLoaderListener);
            } else {
                getSupportLoaderManager().initLoader(ID_TRAILER_LOADER, loaderBundle, trailersLoaderListener);
            }
        } else {
            mMovieBinding.trailers.pbLoading.setVisibility(View.INVISIBLE);
            showTrailerErrorLayout();
        }
    }

    /**
     * Creates a bundle for the loader and creates or restarts the loader to talk to the movieDB API
     * in a background thread
     *
     * @param movieId This is the movieId for which to retrieve the reviews
     */
    private void loadReviews(long movieId) {
        Bundle loaderBundle = new Bundle();
        loaderBundle.putLong(MOVIE_ID, movieId);

        mMovieBinding.reviews.pbLoading.setVisibility(View.VISIBLE);

        if (NetworkUtils.isOnline(this)) {
            showReviewLayout();
            if (getSupportLoaderManager().getLoader(ID_REVIEW_LOADER) != null) {
                getSupportLoaderManager().restartLoader(ID_REVIEW_LOADER, loaderBundle, reviewsLoaderListener);
            } else {
                getSupportLoaderManager().initLoader(ID_REVIEW_LOADER, loaderBundle, reviewsLoaderListener);
            }
        } else {
            mMovieBinding.reviews.pbLoading.setVisibility(View.INVISIBLE);
            showReviewErrorLayout();
        }
    }

    private void showTrailerErrorLayout() {
        mMovieBinding.trailers.rvTrailers.setVisibility(View.INVISIBLE);
        mMovieBinding.trailers.error.rlErrorLayout.setVisibility(View.VISIBLE);
    }

    private void showTrailerLayout() {
        mMovieBinding.trailers.error.rlErrorLayout.setVisibility(View.INVISIBLE);
        mMovieBinding.trailers.rvTrailers.setVisibility(View.VISIBLE);
    }

    private void showReviewErrorLayout() {
        mMovieBinding.reviews.rvReviews.setVisibility(View.INVISIBLE);
        mMovieBinding.reviews.error.rlErrorLayout.setVisibility(View.VISIBLE);
    }

    private void showReviewLayout() {
        mMovieBinding.reviews.error.rlErrorLayout.setVisibility(View.INVISIBLE);
        mMovieBinding.reviews.rvReviews.setVisibility(View.VISIBLE);
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

    @Override
    public void onReviewClicked(String reviewURL) {

        Uri reviewUri = Uri.parse(reviewURL);

        Intent implicitIntent = new Intent(Intent.ACTION_VIEW, reviewUri);
        if (implicitIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(implicitIntent);
        }
    }

    private LoaderManager.LoaderCallbacks<List<String>> trailersLoaderListener = new LoaderManager.LoaderCallbacks<List<String>>() {

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
                    if (bundle != null) {
                        return new LoaderUtils.TrailersAsyncTaskLoader(bundle.getLong(MOVIE_ID), DetailActivity.this);
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
            mMovieBinding.trailers.pbLoading.setVisibility(View.INVISIBLE);
            if (null == trailerList) {
                Toast.makeText(DetailActivity.this, "Oops", Toast.LENGTH_LONG).show();
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
    };

    private LoaderManager.LoaderCallbacks<List<Movie.Review>> reviewsLoaderListener = new LoaderManager.LoaderCallbacks<List<Movie.Review>>() {

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
        public Loader<List<Movie.Review>> onCreateLoader(int loaderId, @Nullable Bundle bundle) {
            switch (loaderId) {

                case ID_REVIEW_LOADER:
                    if (bundle != null) {
                        return new LoaderUtils.ReviewsAsyncTaskLoader(bundle.getLong(MOVIE_ID), DetailActivity.this);
                    }
                default:
                    throw new RuntimeException("Loader Not Implemented: " + loaderId);
            }
        }

        /**
         * Called when a Loader has finished loading its data.
         *
         * @param loader The Loader that has finished.
         * @param reviewsList   The data generated by the Loader.
         */
        @Override
        public void onLoadFinished(@NonNull Loader<List<Movie.Review>> loader, List<Movie.Review> reviewsList) {
            mMovieBinding.reviews.pbLoading.setVisibility(View.INVISIBLE);
            if (null == reviewsList) {
                Toast.makeText(DetailActivity.this, "Oops", Toast.LENGTH_LONG).show();
            } else {
                mReviewAdapter.setReviews(reviewsList);
            }
        }

        /**
         * Called when a previously created loader is being reset, and thus making its data unavailable.
         * Removing reference to the Loader's data.
         *
         * @param loader The Loader that is being reset.
         */
        @Override
        public void onLoaderReset(@NonNull Loader<List<Movie.Review>> loader) {

        }
    };
}