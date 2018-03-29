package com.gyg.tempelhoftour.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.gyg.tempelhoftour.app.AppConfig;
import com.gyg.tempelhoftour.data.db.LocalDatabase;
import com.gyg.tempelhoftour.data.model.Review;
import com.gyg.tempelhoftour.data.model.ReviewsResponse;
import com.gyg.tempelhoftour.data.remote.HttpException;
import com.gyg.tempelhoftour.data.remote.NetworkApi;
import com.gyg.tempelhoftour.data.remote.ServiceCallback;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by thalespessoa on 3/29/18.
 */

public class DataRepository {

    private NetworkApi mNetworkApi;
    private LocalDatabase mLocalDatabase;
    private Application mApplication;
    private LiveData<PagedList<Review>> mReviewsLiveData;
    private SharedPreferences mSharedPref;

    private Executor mDiskIO;

    public DataRepository(NetworkApi networkApi, LocalDatabase localDatabase, Application application) {
        mNetworkApi = networkApi;
        mLocalDatabase = localDatabase;
        mApplication = application;

        mDiskIO = Executors.newSingleThreadExecutor();

        LivePagedListBuilder<Integer, Review> livePagedListBuilder = new LivePagedListBuilder<>(
                mLocalDatabase.reviewDao().loadAllReviews(),
                AppConfig.DB_PAGE_SIZE)
                .setBackgroundThreadExecutor(Executors.newFixedThreadPool(5))
                .setBoundaryCallback(new PagedList.BoundaryCallback<Review>() {
                    @Override
                    public void onItemAtEndLoaded(@NonNull Review itemAtEnd) {
                        fetchEventsFromServer(getLastPageLoaded());
                    }

                    @Override
                    public void onZeroItemsLoaded() {
                        fetchEventsFromServer(0);
                    }
                });
        mReviewsLiveData = livePagedListBuilder.build();
    }

    // ---------------------------------------------------------------------------------------------
    // Public
    // ---------------------------------------------------------------------------------------------

    /**
     * Load all reviews in a paged list live data
     *
     * @return all reviews
     */
    public LiveData<PagedList<Review>> getReviewsLiveData() {
        return mReviewsLiveData;
    }

    public void refresh() {
        fetchEventsFromServer(0);
    }

    // ---------------------------------------------------------------------------------------------
    // Private
    // ---------------------------------------------------------------------------------------------

    private void fetchEventsFromServer(final int page) {

        mNetworkApi.getReviewsApi()
                .fetch(page)
                .enqueue(new ServiceCallback<ReviewsResponse>() {
                    @Override
                    public void onSuccess(final ReviewsResponse response) {
                        if(response.isStatus()) {
                            mDiskIO.execute(new Runnable() {
                                @Override
                                public void run() {
                                    mLocalDatabase.reviewDao().insertAll(response.getData());
                                }
                            });

                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putInt("nextPage", page+1);
                            editor.apply();
                        }
                    }

                    @Override
                    public void onError(HttpException exception) {
                        System.out.println("DataRepository.onError: ");
                    }
                });
    }

    private int getLastPageLoaded() {
        mSharedPref = mApplication.getSharedPreferences("gygPrefs", Context.MODE_PRIVATE);
        return mSharedPref.getInt("nextPage", 0);
    }
}
