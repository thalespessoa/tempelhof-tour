package com.gyg.tempelhoftour.data.db;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.gyg.tempelhoftour.data.model.Review;

import java.util.List;

/**
 * Created by thalespessoa on 3/29/18.
 */
@Dao
public interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Review> reviews);

    @Query("SELECT * FROM reviews")
    DataSource.Factory<Integer, Review> loadAllReviews();
}
