package com.example.divya_user.popularmovies.UI.Detail;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.divya_user.popularmovies.R;
import com.example.divya_user.popularmovies.databinding.MovieDetailActivityBinding;
import com.example.divya_user.popularmovies.data.Room.Entity.Movie;
import com.example.divya_user.popularmovies.utilities.DateUtils;
import com.example.divya_user.popularmovies.utilities.LoaderUtils;
import com.example.divya_user.popularmovies.data.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements
        TrailerAdapter.TrailerClickListener,
        ReviewAdapter.ReviewClickListener {

    private static final int ID_TRAILER_LOADER = 100;

    private static final int ID_REVIEW_LOADER = 110;

    private static final String CLICKED_MOVIE_OBJECT = "clicked_movie_object";

    private static final String DETAIL_SCROLL_POSITION = "detail_scroll_position";

    private static final String MOVIE_ID = "MOVIE_ID";

    private MovieDetailActivityBinding mMovieBinding;

    private ScrollView mScrollView;

    private RecyclerView mTrailerRecyclerView;

    private TrailerAdapter mTrailerAdapter;

    private RecyclerView mReviewRecyclerView;

    private ReviewAdapter mReviewAdapter;

    private static final String REVIEWS_STATE = "reviews_state";

    private static final String TRAILERS_STATE = "trailers_state";

    private static Parcelable mReviewsRecyclerViewState;

    private static Parcelable mTrailersRecyclerViewState;

    private int[] mScrollPosition = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMovieBinding = DataBindingUtil.setContentView(this, R.layout.movie_detail_activity);

        mScrollView = findViewById(R.id.scrollview);

        Intent intent = getIntent();
        if (intent.hasExtra(CLICKED_MOVIE_OBJECT)) {
            final Movie movie = intent.getParcelableExtra(CLICKED_MOVIE_OBJECT);

            mScrollView.smoothScrollTo(0,0);
            mReviewsRecyclerViewState = null;
            mTrailersRecyclerViewState = null;
            updateActionBarTitle(movie.getTitle());
            populateMovieData(movie);

            mMovieBinding.trailers.error.tvRefresh.setOnClickListener((View view) ->
                    loadTrailers(movie.getMovieId())
            );

            mMovieBinding.reviews.error.tvRefresh.setOnClickListener((View view) ->
                    loadReviews(movie.getMovieId())
            );


        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mScrollPosition != null) {
            mScrollView.smoothScrollTo(mScrollPosition[0], mScrollPosition[1]);
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

        setupFavoriteButton(movie);

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

    private void setupFavoriteButton(Movie movie) {
        DetailActivityViewModel detailActivityViewModel = ViewModelProviders
                .of(this)
                .get(DetailActivityViewModel.class);

        FloatingActionButton fabFavoriteButton = mMovieBinding.fabFavorite;

        detailActivityViewModel.isFavoriteMovie(movie.getMovieId()).observe(this, isFavorite -> {
            if(isFavorite == 1) {
                fabFavoriteButton.setImageResource(R.drawable.ic_favorite_selected);
                fabFavoriteButton.setTag(R.string.state_is_favorite);
            } else if(isFavorite == 0) {
                fabFavoriteButton.setImageResource(R.drawable.ic_favorite);
                fabFavoriteButton.setTag(R.string.state_not_favorite);
            }
        });

        fabFavoriteButton.setOnClickListener((View view) -> {
            if (fabFavoriteButton.getTag().equals(R.string.state_not_favorite)) {
                detailActivityViewModel.saveFavoriteMovie(movie);
            } else if(fabFavoriteButton.getTag().equals(R.string.state_is_favorite)) {
                detailActivityViewModel.removeFavoriteMovie(movie);
            }

        });
    }

    /**
     * Creates a bundle for the loader and creates or restarts the loader to talk to the movieDB API
     * in a background thread
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
                if(mTrailersRecyclerViewState != null) {
                    mTrailerRecyclerView.getLayoutManager().onRestoreInstanceState(mTrailersRecyclerViewState);
                }
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
                if(mReviewsRecyclerViewState != null) {
                    mReviewRecyclerView.getLayoutManager().onRestoreInstanceState(mReviewsRecyclerViewState);
                }
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

    /**
     * This method is called before starting another activity and also for orientation change
     * This saves the state of the Reviews and Trailers RecyclerView in order for them to be retrieved
     * when we come back to the activity
     *
     * @param outState The bundle in which to save the state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mReviewsRecyclerViewState = null;
        mTrailersRecyclerViewState = null;
        mScrollPosition = null;
        outState.putParcelable(REVIEWS_STATE, mReviewRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(TRAILERS_STATE, mTrailerRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putIntArray(DETAIL_SCROLL_POSITION,
                new int[]{ mScrollView.getScrollX(), mScrollView.getScrollY()});
    }

    /**
     * This method is called for orientation changes
     * This retrieves the state of the Reviews and Trailers RecyclerView in order
     * when we come back to the activity
     * Since we overrode onSaveInstanceState and onRestoreInstanceState, we will need to save scrollview position
     * in the case of orientation change. 
     *
     * @param inState The bundle from which to retrieve the state
     */
    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        mReviewsRecyclerViewState = inState.getParcelable(REVIEWS_STATE);
        mTrailersRecyclerViewState = inState.getParcelable(TRAILERS_STATE);
        mScrollPosition = inState.getIntArray(DETAIL_SCROLL_POSITION);
    }


}