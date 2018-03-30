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
import android.widget.Toast;

import com.gyg.tempelhoftour.R;
import com.gyg.tempelhoftour.app.ApplicationController;
import com.gyg.tempelhoftour.app.ViewModelFactory;
import com.gyg.tempelhoftour.data.model.Review;
import com.gyg.tempelhoftour.data.remote.NetworkState;

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

        configViewModel();
    }

    // ---------------------------------------------------------------------------------------------
    // ViewModel observers
    // ---------------------------------------------------------------------------------------------

    private void configViewModel() {
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

        mReviewsViewModel.getReviewsStateLiveData().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                if(networkState.getStatus() != NetworkState.Status.RUNNING) {
                    mSwipeRefresh.setRefreshing(false);
                }
                if(networkState.getStatus() == NetworkState.Status.FAILED) {
                    Toast.makeText(ReviewsActivity.this,
                            networkState.getError().getMessage(ReviewsActivity.this),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // ---------------------------------------------------------------------------------------------
    // Actions
    // ---------------------------------------------------------------------------------------------

    @Override
    public void onRefresh() {
        mReviewsViewModel.refresh();
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
    public void onClickDelete(Review pendingReview) {
        mReviewsViewModel.deletePendingReview(pendingReview);
    }

    @Override
    public void onClickRetry(Review pendingReview) {
        Toast.makeText(this, "RETRY NOT IMPLEMENTED", Toast.LENGTH_SHORT).show();
    }
}
