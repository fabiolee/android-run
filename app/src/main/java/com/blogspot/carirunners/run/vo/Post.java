package com.blogspot.carirunners.run.vo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.TypeConverters;

import com.blogspot.carirunners.run.db.BloggerTypeConverters;

import java.util.List;

/**
 * Using id as primary key.
 */
@Entity(primaryKeys = "id")
@TypeConverters(BloggerTypeConverters.class)
public class Post {
    public static final String UNKNOWN_ID = "-1";
    public final String id;
    public final String published;
    public final String updated;
    public final String url;
    public final String title;
    public final String content;
    public final List<String> labels;

    public Post(String id, String published, String updated, String url, String title,
                String content, List<String> labels) {
        this.id = id;
        this.published = published;
        this.updated = updated;
        this.url = url;
        this.title = title;
        this.content = content;
        this.labels = labels;
    }
}
