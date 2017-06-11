package com.blogspot.carirunners.run.repository;

import com.blogspot.carirunners.run.api.ApiResponse;
import com.blogspot.carirunners.run.api.GithubService;
import com.blogspot.carirunners.run.api.RepoSearchResponse;
import com.blogspot.carirunners.run.db.GithubDb;
import com.blogspot.carirunners.run.vo.RepoSearchResult;
import com.blogspot.carirunners.run.vo.Resource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * A task that reads the search result in the database and fetches the next page, if it has one.
 */
public class FetchNextSearchPageTask implements Runnable {
    private final MutableLiveData<Resource<Boolean>> liveData = new MutableLiveData<>();
    private final String query;
    private final GithubService githubService;
    private final GithubDb db;

    FetchNextSearchPageTask(String query, GithubService githubService, GithubDb db) {
        this.query = query;
        this.githubService = githubService;
        this.db = db;
    }

    @Override
    public void run() {
        RepoSearchResult current = db.repoDao().findSearchResult(query);
        if (current == null) {
            liveData.postValue(null);
            return;
        }
        final Integer nextPage = current.next;
        if (nextPage == null) {
            liveData.postValue(Resource.success(false));
            return;
        }
        try {
            Response<RepoSearchResponse> response = githubService
                    .searchRepos(query, nextPage).execute();
            ApiResponse<RepoSearchResponse> apiResponse = new ApiResponse<>(response);
            if (apiResponse.isSuccessful()) {
                // we merge all repo ids into 1 list so that it is easier to fetch the result list.
                List<Integer> ids = new ArrayList<>();
                ids.addAll(current.repoIds);
                //noinspection ConstantConditions
                ids.addAll(apiResponse.body.getRepoIds());
                RepoSearchResult merged = new RepoSearchResult(query, ids,
                        apiResponse.body.getTotal(), apiResponse.getNextPage());
                try {
                    db.beginTransaction();
                    db.repoDao().insert(merged);
                    db.repoDao().insertRepos(apiResponse.body.getItems());
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                liveData.postValue(Resource.success(apiResponse.getNextPage() != null));
            } else {
                liveData.postValue(Resource.error(apiResponse.errorMessage, true));
            }
        } catch (IOException e) {
            liveData.postValue(Resource.error(e.getMessage(), true));
        }
    }

    LiveData<Resource<Boolean>> getLiveData() {
        return liveData;
    }
}
