package com.blogspot.carirunners.run.vo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;

/**
 * Using id as primary key.
 */
@Entity(primaryKeys = "id")
public class Post {
    public static final String UNKNOWN_ID = "-1";
    public final String id;
    public final String published;
    public final String updated;
    public final String url;
    public final String title;
    public final String content;
    @Embedded(prefix = "author_")
    public final Author author;
    @Embedded(prefix = "replies_")
    public final Replies replies;

    public Post(String id, String published, String updated, String url, String title, String content,
                Author author, Replies replies) {
        this.id = id;
        this.published = published;
        this.updated = updated;
        this.url = url;
        this.title = title;
        this.content = content;
        this.author = author;
        this.replies = replies;
    }

    public static class Author {
        public final String id;
        public final String displayName;
        @Embedded(prefix = "image_")
        public final Image image;

        public Author(String id, String displayName, Image image) {
            this.id = id;
            this.displayName = displayName;
            this.image = image;
        }
    }

    public static class Image {
        public final String url;

        public Image(String url) {
            this.url = url;
        }
    }

    public static class Replies {
        public final String totalItems;

        public Replies(String totalItems) {
            this.totalItems = totalItems;
        }
    }
}
