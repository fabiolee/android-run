package com.blogspot.carirunners.run.api;


import android.support.annotation.NonNull;

import com.blogspot.carirunners.run.vo.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO to hold post search responses. This is different from the Entity in the database because
 * we are keeping a search result in 1 row and denormalizing list of results into a single column.
 */
public class PostSearchResponse {
    private String nextPageToken;
    private String prevPageToken;
    private List<Post> items;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public String getPrevPageToken() {
        return prevPageToken;
    }

    public void setPrevPageToken(String prevPageToken) {
        this.prevPageToken = prevPageToken;
    }

    public List<Post> getItems() {
        return items;
    }

    public void setItems(List<Post> items) {
        this.items = items;
    }

    @NonNull
    public List<String> getPostIds() {
        List<String> postIds = new ArrayList<>();
        for (Post post : items) {
            postIds.add(post.id);
        }
        return postIds;
    }
}
