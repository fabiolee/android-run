package com.blogspot.carirunners.run.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.blogspot.carirunners.run.AppExecutors;
import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.api.ApiResponse;
import com.blogspot.carirunners.run.api.BloggerService;
import com.blogspot.carirunners.run.db.FavoriteDao;
import com.blogspot.carirunners.run.db.PageDao;
import com.blogspot.carirunners.run.vo.Favorite;
import com.blogspot.carirunners.run.vo.Page;
import com.blogspot.carirunners.run.vo.PageItem;
import com.blogspot.carirunners.run.vo.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles Page objects.
 */
@Singleton
public class PageRepository {
    private final AppExecutors appExecutors;
    private final FavoriteDao favoriteDao;
    private final PageDao pageDao;
    private final BloggerService bloggerService;
    private final String emptyMsg;

    @Inject
    PageRepository(Context context, AppExecutors appExecutors, FavoriteDao favoriteDao,
                   PageDao pageDao, BloggerService bloggerService) {
        this.appExecutors = appExecutors;
        this.favoriteDao = favoriteDao;
        this.pageDao = pageDao;
        this.bloggerService = bloggerService;
        this.emptyMsg = context.getString(R.string.empty_message);
    }

    public LiveData<Resource<List<PageItem>>> load(String id) {
        return new NetworkBoundResource<List<PageItem>, Page>(appExecutors, emptyMsg) {
            @Override
            protected void saveCallResult(@NonNull Page item) {
                pageDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<PageItem> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<PageItem>> loadFromDb() {
                MediatorLiveData<List<PageItem>> result = new MediatorLiveData<>();
                result.addSource(pageDao.load(id), page -> {
                    List<PageItem> pageItems;
                    if (page == null) {
                        pageItems = null;
                    } else {
                        Document document = Jsoup.parseBodyFragment(page.content);
                        Elements elements = document.select("ul li");
                        pageItems = new ArrayList<>();
                        String title;
                        String urlPath;
                        for (Element element : elements) {
                            title = element.text();
                            urlPath = Uri.parse(element.select("a").attr("href")).getPath();
                            pageItems.add(new PageItem(title, urlPath, false));
                        }
                    }
                    if (pageItems == null || pageItems.isEmpty()) {
                        result.setValue(null);
                    } else {
                        result.addSource(favoriteDao.load(), favoriteList -> {
                            Map<String, String> favoriteMap = new ArrayMap<>();
                            if (favoriteList != null) {
                                for (Favorite favorite : favoriteList) {
                                    favoriteMap.put(favorite.path, null);
                                }
                            }
                            List<PageItem> items = new ArrayList<>(pageItems.size());
                            boolean favorite;
                            for (PageItem item : pageItems) {
                                favorite = favoriteMap.containsKey(item.urlPath);
                                items.add(new PageItem(item.title, item.urlPath, favorite));
                            }
                            result.setValue(items);
                        });
                    }
                });
                return result;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Page>> createCall() {
                return bloggerService.getPage(id);
            }
        }.asLiveData();
    }
}
