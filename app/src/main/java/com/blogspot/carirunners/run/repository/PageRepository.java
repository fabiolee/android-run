package com.blogspot.carirunners.run.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blogspot.carirunners.run.AppExecutors;
import com.blogspot.carirunners.run.api.ApiResponse;
import com.blogspot.carirunners.run.api.BloggerService;
import com.blogspot.carirunners.run.db.AppDatabase;
import com.blogspot.carirunners.run.db.PageDao;
import com.blogspot.carirunners.run.vo.Page;
import com.blogspot.carirunners.run.vo.Resource;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles Page objects.
 */
@Singleton
public class PageRepository {
    private final AppExecutors appExecutors;
    private final AppDatabase db;
    private final PageDao pageDao;
    private final BloggerService bloggerService;

    @Inject
    PageRepository(AppExecutors appExecutors, AppDatabase db, PageDao pageDao,
                   BloggerService bloggerService) {
        this.appExecutors = appExecutors;
        this.db = db;
        this.pageDao = pageDao;
        this.bloggerService = bloggerService;
    }

    public LiveData<Resource<Page>> loadPage(String id) {
        return new NetworkBoundResource<Page, Page>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Page item) {
                pageDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable Page data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<Page> loadFromDb() {
                return pageDao.load(id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Page>> createCall() {
                return bloggerService.getPage(id);
            }
        }.asLiveData();
    }
}
