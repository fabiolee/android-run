package com.blogspot.carirunners.run.api;

import android.arch.lifecycle.LiveData;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * REST API access points
 */
public interface BloggerService {
    String API_KEY = "AIzaSyDGktXyn4O-hKkVkVFna7NQOrEOxfcwqTA";
    String BLOG_ID = "9027509069015616506";

    @GET("posts/search?key=" + API_KEY)
    LiveData<ApiResponse<PostSearchResponse>> searchPosts(@Query("q") String query);
}
