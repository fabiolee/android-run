package com.blogspot.carirunners.run.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.blogspot.carirunners.run.vo.Post;

import java.util.List;

/**
 * Interface for database access for Post related operations.
 */
@Dao
public interface PostDao {
    @Query("DELETE FROM Post")
    void delete();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Post> posts);

    @Query("SELECT * FROM Post")
    LiveData<List<Post>> load();

    @Query("SELECT * FROM Post WHERE id = :id")
    LiveData<Post> load(String id);

    @Query("SELECT * FROM Post WHERE url LIKE :path")
    LiveData<Post> loadByPath(String path);
}
