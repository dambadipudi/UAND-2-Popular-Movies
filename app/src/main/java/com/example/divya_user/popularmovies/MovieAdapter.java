package com.example.divya_user.popularmovies;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.divya_user.popularmovies.model.Movie;
import com.example.divya_user.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 *
 * This class Binds the Movie data to the ViewHolders created
 *
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private List<Movie> mMovieList;

    private Context mContext;

    public MovieAdapter(Context context) {
        mContext = context;
    }

    /**
     * This method creates the view holder from the inflated layout for item
     *
     * @param viewGroup
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext)
                                .inflate(R.layout.movie_poster_item, viewGroup, false);
        return new MovieAdapterViewHolder(view);
    }

    /**
     *
     * This method binds the items in the view holder with the data
     * Uses Picasso to load images
     *
     * @param movieAdapterViewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        Movie currentMovie = mMovieList.get(position);

        Picasso.with(mContext)
                .load(NetworkUtils.getBasePosterImageURL() + currentMovie.getPosterPath())
                .into(movieAdapterViewHolder.mPosterImageView);

        movieAdapterViewHolder.mTitle.setText(currentMovie.getTitle());
        movieAdapterViewHolder.mReleaseYear.setText(currentMovie.getReleaseDate());

    }

    /**
     * @return count of total number of data items
     */
    @Override
    public int getItemCount() {
        if (null == mMovieList) return 0;
        return mMovieList.size();
    }

    /**
     * This inner class is used to create the ViewHolder object mspped to the layout elements
     */
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mPosterImageView;
        public final TextView mTitle;
        public final TextView mReleaseYear;

        /**
         *  Constructor to initialise the layout elements
         *
         * @param view
         */
        public MovieAdapterViewHolder(View view) {
            super(view);
            mPosterImageView = (ImageView) view.findViewById(R.id.iv_movie_poster);
            mTitle = (TextView) view.findViewById(R.id.tv_title);
            mReleaseYear = (TextView) view.findViewById(R.id.tv_release_year);
        }
    }

    /**
     *
     * This method can be accessed to set or modify data
     * @param movieList
     */
    public void setMovieData(List<Movie> movieList) {
        mMovieList = movieList;
        notifyDataSetChanged();
    }

}
