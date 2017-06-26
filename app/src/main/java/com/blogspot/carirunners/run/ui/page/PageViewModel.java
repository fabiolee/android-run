package com.blogspot.carirunners.run.ui.page;

import com.blogspot.carirunners.run.repository.PageRepository;
import com.blogspot.carirunners.run.util.AbsentLiveData;
import com.blogspot.carirunners.run.util.Objects;
import com.blogspot.carirunners.run.vo.Page;
import com.blogspot.carirunners.run.vo.Resource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import javax.inject.Inject;

public class PageViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<String> id = new MutableLiveData<>();
    private final LiveData<Resource<Page>> page;

    @SuppressWarnings("unchecked")
    @Inject
    public PageViewModel(PageRepository repository) {
        page = Transformations.switchMap(id, id -> {
            if (id == null) {
                return AbsentLiveData.create();
            } else {
                return repository.load(id);
            }
        });
    }

    void setId(String id) {
        if (Objects.equals(this.id.getValue(), id)) {
            return;
        }
        this.id.setValue(id);
    }

    LiveData<Resource<Page>> getPage() {
        return page;
    }

    void retry() {
        if (this.id.getValue() != null) {
            this.id.setValue(this.id.getValue());
        }
    }
}
