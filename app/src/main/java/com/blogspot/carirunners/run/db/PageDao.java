package com.blogspot.carirunners.run.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.blogspot.carirunners.run.vo.Page;

/**
 * Interface for database access for Page related operations.
 */
@Dao
public interface PageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Page page);

    @Query("SELECT * FROM Page WHERE id = :id")
    LiveData<Page> load(String id);

    @Update
    void update(Page page);
}
