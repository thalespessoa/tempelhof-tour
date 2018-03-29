package com.gyg.tempelhoftour.data.remote;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Callback to handle retrofit results
 * 
 * Created by thalespessoa on 2/20/18.
 */

public abstract class ServiceCallback<T> implements Callback<T> {

    public abstract void onSuccess(T response);
    public abstract void onError(HttpException exception);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response.body());
        } else {
            onError(new HttpException(response));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onError(new HttpException(HttpException.ERROR_SERVER));
    }
}
