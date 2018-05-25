package com.example.divya_user.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.example.divya_user.popularmovies.model.Movie;
import com.example.divya_user.popularmovies.utilities.JSONUtils;
import com.example.divya_user.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;


/**
 *
 * This class contains the code to present the network data to the Recycler View UI
 *
 */
public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>>
         {

    private static final int ID_MOVIE_LOADER = 44;

    private int gridSpanCount = 3;

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    // The page number from which data is returned by movieDB API
    private static int pageNumber = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movies);

        GridLayoutManager layoutManager
                = new GridLayoutManager(this, gridSpanCount);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);

    }

     /**
      * Loader class to make the network call to the movie DB API
      */
     private static class MovieAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {


        List<Movie> cachedMoviesList;
        Context mContext;

        public MovieAsyncTaskLoader(@NonNull Context context) {
            super(context);
            mContext = context;
        }

        @Override
        protected void onStartLoading() {
            if (cachedMoviesList != null) {
                //To skip loadInBackground call
                deliverResult(cachedMoviesList);
            }else{
                forceLoad();
            }
        }

        /**
        * Called when a load is forced
        *
        * @return A List of Movie objects created from JSON Response
        * obtained from network call to movie DB APU
        */
        @Nullable
        @Override
        public List<Movie> loadInBackground() {
            URL movieURL = NetworkUtils.getPopularMoviesURL(pageNumber);
            String movieJSON;
            try {
                if(NetworkUtils.isOnline(mContext)) {
                    movieJSON = NetworkUtils.getResponseFromHttpUrl(movieURL);
                    if(movieJSON != null) {
                        List<Movie> moviesList = JSONUtils.getListOfMoviesFromJSON(movieJSON);
                        return moviesList;
                    }
                }
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

         @Override
         public void deliverResult(List<Movie> data) {
             cachedMoviesList = data;
             super.deliverResult(data);
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
    public Loader<List<Movie>> onCreateLoader(int loaderId, @Nullable Bundle bundle) {
        switch (loaderId) {

            case ID_MOVIE_LOADER:
                return new MainActivity.MovieAsyncTaskLoader(this);
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
        if(null == data) {
            Toast.makeText(this, "Oops", Toast.LENGTH_LONG).show();
        } else {
            mMovieAdapter.setMovieData(data);
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
}