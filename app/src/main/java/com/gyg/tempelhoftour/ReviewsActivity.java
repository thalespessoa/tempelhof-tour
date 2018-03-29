package com.gyg.tempelhoftour;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.gyg.tempelhoftour.app.ApplicationController;
import com.gyg.tempelhoftour.app.ViewModelFactory;
import com.gyg.tempelhoftour.data.model.Review;
import com.gyg.tempelhoftour.reviews.ReviewsAdapter;
import com.gyg.tempelhoftour.reviews.ReviewsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

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

        mSwipeRefresh.setOnRefreshListener(this);

        mReviewsViewModel = ViewModelProviders.of(ReviewsActivity.this,
                new ViewModelFactory((ApplicationController) getApplication()))
                .get(ReviewsViewModel.class);

        mReviewsViewModel.getReviewsLiveData().observe(ReviewsActivity.this, new Observer<PagedList<Review>>() {
            @Override
            public void onChanged(@Nullable PagedList<Review> reviews) {
                System.out.println("ReviewsActivity.onChanged: "+reviews.size());
                mReviewsAdapter.setList(reviews);
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        mReviewsViewModel.refresh();
    }
}
