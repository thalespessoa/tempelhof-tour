package com.gyg.tempelhoftour.reviews;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gyg.tempelhoftour.R;
import com.gyg.tempelhoftour.data.model.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thalespessoa on 3/29/18.
 */

public class ReviewsAdapter extends PagedListAdapter<Review, ReviewsAdapter.ReviewViewHolder> {

    OnSelectListener onSelectListener;

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    interface OnSelectListener {
        void onSelectEvent(Review event);
    }

    public ReviewsAdapter() {
        super(new DiffCallback<Review>() {
            @Override
            public boolean areItemsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
                return oldItem.getTitle() != null && oldItem.getTitle().equals(newItem.getTitle());
            }
        });
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position, List payloads) {
        onBindViewHolder(holder, position);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        final Review review = getItem(position);
        holder.title.setText(review.getTitle());
        holder.message.setText(review.getMessage());
        holder.rating.setText(String.valueOf(review.getRating()));
        holder.author.setText(review.getAuthor());
    }

    // ViewHolders

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView title;
        @BindView(R.id.tv_message)
        TextView message;
        @BindView(R.id.tv_rating)
        TextView rating;
        @BindView(R.id.tv_author)
        TextView author;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
