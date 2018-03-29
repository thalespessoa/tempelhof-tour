package com.gyg.tempelhoftour.data.model;

import java.util.List;

/**
 * Created by thalespessoa on 3/29/18.
 */

public class ReviewsResponse {

    boolean status;
    List<Review> data;

    public List<Review> getData() {
        return data;
    }

    public void setData(List<Review> data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
