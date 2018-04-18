package com.blogspot.carirunners.run.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.blogspot.carirunners.run.vo.Resource;

/**
 * @author fabiolee
 */
public abstract class DatabaseBoundResource<ResultType> {
    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    DatabaseBoundResource() {
        result.setValue(Resource.loading(null));
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(dbSource, data -> result.setValue(Resource.success(data)));
    }

    public LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }

    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();
}
