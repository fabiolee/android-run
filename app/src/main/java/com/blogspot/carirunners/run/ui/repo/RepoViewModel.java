package com.blogspot.carirunners.run.ui.repo;

import com.blogspot.carirunners.run.repository.RepoRepository;
import com.blogspot.carirunners.run.util.AbsentLiveData;
import com.blogspot.carirunners.run.util.Objects;
import com.blogspot.carirunners.run.vo.Contributor;
import com.blogspot.carirunners.run.vo.Repo;
import com.blogspot.carirunners.run.vo.Resource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import java.util.List;

import javax.inject.Inject;

public class RepoViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<RepoId> repoId;
    private final LiveData<Resource<Repo>> repo;
    private final LiveData<Resource<List<Contributor>>> contributors;

    @Inject
    public RepoViewModel(RepoRepository repository) {
        this.repoId = new MutableLiveData<>();
        repo = Transformations.switchMap(repoId, input -> {
            if (input.isEmpty()) {
                return AbsentLiveData.create();
            }
            return repository.loadRepo(input.owner, input.name);
        });
        contributors = Transformations.switchMap(repoId, input -> {
            if (input.isEmpty()) {
                return AbsentLiveData.create();
            } else {
                return repository.loadContributors(input.owner, input.name);
            }

        });
    }

    public LiveData<Resource<Repo>> getRepo() {
        return repo;
    }

    public LiveData<Resource<List<Contributor>>> getContributors() {
        return contributors;
    }

    public void retry() {
        RepoId current = repoId.getValue();
        if (current != null && !current.isEmpty()) {
            repoId.setValue(current);
        }
    }

    void setId(String owner, String name) {
        RepoId update = new RepoId(owner, name);
        if (Objects.equals(repoId.getValue(), update)) {
            return;
        }
        repoId.setValue(update);
    }

    @VisibleForTesting
    static class RepoId {
        public final String owner;
        public final String name;

        RepoId(String owner, String name) {
            this.owner = owner == null ? null : owner.trim();
            this.name = name == null ? null : name.trim();
        }

        boolean isEmpty() {
            return owner == null || name == null || owner.length() == 0 || name.length() == 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            RepoId repoId = (RepoId) o;

            if (owner != null ? !owner.equals(repoId.owner) : repoId.owner != null) {
                return false;
            }
            return name != null ? name.equals(repoId.name) : repoId.name == null;
        }

        @Override
        public int hashCode() {
            int result = owner != null ? owner.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }
    }
}