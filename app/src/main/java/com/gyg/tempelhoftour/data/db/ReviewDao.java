package com.gyg.tempelhoftour.data.db;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.gyg.tempelhoftour.data.model.Review;

import java.util.Date;
import java.util.List;

/**
 * Created by thalespessoa on 3/29/18.
 */
@Dao
public interface ReviewDao {

    @Query("SELECT * FROM reviews ORDER BY date desc")
    DataSource.Factory<Integer, Review> loadAllReviews();


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Review> reviews);

    @Insert
    void insert(Review review);


    @Delete
    void delete(Review review);

    @Query("DELETE FROM reviews WHERE date < :date")
    void deleteOlder(Date date);
}
