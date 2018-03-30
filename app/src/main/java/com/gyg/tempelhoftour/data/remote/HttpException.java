package com.gyg.tempelhoftour.data.remote;

import android.content.Context;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyg.tempelhoftour.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;

/**
 * Custom class exception to abstract all types of exceptions and fails
 *
 * Created by thalespessoa on 1/17/18.
 */

public class HttpException {

    public static int ERROR_SERVER = 100;

    private int mCode;
    private String mMessage;

    public String getMessage(Context context) {
        if(mMessage != null) {
            return mMessage;
        } else if(mCode == ERROR_SERVER) {
            return context.getResources().getString(R.string.error_server);
        } else {
            return String.format("%d: %s", mCode, context.getResources().getString(R.string.error_unknown));
        }
    }

    public HttpException(int code) {
        mCode = code;
    }

    public HttpException(Response response) {
        mCode = response.code();
        if(!response.message().equals("")) {
            mMessage = String.format("%d: %s", response.message());
        }
    }
}
