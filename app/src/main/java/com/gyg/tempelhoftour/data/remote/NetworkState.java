package com.gyg.tempelhoftour.data.remote;

/**
 * Created by thalespessoa on 2/23/18.
 */

public class NetworkState {
    public enum Status {
        RUNNING,
        SUCCESS,
        FAILED
    }

    private Status mStatus;
    private HttpException mError;

    public NetworkState(Status status, HttpException error) {
        mStatus = status;
        mError = error;
    }

    public Status getStatus() {
        return mStatus;
    }

    public HttpException getError() {
        return mError;
    }
}
