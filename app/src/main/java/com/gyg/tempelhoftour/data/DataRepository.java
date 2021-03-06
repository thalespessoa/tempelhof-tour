package com.gyg.tempelhoftour.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.gyg.tempelhoftour.app.AppConfig;
import com.gyg.tempelhoftour.data.db.LocalDatabase;
import com.gyg.tempelhoftour.data.model.Review;
import com.gyg.tempelhoftour.data.model.ServerResponse;
import com.gyg.tempelhoftour.data.remote.HttpException;
import com.gyg.tempelhoftour.data.remote.NetworkApi;
import com.gyg.tempelhoftour.data.remote.NetworkState;
import com.gyg.tempelhoftour.data.remote.ServiceCallback;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by thalespessoa on 3/29/18.
 */

public class DataRepository {

    private NetworkApi mNetworkApi;
    private LocalDatabase mLocalDatabase;
    private LiveData<PagedList<Review>> mReviewsLiveData;
    private SharedPreferences mSharedPref;
    private MutableLiveData<NetworkState> mNetworkStateLiveData;

    private Executor mDiskIO;

    public DataRepository(NetworkApi networkApi, LocalDatabase localDatabase, Context context) {
        mNetworkApi = networkApi;
        mLocalDatabase = localDatabase;
        mSharedPref = context.getSharedPreferences("gygPrefs", Context.MODE_PRIVATE);

        mDiskIO = Executors.newSingleThreadExecutor();

        LivePagedListBuilder<Integer, Review> livePagedListBuilder = new LivePagedListBuilder<>(
                mLocalDatabase.reviewDao().loadAllReviews(),
                AppConfig.DB_PAGE_SIZE)
                .setBackgroundThreadExecutor(Executors.newFixedThreadPool(5))
                .setBoundaryCallback(new PagedList.BoundaryCallback<Review>() {
                    @Override
                    public void onItemAtEndLoaded(@NonNull Review itemAtEnd) {
                        fetchReviewsFromServer(getLastPageLoaded());
                    }

                    @Override
                    public void onZeroItemsLoaded() {
                        fetchReviewsFromServer(0);
                    }
                });
        mReviewsLiveData = livePagedListBuilder.build();
        mNetworkStateLiveData = new MutableLiveData<>();
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

    /**
     * @return NetworkState of the review list
     */
    public MutableLiveData<NetworkState> getNetworkStateLiveData() {
        return mNetworkStateLiveData;
    }

    /**
     * Request the list of reviews from server and update local database
     */
    public void refresh() {
        fetchReviewsFromServer(0);
    }

    /**
     * Save pending review in the local data base, and send this review to server
     * @param pendingReview
     */
    public void saveReviewLocal(final Review pendingReview) {
        mDiskIO.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDatabase.reviewDao().insert(pendingReview);
            }
        });
        saveReviewRemote(pendingReview);
    }

    /**
     * Send a review to server
     * @param pendingReview
     */
    public void saveReviewRemote(final Review pendingReview) {
        mNetworkStateLiveData.setValue(new NetworkState(NetworkState.Status.RUNNING, null));
        mNetworkApi.getReviewsApi()
                .save(pendingReview)
                .enqueue(new ServiceCallback<ServerResponse<Review>>() {
                    @Override
                    public void onSuccess(final ServerResponse<Review> reponse) {
                        mNetworkStateLiveData.setValue(new NetworkState(NetworkState.Status.SUCCESS, null));
                        mDiskIO.execute(new Runnable() {
                            @Override
                            public void run() {
                                mLocalDatabase.reviewDao().delete(pendingReview);
                                mLocalDatabase.reviewDao().insert(reponse.getData());
                            }
                        });
                    }

                    @Override
                    public void onError(HttpException exception) {
                        mNetworkStateLiveData.setValue(new NetworkState(NetworkState.Status.FAILED, exception));
                    }
                });
    }

    /**
     * Delete a pending review from data base
     * @param review
     */
    public void deletePendingReview(final Review review) {
        mDiskIO.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDatabase.reviewDao().delete(review);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------
    // Private
    // ---------------------------------------------------------------------------------------------

    private void fetchReviewsFromServer(final int page) {
        mNetworkStateLiveData.setValue(new NetworkState(NetworkState.Status.RUNNING, null));
        mNetworkApi.getReviewsApi()
                .fetch(page, AppConfig.API_PAGE_SIZE)
                .enqueue(new ServiceCallback<ServerResponse<List<Review>>>() {
                    @Override
                    public void onSuccess(final ServerResponse<List<Review>> response) {
                        mNetworkStateLiveData.setValue(new NetworkState(NetworkState.Status.SUCCESS, null));
                        if (response.isStatus()) {
                            mDiskIO.execute(new Runnable() {
                                @Override
                                public void run() {
                                    List<Review> reviews = response.getData();
                                    mLocalDatabase.reviewDao().deleteOlder(reviews.get(reviews.size()-1).getDate());
                                    mLocalDatabase.reviewDao().insertAll(reviews);
                                }
                            });

                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putInt("nextPage", page + 1);
                            editor.apply();
                        }
                    }

                    @Override
                    public void onError(HttpException exception) {
                        mNetworkStateLiveData.setValue(new NetworkState(NetworkState.Status.FAILED, exception));
                    }
                });
    }

    private int getLastPageLoaded() {
        return mSharedPref.getInt("nextPage", 0);
    }
}
