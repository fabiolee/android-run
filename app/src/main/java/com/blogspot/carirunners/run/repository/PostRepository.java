package com.blogspot.carirunners.run.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blogspot.carirunners.run.AppExecutors;
import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.api.ApiResponse;
import com.blogspot.carirunners.run.api.BloggerService;
import com.blogspot.carirunners.run.api.PostSearchResponse;
import com.blogspot.carirunners.run.db.AppDatabase;
import com.blogspot.carirunners.run.db.PostDao;
import com.blogspot.carirunners.run.util.RateLimiter;
import com.blogspot.carirunners.run.vo.Post;
import com.blogspot.carirunners.run.vo.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles Post objects.
 */
@Singleton
public class PostRepository {
    private final AppExecutors appExecutors;
    private final AppDatabase db;
    private final PostDao postDao;
    private final BloggerService bloggerService;
    private RateLimiter<String> postListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);
    private final String emptyMsg;

    @Inject
    PostRepository(Context context, AppExecutors appExecutors, AppDatabase db, PostDao postDao,
                   BloggerService bloggerService) {
        this.appExecutors = appExecutors;
        this.db = db;
        this.postDao = postDao;
        this.bloggerService = bloggerService;
        this.emptyMsg = context.getString(R.string.empty_message);
    }

    public LiveData<Resource<Post>> load(String id) {
        return new NetworkBoundResource<Post, Post>(appExecutors, emptyMsg) {
            @Override
            protected void saveCallResult(@NonNull Post item) {
                postDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable Post data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<Post> loadFromDb() {
                return postDao.load(id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Post>> createCall() {
                return bloggerService.getPost(id);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Post>> loadByPath(String path) {
        return new NetworkBoundResource<Post, Post>(appExecutors, emptyMsg) {
            @Override
            protected void saveCallResult(@NonNull Post item) {
                postDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable Post data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<Post> loadFromDb() {
                return postDao.loadByPath("%" + path);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Post>> createCall() {
                return bloggerService.getPostByPath(path);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<Post>>> search(String query) {
        return new NetworkBoundResource<List<Post>, PostSearchResponse>(appExecutors, emptyMsg) {
            @Override
            protected void saveCallResult(@NonNull PostSearchResponse item) {
                db.beginTransaction();
                try {
                    postDao.delete();
                    postDao.insert(item.getItems());
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Post> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Post>> loadFromDb() {
                return postDao.load();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PostSearchResponse>> createCall() {
                return bloggerService.searchPosts(query);
            }

            @Override
            protected PostSearchResponse processResponse(ApiResponse<PostSearchResponse> response) {
                PostSearchResponse body = response.body;
                if (body != null) {
                    //body.setNextPageToken(String.valueOf(response.getNextPage()));
                }
                return body;
            }
        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> searchNextPage(String query) {
        return new MutableLiveData<>();
    }
}
