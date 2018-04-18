package com.blogspot.carirunners.run.ui.page;

import com.blogspot.carirunners.run.repository.FavoriteRepository;
import com.blogspot.carirunners.run.repository.PageRepository;
import com.blogspot.carirunners.run.util.AbsentLiveData;
import com.blogspot.carirunners.run.util.Objects;
import com.blogspot.carirunners.run.vo.Favorite;
import com.blogspot.carirunners.run.vo.PageItem;
import com.blogspot.carirunners.run.vo.Resource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import java.util.List;

import javax.inject.Inject;

public class PageViewModel extends ViewModel {
    private final FavoriteRepository favoriteRepository;
    @VisibleForTesting
    final MutableLiveData<String> id = new MutableLiveData<>();
    private final LiveData<Resource<List<PageItem>>> pageItemList;

    @SuppressWarnings("unchecked")
    @Inject
    public PageViewModel(FavoriteRepository favoriteRepository, PageRepository pageRepository) {
        this.favoriteRepository = favoriteRepository;
        this.pageItemList = Transformations.switchMap(id, id -> {
            if (id == null) {
                return AbsentLiveData.create();
            } else {
                return pageRepository.load(id);
            }
        });
    }

    void setId(String id) {
        if (Objects.equals(this.id.getValue(), id)) {
            return;
        }
        this.id.setValue(id);
    }

    LiveData<Resource<List<PageItem>>> getPageItemList() {
        return pageItemList;
    }

    void retry() {
        if (this.id.getValue() != null) {
            this.id.setValue(this.id.getValue());
        }
    }

    void toggleFavorite(boolean isFavorited, Favorite favorite) {
        if (isFavorited) {
            favoriteRepository.deleteByPath(favorite.path);
        } else {
            favoriteRepository.insert(favorite);
        }
    }
}
