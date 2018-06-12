package com.example.divya_user.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.example.divya_user.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * This class contains the methods to create the URL and make the network calls to the movie DB API
 *
 */

public class NetworkUtils {

    //For both top rated and popular movies, we are using the movie endpoint, so that is the base URL
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    //The Base URL to access the poster image
    private static final String POSTER_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String POSTER_SIZE = "w185/";

    private static final String BACKDROP_SIZE = "w342/";

    //the movie/top_rated endpoint in order to sort the movies by top rated
    private static final String TOP_RATED_PATH = "top_rated";

    //the movie/popular endpoint in order to sort the movies by most popular
    private static final String POPULAR_PATH = "popular";

    //the movie/top_rated endpoint in order to retrieve the trailers
    private static final String TRAILERS_PATH = "videos";

    //The API_Key that is required to talk to the movie DB API
    private static final String API_KEY_PARAM = "api_key";

    //The page number to be specified for subsequent requests
    private static final String PAGE_PARAM = "page";

    //For youtube Base URL
    private static final String YOUTUBE_BASE_URL = "https://youtube.com/watch";

    //The youtube video Key
    private static final String YOUTUBE_KEY_PARAM = "v";

    /**
     * Builds the URL used to talk to the movie DB API. It builds the URL based on the
     * sort type and the page number to be downloaded
     *
     * @param sortType Either top_rated or popular
     * @param pageNumber The movies to be downloaded based on the page number
     * @return The URL to use to query the movie DB API
     */
    public static URL getMovieDBURL(String sortType, int pageNumber) throws MalformedURLException {

        if(!(sortType.equals(POPULAR_PATH) || sortType.equals(TOP_RATED_PATH))) {
            return null;
        }

        Uri movieDBUri = Uri.parse(BASE_URL + sortType).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY)
                .appendQueryParameter(PAGE_PARAM, Integer.toString(pageNumber))
                .build();

        return new URL(movieDBUri.toString());
    }

    /**
     * Builds the URL used to talk to the movie DB API to retrieve the trailers. It builds the URL based on the
     * movieId
     *
     * @param movieId The movie ID for which to retrieve the trailers
     * @return The URL to use to query the movie DB API for trailers
     */
    public static URL getTrailersListURL(long movieId) throws MalformedURLException {

        Uri movieDBUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(Long.toString(movieId))
                .appendPath(TRAILERS_PATH)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY)
                .build();

        return new URL(movieDBUri.toString());
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * This method checks whether there the device is connected to the network
     *
     * @param context The context which calls this method
     * @return Whether the device is connected
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                                            context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null) {
                return networkInfo.isConnected();
            }
        }

        return false;
    }

    /**
     * This method returns the base url that needs to prepended to the poster path
     *
     * @return the path as a string
     */
    public static String getBasePosterImageURL() {
        return POSTER_IMAGE_BASE_URL + POSTER_SIZE;
    }

    /**
     * This method returns the base url that needs to prepended to the backdrop path
     *
     * @return the path as a string
     */
    public static String getBaseBackdropImageURL() {
        return POSTER_IMAGE_BASE_URL + BACKDROP_SIZE;
    }

    /**
     * Builds the Uri for the youtube trailer with the trailer key used to identify the video
     *
     * @param trailerKey The youtube's key to identify a video
     * @return The URL to use to query the movie DB API
     */
    public static Uri getTrailerUri(String trailerKey) throws MalformedURLException {

       return Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_KEY_PARAM, trailerKey)
                .build();
    }
}