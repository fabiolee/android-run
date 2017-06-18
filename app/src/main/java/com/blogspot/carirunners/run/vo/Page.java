package com.blogspot.carirunners.run.vo;

import android.arch.persistence.room.Entity;

/**
 * Using id as primary key.
 */
@Entity(primaryKeys = "id")
public class Page {
    public static final String UNKNOWN_ID = "-1";
    public final String id;
    public final String published;
    public final String updated;
    public final String url;
    public final String title;
    public final String content;

    public Page(String id, String published, String updated, String url, String title,
                String content) {
        this.id = id;
        this.published = published;
        this.updated = updated;
        this.url = url;
        this.title = title;
        this.content = content;
    }
}
