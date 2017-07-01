package com.blogspot.carirunners.run.ui.post;

import com.blogspot.carirunners.run.repository.PostRepository;
import com.blogspot.carirunners.run.util.AbsentLiveData;
import com.blogspot.carirunners.run.util.Objects;
import com.blogspot.carirunners.run.vo.Post;
import com.blogspot.carirunners.run.vo.Resource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import javax.inject.Inject;

public class PostViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<PostId> postId;
    private final LiveData<Resource<Post>> post;

    @Inject
    public PostViewModel(PostRepository repository) {
        this.postId = new MutableLiveData<>();
        post = Transformations.switchMap(postId, input -> {
            if (input.isEmpty()) {
                return AbsentLiveData.create();
            } else if (input.path != null) {
                return repository.loadByPath(input.path);
            } else {
                return repository.load(input.id);
            }
        });
    }

    public LiveData<Resource<Post>> getPost() {
        return post;
    }

    public void retry() {
        PostId current = postId.getValue();
        if (current != null && !current.isEmpty()) {
            postId.setValue(current);
        }
    }

    void setId(String id, String path) {
        PostId update = new PostId(id, path);
        if (Objects.equals(postId.getValue(), update)) {
            return;
        }
        postId.setValue(update);
    }

    @VisibleForTesting
    static class PostId {
        public final String id;
        public final String path;

        PostId(String id, String path) {
            this.id = id == null ? null : id.trim();
            this.path = path == null ? null : path.trim();
        }

        boolean isEmpty() {
            return (id == null || id.length() == 0) && (path == null || path.length() == 0);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            PostId postId = (PostId) o;

            if (id != null ? !id.equals(postId.id) : postId.id != null) {
                return false;
            }
            return path != null ? path.equals(postId.path) : postId.path == null;
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (path != null ? path.hashCode() : 0);
            return result;
        }
    }
}
