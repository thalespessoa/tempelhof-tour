package com.gyg.tempelhoftour.data.model;

/**
 * Created by thalespessoa on 3/29/18.
 */

public class ServerResponse<T> {

    boolean status;
    String message;
    T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
