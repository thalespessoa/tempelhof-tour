package com.gyg.tempelhoftour.data.remote;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyg.tempelhoftour.BuildConfig;
import com.gyg.tempelhoftour.app.AppConfig;
import com.gyg.tempelhoftour.data.model.Review;
import com.gyg.tempelhoftour.data.model.ServerResponse;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Class responsible for the communication with server
 * All remote data access comes from here.
 * <p>
 * Created by thalespessoa on 2/19/18.
 */

public class NetworkApi {

    private Retrofit mRetrofit;
    private ReviewsApi mReviewsApi;

    public NetworkApi() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor());

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:60.0) Gecko/20100101 Firefox/60.0")
                            .build();

                    return chain.proceed(request);
                }
            });
        }

        OkHttpClient client = clientBuilder.build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.API_URL)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();

        mReviewsApi = mRetrofit.create(ReviewsApi.class);
    }

    public ReviewsApi getReviewsApi() {
        return mReviewsApi;
    }

    public interface ReviewsApi {
        @GET(AppConfig.GET_REVIEWS)
        Call<ServerResponse<List<Review>>> fetch(@Query("page") int page, @Query("count") int count);

        @POST(AppConfig.POST_REVIEW)
        Call<ServerResponse<Review>> save(@Body Review pendingReview);
    }
}
