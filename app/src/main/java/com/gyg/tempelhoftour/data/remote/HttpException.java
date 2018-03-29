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
    public static int ERROR_UNKNOWN = 101;

    private int mCode;
    private String mMessage;

    public String getMessage(Context context) {
        if(mMessage != null) {
            return mMessage;
        } else if(mCode == ERROR_SERVER) {
            return context.getResources().getString(R.string.error_server);
        } else {
            return context.getResources().getString(R.string.error_unknown);
        }
    }

    public HttpException(int code) {
        mCode = code;
    }

    public HttpException(Response response) {

        Map<String, Object> body;

        try {
            String src = response.errorBody().string();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            body = mapper.readValue(src, HashMap.class);
            mMessage = body.get("detail").toString();
            mCode = Integer.valueOf(body.get("status").toString());
        } catch (IOException e) {
            body = null;
            mCode = ERROR_UNKNOWN;
        } catch (Exception e) {
            mCode = ERROR_UNKNOWN;
        }

    }
}
