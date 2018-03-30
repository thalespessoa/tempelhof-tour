package com.gyg.tempelhoftour.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.gyg.tempelhoftour.data.model.PendingReview;
import com.gyg.tempelhoftour.data.model.Review;

/**
 * Created by thalespessoa on 3/29/18.
 */


@Database(entities = {Review.class, PendingReview.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract ReviewDao reviewDao();
}
