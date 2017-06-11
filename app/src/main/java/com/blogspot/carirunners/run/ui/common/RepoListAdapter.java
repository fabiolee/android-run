package com.blogspot.carirunners.run.ui.common;

import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.databinding.RepoItemBinding;
import com.blogspot.carirunners.run.util.Objects;
import com.blogspot.carirunners.run.vo.Repo;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * A RecyclerView adapter for {@link Repo} class.
 */
public class RepoListAdapter extends DataBoundListAdapter<Repo, RepoItemBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final RepoClickCallback repoClickCallback;
    private final boolean showFullName;

    public RepoListAdapter(DataBindingComponent dataBindingComponent, boolean showFullName,
            RepoClickCallback repoClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.repoClickCallback = repoClickCallback;
        this.showFullName = showFullName;
    }

    @Override
    protected RepoItemBinding createBinding(ViewGroup parent) {
        RepoItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.repo_item,
                        parent, false, dataBindingComponent);
        binding.setShowFullName(showFullName);
        binding.getRoot().setOnClickListener(v -> {
            Repo repo = binding.getRepo();
            if (repo != null && repoClickCallback != null) {
                repoClickCallback.onClick(repo);
            }
        });
        return binding;
    }

    @Override
    protected void bind(RepoItemBinding binding, Repo item) {
        binding.setRepo(item);
    }

    @Override
    protected boolean areItemsTheSame(Repo oldItem, Repo newItem) {
        return Objects.equals(oldItem.owner, newItem.owner) &&
                Objects.equals(oldItem.name, newItem.name);
    }

    @Override
    protected boolean areContentsTheSame(Repo oldItem, Repo newItem) {
        return Objects.equals(oldItem.description, newItem.description) &&
                oldItem.stars == newItem.stars;
    }

    public interface RepoClickCallback {
        void onClick(Repo repo);
    }
}
