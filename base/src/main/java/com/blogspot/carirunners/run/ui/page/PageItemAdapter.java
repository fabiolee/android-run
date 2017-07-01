package com.blogspot.carirunners.run.ui.page;

import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.databinding.PageItemBinding;
import com.blogspot.carirunners.run.ui.common.DataBoundListAdapter;
import com.blogspot.carirunners.run.util.Objects;
import com.blogspot.carirunners.run.vo.PageItem;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class PageItemAdapter
        extends DataBoundListAdapter<PageItem, PageItemBinding> {

    private final DataBindingComponent dataBindingComponent;
    private final Callback callback;

    public PageItemAdapter(DataBindingComponent dataBindingComponent, Callback callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
    }

    @Override
    protected PageItemBinding createBinding(ViewGroup parent) {
        PageItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.page_item, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            PageItem pageItem = binding.getPageItem();
            if (pageItem != null && callback != null) {
                callback.onClick(pageItem);
            }
        });
        return binding;
    }

    @Override
    protected void bind(PageItemBinding binding, PageItem item) {
        binding.setPageItem(item);
    }

    @Override
    protected boolean areItemsTheSame(PageItem oldItem, PageItem newItem) {
        return Objects.equals(oldItem.urlPath, newItem.urlPath);
    }

    @Override
    protected boolean areContentsTheSame(PageItem oldItem, PageItem newItem) {
        return Objects.equals(oldItem.title, newItem.title)
                && Objects.equals(oldItem.urlPath, newItem.urlPath);
    }

    public interface Callback {
        void onClick(PageItem pageItem);
    }
}
