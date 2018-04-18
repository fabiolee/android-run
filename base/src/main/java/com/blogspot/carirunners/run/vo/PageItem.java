package com.blogspot.carirunners.run.vo;

/**
 * @author fabiolee
 */
public class PageItem {
    public final String title;
    public final String urlPath;
    public boolean favorite;

    public PageItem(String title, String urlPath, boolean favorite) {
        this.title = title;
        this.urlPath = urlPath;
        this.favorite = favorite;
    }
}
