package com.example.divya_user.popularmovies.utilities;

import com.example.divya_user.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds methods to extract Movie objects from the JSON data returned by the API calls
 */

public class JSONUtils {

    private final static String TOTAL_PAGES = "total_pages";

    private final static String RESULTS = "results";

    private final static String TITLE = "title";

    private final static String ORIGINAL_TITLE = "original_title";

    private final static String POSTER_PATH = "poster_path";

    private final static String BACKDROP_PATH = "backdrop_path";

    private final static String PLOT_SYNOPSIS  = "overview";

    private final static String USER_RATING_COUNT = "vote_count";

    private final static String USER_RATING = "vote_average";

    private final static String RELEASE_DATE = "release_date";

    public static int getTotalPages(String jsonString) {
        try {
            JSONObject movieJSONObject = new JSONObject(jsonString);
            return movieJSONObject.getInt(TOTAL_PAGES);

        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static List<Movie> getListOfMoviesFromJSON(String jsonString) {
        try {
            JSONObject movieJSONObject = new JSONObject(jsonString);

            JSONArray resultsJSONArray = movieJSONObject.getJSONArray(RESULTS);

            List<Movie> moviesList = new ArrayList<>();

            for(int index = 0; index < resultsJSONArray.length(); index++) {

                JSONObject currentMovieJSONObject = resultsJSONArray.getJSONObject(index);
                Movie currentMovie = new Movie(currentMovieJSONObject.getString(TITLE),
                                                currentMovieJSONObject.getString(ORIGINAL_TITLE),
                                                currentMovieJSONObject.getString(POSTER_PATH),
                                                currentMovieJSONObject.getString(BACKDROP_PATH),
                                                currentMovieJSONObject.getString(PLOT_SYNOPSIS),
                                                currentMovieJSONObject.getDouble(USER_RATING),
                                                currentMovieJSONObject.getInt(USER_RATING_COUNT),
                                                currentMovieJSONObject.getString(RELEASE_DATE));

                moviesList.add(currentMovie);
            }

            return moviesList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

}
