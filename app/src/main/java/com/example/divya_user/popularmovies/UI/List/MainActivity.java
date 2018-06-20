package com.example.divya_user.popularmovies.UI.List;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.divya_user.popularmovies.R;
import com.example.divya_user.popularmovies.UI.Detail.DetailActivity;
import com.example.divya_user.popularmovies.data.Room.Entity.Movie;
import com.example.divya_user.popularmovies.utilities.LoaderUtils;
import com.example.divya_user.popularmovies.data.NetworkUtils;

import java.util.List;


/**
 *
 * This class contains the code to present the network data to the Recycler View UI
 *
 */
public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>>,
        AdapterView.OnItemSelectedListener,
        MovieAdapter.MoviePosterClickListener
{

    private static final int ID_MOVIE_LOADER = 44;

    private static final String SORT_BY_KEY = "sort_by";

    private static final String DEFAULT_SORT_BY = "popular";

    private static final String SORT_BY_POSITION_STATE = "sort_by_position_state";

    private static final String RECYCLER_VIEW_STATE = "recycler_view_state";

    private static Parcelable mRecyclerViewState;

    private static final String CLICKED_MOVIE_OBJECT = "clicked_movie_object";

    private boolean spinnerTouched = false;

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private Spinner mSortBySpinner;

    private TextView mRefreshButton;

    private ProgressBar mLoading;

    private RelativeLayout mErrorLayout;

    private LinearLayout mMovieLayout;

    // The page number from which data is returned by movieDB API
    private static int pageNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSortBySpinner = findViewById(R.id.spinner_sort_by);
        mSortBySpinner.setOnItemSelectedListener(this);
        mSortBySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    spinnerTouched = true;
                }

                return false;
            }
        });

        mRecyclerView = findViewById(R.id.rv_movies);

        mRefreshButton =findViewById(R.id.tv_refresh);
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int spinnerPosition = mSortBySpinner.getSelectedItemPosition();
                loadMovieData(getSortByKeyFromPosition(spinnerPosition));
            }
        });

        mLoading = findViewById(R.id.pb_loading);

        mErrorLayout = findViewById(R.id.rl_error_layout);

        mMovieLayout = findViewById(R.id.ll_movie_layout);

        int gridSpanCount = 3;

        GridLayoutManager layoutManager
                = new GridLayoutManager(this, gridSpanCount);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        String sortByKey = DEFAULT_SORT_BY;

        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey(SORT_BY_POSITION_STATE)) {
                int spinnerPosition = savedInstanceState.getInt(SORT_BY_POSITION_STATE);
                mSortBySpinner.setSelection(spinnerPosition);
                sortByKey = getSortByKeyFromPosition(spinnerPosition);
            }
            if(savedInstanceState.containsKey(RECYCLER_VIEW_STATE)) {
                mRecyclerViewState = savedInstanceState.getParcelable(RECYCLER_VIEW_STATE);
            }
        }

        loadMovieData(sortByKey);
    }

    /**
     * Returns the key based on the position of the Sort By Label selected
     *
     * @param position of the Sort By Label in the spinner
     */
    private String getSortByKeyFromPosition(int position) {
        return getResources().getStringArray(R.array.spinner_keys)[position];
    }

    /**
     * Creates a bundle for the loader and creates or restarts the loader to talk to the movieDB API
     * in a background thread
     *
     * @param sortByKey This is the key for the Sort By Label selected in the spinner
     */
    private void loadMovieData(String sortByKey) {

        //If the sort by category is 'Favorites' then get the data from the local database
        //Or else restart the loader to get the information from the API
        if(sortByKey.equals(getString(R.string.favorite_spinner_key))) {
            mLoading.setVisibility(View.VISIBLE);
            MainActivityViewModel mainActivityViewModel = ViewModelProviders
                                                            .of(this)
                                                            .get(MainActivityViewModel.class);
            mainActivityViewModel.getFavoriteMovies().observe(this, favoriteMovies -> {
                mMovieAdapter.setMovieData(favoriteMovies);
                mLoading.setVisibility(View.INVISIBLE);
            });

        } else {
            Bundle loaderBundle = new Bundle();
            loaderBundle.putString(SORT_BY_KEY, sortByKey);

            mLoading.setVisibility(View.VISIBLE);

            if (NetworkUtils.isOnline(this)) {
                showMovieLayout();
                if (getSupportLoaderManager().getLoader(ID_MOVIE_LOADER) != null) {
                    getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER, loaderBundle, this);
                } else {
                    getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, loaderBundle, this);
                }
            } else {
                mLoading.setVisibility(View.INVISIBLE);
                showErrorLayout();
            }
        }
    }

    private void showErrorLayout() {
        mMovieLayout.setVisibility(View.INVISIBLE);
        mErrorLayout.setVisibility(View.VISIBLE);
    }

    private void showMovieLayout(){
        mErrorLayout.setVisibility(View.INVISIBLE);
        mMovieLayout.setVisibility(View.VISIBLE);
    }


    /**
     * This method handles spinner item selection. Only if the spinner is touched, the loader's data is
     * invalidated and then it is restarted. This is to avoid loader being restarted for a second
     * time during orientation changes
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(spinnerTouched) {
            invalidateData();
            loadMovieData(getSortByKeyFromPosition(position));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * This method removes any saved data or layout state of the Recycler View
     *
     */
    private void invalidateData() {
        mMovieAdapter.setMovieData(null);
        mRecyclerViewState = null;
    }

    /**
     * Called when a movie poster is clicked. It creates an explicit intent to the DetailActivity class
     *
     * @param clickedMovieObject this object is passed from the ViewHolder It needs to be a Parcelable class
     */
    @Override
    public void onPosterClicked(Movie clickedMovieObject) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(CLICKED_MOVIE_OBJECT, clickedMovieObject);
        startActivity(intent);
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
    public Loader<List<Movie>> onCreateLoader(int loaderId, @Nullable Bundle bundle) {
        switch (loaderId) {

            case ID_MOVIE_LOADER:
                if(bundle != null) {
                    return new LoaderUtils.MovieAsyncTaskLoader(bundle.getString(SORT_BY_KEY), pageNumber, this);
                }
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    /**
     * Called when a Loader has finished loading its data.
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
        mLoading.setVisibility(View.INVISIBLE);
        if(null == data) {
            Toast.makeText(this, "Oops", Toast.LENGTH_LONG).show();
        } else {
            mMovieAdapter.setMovieData(data);
            if(mRecyclerViewState != null) {
                mRecyclerView.getLayoutManager().onRestoreInstanceState(mRecyclerViewState);
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
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {

    }

    /**
     * Called when to save the state of the spinner and the RecyclerView in order for it to be restored
     * when we come back to the activity
     *
     * @param outState The bundle in which to save the state
     */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SORT_BY_POSITION_STATE, mSortBySpinner.getSelectedItemPosition());
        outState.putParcelable(RECYCLER_VIEW_STATE, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }
}