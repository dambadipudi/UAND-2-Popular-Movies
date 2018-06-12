package com.example.divya_user.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * This class contains the Movie object that we obtain from the Movie DB API
 *
 */

public class Movie implements Parcelable{

    private long movieId;

    private String title;

    private String originalTitle;

    private String posterPath;

    private String backdropPath;

    private String plotSynopsis; // It is called overview in the API

    private double userRating; // It is called vote_average in the API

    private int userRatingCount; //It is called vote_count in the API

    private String releaseDate;

    public Movie(long movieId,
                 String title,
                 String originalTitle,
                 String posterPath,
                 String backdropPath,
                 String plotSynopsis,
                 double userRating,
                 int userRatingCount,
                 String releasedDate) {
        this.movieId = movieId;
        this.title = title;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.userRatingCount = userRatingCount;
        this.releaseDate = releasedDate;
    }

    private Movie(Parcel in) {
        movieId = in.readLong();
        title = in.readString();
        originalTitle = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        plotSynopsis = in.readString();
        userRating = in.readDouble();
        userRatingCount = in.readInt();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return  "movieId: " + movieId +
                " title: " + title +
                " originalTitle: " + originalTitle +
                " posterPath: " + posterPath +
                " backdropPath: " + backdropPath +
                " plotSynopsis: " + plotSynopsis +
                " userRating: " + userRating +
                " userRatingCount: " + userRatingCount +
                " releaseDate: " + releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(movieId);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(plotSynopsis);
        dest.writeDouble(userRating);
        dest.writeInt(userRatingCount);
        dest.writeString(releaseDate);
    }
}
