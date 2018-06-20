package com.example.divya_user.popularmovies.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.divya_user.popularmovies.data.NetworkUtils;
import com.example.divya_user.popularmovies.data.Room.Entity.Movie;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class LoaderUtils {

    /**
     * Loader class to make the network call to the movie DB API for Top Rated and Most Popular movies
     */
    public static class MovieAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {


        List<Movie> cachedMoviesList;
        String mSortByKey;
        int mPageNumber;

        public MovieAsyncTaskLoader(String sortByKey, int pageNumber, @NonNull Context context) {
            super(context);
            mSortByKey = sortByKey;
            mPageNumber = pageNumber;
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
            try {
                URL movieURL = NetworkUtils.getMovieDBURL(mSortByKey, mPageNumber);

                String movieJSON;

                if(movieURL != null) {
                    movieJSON = NetworkUtils.getResponseFromHttpUrl(movieURL);
                    if(movieJSON != null) {
                        return JSONUtils.getListOfMoviesFromJSON(movieJSON);
                    }
                }
                return null;
            } catch(MalformedURLException e) {
                e.printStackTrace();
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
     * Loader class to make the network call to the movie DB API to retrieve trailers
     */
    public static class TrailersAsyncTaskLoader extends AsyncTaskLoader<List<String>> {


        List<String> cachedTrailerList;
        long mMovieId;

        public TrailersAsyncTaskLoader(long movieId, @NonNull Context context) {
            super(context);
            mMovieId = movieId;
        }

        @Override
        protected void onStartLoading() {
            if (cachedTrailerList != null) {
                //To skip loadInBackground call
                deliverResult(cachedTrailerList);
            }else{
                forceLoad();
            }
        }

        /**
         * Called when a load is forced
         *
         * @return A List of Trailer Youtube Keys created from the JSON Response
         * obtained from network call to movie DB API
         */
        @Nullable
        @Override
        public List<String> loadInBackground() {
            try {
                URL trailerURL = NetworkUtils.getTrailersListURL(mMovieId);

                String trailerJSON;

                if(trailerURL != null) {
                    trailerJSON = NetworkUtils.getResponseFromHttpUrl(trailerURL);
                    if(trailerJSON != null) {
                        return JSONUtils.getListOfTrailersFromJSON(trailerJSON);
                    }
                }
                return null;
            } catch(MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void deliverResult(List<String> data) {
            cachedTrailerList = data;
            super.deliverResult(data);
        }

    }

    /**
     * Loader class to make the network call to the movie DB API to retrieve reviews
     */
    public static class ReviewsAsyncTaskLoader extends AsyncTaskLoader<List<Movie.Review>> {


        List<Movie.Review> cachedReviewList;
        long mMovieId;

        public ReviewsAsyncTaskLoader(long movieId, @NonNull Context context) {
            super(context);
            mMovieId = movieId;
        }

        @Override
        protected void onStartLoading() {
            if (cachedReviewList != null) {
                //To skip loadInBackground call
                deliverResult(cachedReviewList);
            }else{
                forceLoad();
            }
        }

        /**
         * Called when a load is forced
         *
         * @return A List of Movie.Review objects created from the JSON Response
         * obtained from network call to movie DB API
         */
        @Nullable
        @Override
        public List<Movie.Review> loadInBackground() {
            try {
                URL reviewURL = NetworkUtils.getReviewsURL(mMovieId);

                String reviewJSON;

                if(reviewURL != null) {
                    reviewJSON = NetworkUtils.getResponseFromHttpUrl(reviewURL);
                    if(reviewJSON != null) {
                        return JSONUtils.getListOfReviewsFromJSON(reviewJSON);
                    }
                }
                return null;
            } catch(MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void deliverResult(List<Movie.Review> data) {
            cachedReviewList = data;
            super.deliverResult(data);
        }

    }
}
