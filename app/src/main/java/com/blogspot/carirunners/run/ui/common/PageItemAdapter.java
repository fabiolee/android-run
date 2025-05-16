package com.blogspot.carirunners.run.ui.common;

import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.databinding.PageContentItemBinding;
import com.blogspot.carirunners.run.databinding.PageTitleItemBinding;
import com.blogspot.carirunners.run.util.Objects;
import com.blogspot.carirunners.run.vo.PageItem;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class PageItemAdapter
        extends DataBoundListAdapter<PageItem, ViewDataBinding> {

    private static final int VIEW_TYPE_TITLE = 0;
    private static final int VIEW_TYPE_CONTENT = 1;

    private final DataBindingComponent dataBindingComponent;
    private final Callback callback;

    public PageItemAdapter(DataBindingComponent dataBindingComponent, Callback callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_CONTENT;
    }

    @Override
    protected ViewDataBinding createBinding(ViewGroup parent, int viewType) {
        ViewDataBinding binding;
        switch (viewType) {
            case VIEW_TYPE_TITLE:
                binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.page_title_item, parent, false,
                        dataBindingComponent);
                break;
            case VIEW_TYPE_CONTENT:
                binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.page_content_item, parent, false,
                        dataBindingComponent);
                binding.getRoot().setOnClickListener(v -> {
                    PageItem pageItem = ((PageContentItemBinding) binding).getPageItem();
                    if (pageItem != null && callback != null) {
                        callback.onClick(pageItem);
                    }
                });
                break;
            default:
                binding = null;
                break;
        }
        return binding;
    }

    @Override
    protected void bind(ViewDataBinding binding, int position, PageItem item, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_TITLE:
                ((PageTitleItemBinding) binding).setPageItem(item);
                break;
            case VIEW_TYPE_CONTENT:
                PageContentItemBinding contentBinding = (PageContentItemBinding) binding;
                contentBinding.setPageItem(item);
                contentBinding.setPageItemCallback(callback);
                break;
        }
    }

    @Override
    protected boolean areItemsTheSame(PageItem oldItem, PageItem newItem) {
        return Objects.equals(oldItem.urlPath, newItem.urlPath);
    }

    @Override
    protected boolean areContentsTheSame(PageItem oldItem, PageItem newItem) {
        return Objects.equals(oldItem.title, newItem.title)
                && Objects.equals(oldItem.urlPath, newItem.urlPath)
                && oldItem.favorite == newItem.favorite;
    }

    public interface Callback {
        void onClick(PageItem pageItem);

        void onClickFavorite(PageItem pageItem);
    }
}
