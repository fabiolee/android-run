package com.blogspot.carirunners.run.vo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * @author fabiolee
 */
@Entity
public class Favorite {
    @PrimaryKey(autoGenerate = true)
    public int _id;
    public final String id;
    @NonNull
    public final String title;
    public final String path;

    public Favorite(String id, @NonNull String title, String path) {
        this.id = id;
        this.title = title;
        this.path = path;
    }
}
