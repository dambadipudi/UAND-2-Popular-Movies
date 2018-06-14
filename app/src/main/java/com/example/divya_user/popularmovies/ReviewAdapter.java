package com.example.divya_user.popularmovies;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.divya_user.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 *
 * This class Binds the Review information to the Review ViewHolders created
 *
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private List<Movie.Review> mReviews;

    private Context mContext;

    final private ReviewClickListener mOnClickListener;

    /**
     * Constructor for ReviewAdapter that accepts the context of the calling class
     * and a class that implements the ReviewClickListener interface
     *
     * @param context of the calling class
     * @param listener is of the class that implements the clicklistener, which is the calling class in this case
     */
    public ReviewAdapter(Context context, ReviewClickListener listener) {
        mContext = context;
        mOnClickListener = listener;
    }

    public interface ReviewClickListener {
        void onReviewClicked(String reviewURL);
    }

    /**
     * This method creates the view holder from the inflated layout for item
     *
     * @param viewGroup
     * @param viewType
     * @return ReviewAdapterViewHolder
     */
    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.movie_review_item, viewGroup, false);
        return new ReviewAdapterViewHolder(view);
    }

    /**
     *
     * This method binds the items in the view holder with the data
     *
     * @param reviewAdapterViewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder reviewAdapterViewHolder, int position) {
        Movie.Review reviewObject = mReviews.get(position);

        reviewAdapterViewHolder.mAuthor.setText(reviewObject.getAuthor());

        reviewAdapterViewHolder.mContent.setText(reviewObject.getContent());

        reviewAdapterViewHolder.itemView.setTag(reviewObject.getURL());
    }

    /**
     * @return count of total number of data items
     */
    @Override
    public int getItemCount() {
        if (null == mReviews) return 0;
        return mReviews.size();
    }

    /**
     * This inner class is used to create the ViewHolder object mapped to the layout elements
     */
    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        final TextView mAuthor;
        final TextView mContent;

        /**
         *  Constructor to initialise the layout elements
         *
         * @param view
         */
        ReviewAdapterViewHolder(View view) {
            super(view);
            mAuthor = view.findViewById(R.id.tv_review_author);
            mContent = view.findViewById(R.id.tv_review_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onReviewClicked((String) v.getTag());
        }
    }

    /**
     *
     * This method can be accessed to set or modify data
     * @param reviewList
     */
    public void setReviews(List<Movie.Review> reviewList) {
        mReviews = reviewList;
        notifyDataSetChanged();
    }

}
