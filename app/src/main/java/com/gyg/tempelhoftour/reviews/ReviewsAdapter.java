package com.gyg.tempelhoftour.reviews;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gyg.tempelhoftour.R;
import com.gyg.tempelhoftour.app.AppConfig;
import com.gyg.tempelhoftour.data.model.Review;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thalespessoa on 3/29/18.
 */

public class ReviewsAdapter extends PagedListAdapter<Review, ReviewsAdapter.ReviewViewHolder> {

    static SimpleDateFormat dateFormat = new SimpleDateFormat(AppConfig.APP_DATE_FORMAT);

    OnInteractionListener onInteractionListener;

    public void setOnInteractionListener(OnInteractionListener onInteractionListener) {
        this.onInteractionListener = onInteractionListener;
    }

    interface OnInteractionListener {
        void onClickDelete(Review review);
        void onClickRetry(Review review);
    }

    public ReviewsAdapter() {
        super(new DiffCallback<Review>() {
            @Override
            public boolean areItemsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
                return oldItem.getId().equals(newItem.getId());
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
        Context context = holder.view.getContext();
        holder.title.setText(review.getTitle());
        holder.message.setText(review.getMessage());
        holder.rating.setText(String.valueOf(review.getRating()));
        holder.author.setText(review.getAuthor());
        if(!review.isPending()) {
            holder.date.setText(dateFormat.format(review.getDate()));
            holder.error.setVisibility(View.GONE);
            holder.date.setVisibility(View.VISIBLE);
            holder.view.setBackgroundColor(context.getResources().getColor(R.color.colorBaseLight));
        } else {
            holder.date.setText(dateFormat.format(review.getDate()));
            holder.error.setVisibility(View.VISIBLE);
            holder.date.setVisibility(View.GONE);
            holder.view.setBackgroundColor(context.getResources().getColor(R.color.colorBase));

            holder.retryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onInteractionListener.onClickRetry(null);
                }
            });
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onInteractionListener.onClickDelete(review);
                }
            });
        }
    }

    // ViewHolders

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view)
        View view;
        @BindView(R.id.tv_title)
        TextView title;
        @BindView(R.id.tv_message)
        TextView message;
        @BindView(R.id.tv_rating)
        TextView rating;
        @BindView(R.id.tv_author)
        TextView author;
        @BindView(R.id.tv_date)
        TextView date;
        @BindView(R.id.bt_retry)
        Button retryButton;
        @BindView(R.id.bt_delete)
        Button deleteButton;
        @BindView(R.id.container_error)
        View error;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
