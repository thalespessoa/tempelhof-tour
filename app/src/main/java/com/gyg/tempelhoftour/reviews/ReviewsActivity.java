package com.gyg.tempelhoftour.reviews;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gyg.tempelhoftour.R;
import com.gyg.tempelhoftour.app.ApplicationController;
import com.gyg.tempelhoftour.app.ViewModelFactory;
import com.gyg.tempelhoftour.data.model.Review;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewsActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        ReviewsAdapter.OnInteractionListener {

    @BindView(R.id.rv_reviews)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.sr_reviews)
    protected SwipeRefreshLayout mSwipeRefresh;

    private ReviewsAdapter mReviewsAdapter;
    private ReviewsViewModel mReviewsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);

        mReviewsAdapter = new ReviewsAdapter();
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mReviewsAdapter);

        mReviewsAdapter.setOnInteractionListener(this);

        mSwipeRefresh.setOnRefreshListener(this);

        mReviewsViewModel = ViewModelProviders.of(ReviewsActivity.this,
                new ViewModelFactory((ApplicationController) getApplication()))
                .get(ReviewsViewModel.class);

        mReviewsViewModel.getReviewsLiveData().observe(ReviewsActivity.this, new Observer<PagedList<Review>>() {
            @Override
            public void onChanged(@Nullable PagedList<Review> reviews) {
                mReviewsAdapter.setList(reviews);
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        mReviewsViewModel.refresh();
        mSwipeRefresh.setRefreshing(false);
    }

    @OnClick(R.id.bt_add_review)
    public void onClickAddReview() {
        new AddReviewFragment()
                .setOnReviewFinish(new AddReviewFragment.OnReviewFinish() {
                    @Override
                    public void onReviewFinish(Review pendingReview) {
                        mReviewsViewModel.saveReview(pendingReview);
                        mRecyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.smoothScrollToPosition(0);
                            }
                        }, 500);
                    }
                })
                .show(getSupportFragmentManager(), "add");
    }

    @Override
    public void onClickDelete(Review review) {
        mReviewsViewModel.deletePendingReview(review);
    }

    @Override
    public void onClickRetry(Review review) {

    }
}
