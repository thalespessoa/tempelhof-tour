package com.gyg.tempelhoftour.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gyg.tempelhoftour.app.AppConfig;

import java.util.Date;

/**
 * Created by thalespessoa on 3/29/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(tableName = "pending_reviews")
public class PendingReview {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @JsonProperty("review_id")
    private int id;
    protected double rating;
    protected String title;
    protected String message;
    protected String author;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.API_DATE_FORMAT, timezone = "GMT")
    protected Date date;
    @JsonIgnore
    private boolean pending = true;

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
