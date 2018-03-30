package com.gyg.tempelhoftour.data;

import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;

import com.gyg.tempelhoftour.data.db.LocalDatabase;
import com.gyg.tempelhoftour.data.model.Review;
import com.gyg.tempelhoftour.data.model.ServerResponse;
import com.gyg.tempelhoftour.data.remote.NetworkApi;
import com.gyg.tempelhoftour.data.remote.NetworkState;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepositoryTest {

    NetworkApi.ReviewsApi mReviewsApiMocked;
    NetworkApi mNetworkApiMocked;
    DataRepository mDataRepository;
    int counter = 0;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mReviewsApiMocked = Mockito.mock(NetworkApi.ReviewsApi.class);
        mNetworkApiMocked = Mockito.mock(NetworkApi.class);

        Context context = InstrumentationRegistry.getTargetContext();
        LocalDatabase localDatabase = Room.inMemoryDatabaseBuilder(context, LocalDatabase.class).allowMainThreadQueries().build();
        mDataRepository = new DataRepository(mNetworkApiMocked, localDatabase, context);
    }

    @Test
    @UiThreadTest
    public void testSaveReviewSuccess() throws Exception {

        final Review review = createReview("100", "Title 100");

        final ServerResponse<Review> serverResponse = new ServerResponse<>();
        serverResponse.setStatus(true);
        serverResponse.setData(review);

        final Call<ServerResponse<Review>> mockedReview = Mockito.mock(Call.class);
        Mockito.when(mReviewsApiMocked.save(review)).thenReturn(mockedReview);
        Mockito.when(mNetworkApiMocked.getReviewsApi()).thenReturn(mReviewsApiMocked);

        Mockito.doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Callback<ServerResponse<Review>> callback = invocation.getArgument(0);
                callback.onResponse(mockedReview, Response.success(serverResponse));
                return null;
            }
        }).when(mockedReview).enqueue(ArgumentMatchers.any(Callback.class));

        checkNetworkState(NetworkState.Status.SUCCESS);
        mDataRepository.saveReviewRemote(review);
    }

    private void checkNetworkState(final NetworkState.Status finalState) {
        counter = 0;
        mDataRepository.getNetworkStateLiveData().observeForever(new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                if (counter == 0) {
                    Assert.assertEquals(NetworkState.Status.RUNNING, networkState.getStatus());
                } else if (counter == 1) {
                    Assert.assertEquals(finalState, networkState.getStatus());
                } else {
                    Assert.fail();
                }
                counter++;
            }
        });
    }

    private Review createReview(String id, String title) {
        Review review = new Review();
        review.setId(id);
        review.setTitle(title);
        return review;
    }
}
