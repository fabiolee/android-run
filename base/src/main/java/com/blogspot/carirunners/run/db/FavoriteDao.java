package com.blogspot.carirunners.run.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.blogspot.carirunners.run.vo.Favorite;

import java.util.List;

/**
 * Interface for database access for Favorite related operations.
 */
@Dao
public interface FavoriteDao {
    @Query("DELETE FROM Favorite WHERE path = :path")
    void deleteByPath(String path);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Favorite favorite);

    @Query("SELECT * FROM Favorite")
    LiveData<List<Favorite>> load();

    @Query("SELECT * FROM Favorite WHERE path = :path")
    LiveData<Favorite> loadByPath(String path);
}
