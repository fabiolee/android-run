package com.blogspot.carirunners.run.api;

import android.arch.lifecycle.LiveData;

import com.blogspot.carirunners.run.vo.Page;
import com.blogspot.carirunners.run.vo.Post;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * REST API access points
 */
public interface BloggerService {
    String API_KEY = "AIzaSyDGktXyn4O-hKkVkVFna7NQOrEOxfcwqTA";
    String BLOG_ID = "9027509069015616506";
    String PAGE_ID = "6337578441124076615";

    @GET("posts/search?key=" + API_KEY)
    LiveData<ApiResponse<PostSearchResponse>> searchPosts(@Query("q") String query);

    @GET("pages/{id}?key=" + API_KEY)
    LiveData<ApiResponse<Page>> getPage(@Path("id") String id);

    @GET("posts/{id}?key=" + API_KEY)
    LiveData<ApiResponse<Post>> getPost(@Path("id") String id);

    @GET("posts/bypath?key=" + API_KEY)
    LiveData<ApiResponse<Post>> getPostByPath(@Query("path") String path);
}
