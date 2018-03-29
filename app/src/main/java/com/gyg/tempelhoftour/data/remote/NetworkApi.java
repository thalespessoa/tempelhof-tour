package com.gyg.tempelhoftour.data.remote;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyg.tempelhoftour.app.AppConfig;
import com.gyg.tempelhoftour.data.model.ReviewsResponse;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Class responsible for the communication with server
 * All remote data access comes from here.
 *
 * Created by thalespessoa on 2/19/18.
 */

public class NetworkApi {

    private Retrofit mRetrofit;
    private ReviewsApi mReviewsApi;

    public NetworkApi() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original.newBuilder().build();
                        return chain.proceed(request);
                    }
                });
        clientBuilder.addNetworkInterceptor(new StethoInterceptor());
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
        @GET("reviews.json?count=15&page=0&type=&sortBy=date_of_review&direction=DESC")
        Call<ReviewsResponse> fetch();
    }
}
