package com.gyg.tempelhoftour.reviews;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import com.gyg.tempelhoftour.app.ApplicationComponent;
import com.gyg.tempelhoftour.data.DataRepository;
import com.gyg.tempelhoftour.data.model.Review;

import javax.inject.Inject;

/**
 * Created by thalespessoa on 3/29/18.
 */

public class ReviewsViewModel extends ViewModel implements ApplicationComponent.Injectable {
    @Inject
    DataRepository mDataRepository;

    private LiveData<PagedList<Review>> mReviewsLiveData;

    @Override
    public void inject(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
        mReviewsLiveData = mDataRepository.getReviewsLiveData();
    }

    public LiveData<PagedList<Review>> getReviewsLiveData() {
        return mReviewsLiveData;
    }

    public void refresh() {
        mDataRepository.refresh();
    }

    public void saveReview(Review review) {
        mDataRepository.saveReview(review);
    }

    public void deletePendingReview(Review review) {
        mDataRepository.deletePendingReview(review);
    }
}
