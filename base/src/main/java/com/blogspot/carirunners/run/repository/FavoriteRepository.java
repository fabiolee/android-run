package com.blogspot.carirunners.run.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.blogspot.carirunners.run.AppExecutors;
import com.blogspot.carirunners.run.db.FavoriteDao;
import com.blogspot.carirunners.run.vo.Favorite;
import com.blogspot.carirunners.run.vo.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles Favorite objects.
 */
@Singleton
public class FavoriteRepository {
    private final AppExecutors appExecutors;
    private final FavoriteDao favoriteDao;

    @Inject
    FavoriteRepository(AppExecutors appExecutors, FavoriteDao favoriteDao) {
        this.appExecutors = appExecutors;
        this.favoriteDao = favoriteDao;
    }

    public void deleteByPath(String path) {
        appExecutors.diskIO().execute(() -> favoriteDao.deleteByPath(path));
    }

    public void insert(Favorite favorite) {
        appExecutors.diskIO().execute(() -> favoriteDao.insert(favorite));
    }

    public LiveData<Resource<List<Favorite>>> load() {
        return new DatabaseBoundResource<List<Favorite>>() {
            @NonNull
            @Override
            protected LiveData<List<Favorite>> loadFromDb() {
                return favoriteDao.load();
            }
        }.asLiveData();
    }
}
