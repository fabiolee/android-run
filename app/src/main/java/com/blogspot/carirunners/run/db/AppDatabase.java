package com.blogspot.carirunners.run.db;


import com.blogspot.carirunners.run.vo.Contributor;
import com.blogspot.carirunners.run.vo.Post;
import com.blogspot.carirunners.run.vo.Repo;
import com.blogspot.carirunners.run.vo.RepoSearchResult;
import com.blogspot.carirunners.run.vo.User;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Main database description.
 */
@Database(entities = {
        Post.class,
        User.class,
        Repo.class,
        Contributor.class,
        RepoSearchResult.class
}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PostDao postDao();

    abstract public UserDao userDao();

    abstract public RepoDao repoDao();
}
