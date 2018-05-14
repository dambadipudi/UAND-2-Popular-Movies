package com.example.divya_user.popularmovies.model;

/**
 * Created by Divya on 5/9/2018.
 *
 * This class contains the Movie object that we obtain from the Movie DB API
 *
 * Added a constructor to create the Movie object with all values on 5/12/2018
 *
 */

public class Movie {

    private String originalTitle;

    private String posterPath;

    private String backdropPath;

    private String plotSynopsis; // It is called overview in the API

    private double userRating; // It is called vote_average in the API

    private int userRatingCount; //It is called vote_count in the API

    private String releaseDate;

    public Movie(String originalTitle,
                 String posterPath,
                 String backdropPath,
                 String plotSynopsis,
                 double userRating,
                 int userRatingCount,
                 String releasedDate) {
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.userRatingCount = userRatingCount;
        this.releaseDate = releasedDate;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRatingCount(int userRatingCount) {
        this.userRatingCount = userRatingCount;
    }

    public int getUserRatingCount() {
        return userRatingCount;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        return "originalTitle: " + originalTitle + " posterPath: " + posterPath + " backdropPath " +
                backdropPath + "plotSynopsis: " + plotSynopsis + "userRating: " + userRating +
                "userRatingCount: " + userRatingCount + " releaseDate: " + releaseDate;
    }
}
