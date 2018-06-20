package com.example.divya_user.popularmovies.UI.Detail;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.divya_user.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 *
 * This class Binds the Trailer URLs to the Trailer ViewHolders created
 *
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private List<String> mTrailerKeys;

    private Context mContext;

    final private TrailerClickListener mOnClickListener;

    /**
     * Constructor for TrailerAdapter that accepts the context of the calling class
     * and a class that implements the TrailerClickListener interface
     *
     * @param context of the calling class
     * @param listener is of the class that implements the clicklistener, which is the calling class in this case
     */
    public TrailerAdapter(Context context, TrailerClickListener listener) {
        mContext = context;
        mOnClickListener = listener;
    }

    public interface TrailerClickListener {
        void onTrailerClicked(String trailerKey);
    }

    /**
     * This method creates the view holder from the inflated layout for item
     *
     * @param viewGroup
     * @param viewType
     * @return TrailerAdapterViewHolder
     */
    @NonNull
    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.movie_trailer_item, viewGroup, false);
        return new TrailerAdapterViewHolder(view);
    }

    /**
     *
     * This method binds the items in the view holder with the data
     * Uses Picasso to load thumbnails
     *
     * @param trailerAdapterViewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull TrailerAdapterViewHolder trailerAdapterViewHolder, int position) {
        String trailerKey = mTrailerKeys.get(position);

        Picasso.with(mContext)
                .load("https://i1.ytimg.com/vi/"+trailerKey+"/default.jpg")
                .placeholder(R.drawable.ic_play_circle_filled)
                .error(R.drawable.ic_error)
                .into(trailerAdapterViewHolder.mTrailerImageView);

        trailerAdapterViewHolder.itemView.setTag(trailerKey);
    }

    /**
     * @return count of total number of data items
     */
    @Override
    public int getItemCount() {
        if (null == mTrailerKeys) return 0;
        return mTrailerKeys.size();
    }

    /**
     * This inner class is used to create the ViewHolder object mapped to the layout elements
     */
    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        final ImageView mTrailerImageView;

        /**
         *  Constructor to initialise the layout elements
         *
         * @param view
         */
        TrailerAdapterViewHolder(View view) {
            super(view);
            mTrailerImageView = view.findViewById(R.id.iv_trailer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onTrailerClicked((String) v.getTag());
        }
    }

    /**
     *
     * This method can be accessed to set or modify data
     * @param trailerKeys
     */
    public void setTrailerKeys(List<String> trailerKeys) {
        mTrailerKeys = trailerKeys;
        notifyDataSetChanged();
    }

}
