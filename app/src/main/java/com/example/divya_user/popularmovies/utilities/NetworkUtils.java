package com.example.divya_user.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.example.divya_user.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Divya on 5/10/2018.
 *
 * This class contains the methods to create the URL and make the network calls to the movie DB API
 *
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    //For both top rated and popular movies, we are using the movie endpoint, so that is the base URL
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    //the movie/top_rated endpoint in order to sort the movies by top rated
    private static final String TOP_RATED_PATH = "top_rated";

    //the movie/popular endpoint in order to sort the movies by most popular
    private static final String POPULAR_PATH = "popular";

    //The API_Key that is required to talk to the movie DB API
    private static final String API_KEY_PARAM = "api_key";

    //The page number to be specified for subsequent requests
    private static final String PAGE_PARAM = "page";


    /**
     * Retrieves the URL to query for the Top Rated Movies List
     *
     * @param pageNumber in the API so can download the list of movies from that page
     * @return URL to query the movie/top_rated endpoint
     */
    public static URL getTopRatedMoviesURL(int pageNumber) {
        return buildMovieDBURL(TOP_RATED_PATH, pageNumber);
    }

    /**
     * Retrieves the URL to query for the Popular Movies List
     *
     * @param pageNumber in the API so can download the list of movies from that page
     * @return URL to query the movie/popular endpoint
     */
    public static URL getPopularMoviesURL(int pageNumber) {
        return buildMovieDBURL(POPULAR_PATH, pageNumber);
    }

    /**
     * Builds the URL used to talk to the movie DB API. It builds the URL based on the
     * sort type and the page number to be downloaded
     *
     * @param sortType Either top_rated or popular
     * @param pageNumber The movies to be downloaded based on the page number
     * @return The URL to use to query the movie DB API
     */
    private static URL buildMovieDBURL(String sortType, int pageNumber) {

        Uri movieDBUri = Uri.parse(BASE_URL + sortType).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY)
                .appendQueryParameter(PAGE_PARAM, Integer.toString(pageNumber))
                .build();

        try {
            URL movieDBURL = new URL(movieDBUri.toString());
            return movieDBURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
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
}