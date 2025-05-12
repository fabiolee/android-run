package com.blogspot.carirunners.run.ui.favorite;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.blogspot.carirunners.run.repository.FavoriteRepository;
import com.blogspot.carirunners.run.vo.Favorite;
import com.blogspot.carirunners.run.vo.Resource;

import java.util.List;

import javax.inject.Inject;

public class FavoriteViewModel extends ViewModel {
    private final FavoriteRepository repository;
    private LiveData<Resource<List<Favorite>>> favoriteList;

    @SuppressWarnings("unchecked")
    @Inject
    public FavoriteViewModel(FavoriteRepository repository) {
        this.repository = repository;
        this.favoriteList = repository.load();
    }

    LiveData<Resource<List<Favorite>>> getFavoriteList() {
        return favoriteList;
    }

    void retry() {
        if (this.favoriteList.getValue() == null) {
            this.favoriteList = repository.load();
        }
    }

    void toggleFavorite(boolean isFavorited, Favorite favorite) {
        if (isFavorited) {
            repository.deleteByPath(favorite.path);
        } else {
            repository.insert(favorite);
        }
    }
}
