package com.blogspot.carirunners.run.ui.common;

import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.databinding.PostItemBinding;
import com.blogspot.carirunners.run.util.Objects;
import com.blogspot.carirunners.run.vo.Post;
import com.blogspot.carirunners.run.vo.Repo;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * A RecyclerView adapter for {@link Repo} class.
 */
public class PostListAdapter extends DataBoundListAdapter<Post, PostItemBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final Callback callback;
    private final boolean showFullName;

    public PostListAdapter(DataBindingComponent dataBindingComponent, boolean showFullName,
                           Callback callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.showFullName = showFullName;
    }

    @Override
    protected PostItemBinding createBinding(ViewGroup parent, int viewType) {
        PostItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.post_item,
                        parent, false, dataBindingComponent);
        binding.setShowFullName(showFullName);
        binding.getRoot().setOnClickListener(v -> {
            Post post = binding.getPost();
            if (post != null && callback != null) {
                callback.onClick(post);
            }
        });
        return binding;
    }

    @Override
    protected void bind(PostItemBinding binding, Post item, int viewType) {
        binding.setPost(item);
    }

    @Override
    protected boolean areItemsTheSame(Post oldItem, Post newItem) {
        return Objects.equals(oldItem.id, newItem.id);
    }

    @Override
    protected boolean areContentsTheSame(Post oldItem, Post newItem) {
        return Objects.equals(oldItem.title, newItem.title) &&
                Objects.equals(oldItem.content, newItem.content);
    }

    public interface Callback {
        void onClick(Post post);
    }
}
