package com.blogspot.carirunners.run.db;


import com.blogspot.carirunners.run.vo.Contributor;
import com.blogspot.carirunners.run.vo.Repo;
import com.blogspot.carirunners.run.vo.RepoSearchResult;
import com.blogspot.carirunners.run.vo.User;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Main database description.
 */
@Database(entities = {User.class, Repo.class, Contributor.class,
        RepoSearchResult.class}, version = 3)
public abstract class GithubDb extends RoomDatabase {

    abstract public UserDao userDao();

    abstract public RepoDao repoDao();
}
