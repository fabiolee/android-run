package com.blogspot.carirunners.run.vo;

import com.blogspot.carirunners.run.db.BloggerTypeConverters;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.Nullable;

import java.util.List;

@Entity(primaryKeys = {"query"})
@TypeConverters(BloggerTypeConverters.class)
public class RepoSearchResult {
    public final String query;
    public final List<Integer> repoIds;
    public final int totalCount;
    @Nullable
    public final Integer next;

    public RepoSearchResult(String query, List<Integer> repoIds, int totalCount,
                            Integer next) {
        this.query = query;
        this.repoIds = repoIds;
        this.totalCount = totalCount;
        this.next = next;
    }
}
