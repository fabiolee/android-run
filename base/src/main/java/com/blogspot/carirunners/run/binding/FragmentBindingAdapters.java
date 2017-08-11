package com.blogspot.carirunners.run.binding;

import android.databinding.BindingAdapter;
import android.support.v4.app.Fragment;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

/**
 * Binding adapters that work with a fragment instance.
 */
public class FragmentBindingAdapters {
    final Fragment fragment;

    @Inject
    public FragmentBindingAdapters(Fragment fragment) {
        this.fragment = fragment;
    }

    @BindingAdapter("imageUrl")
    public void imageUrl(ImageView imageView, String url) {
        Glide.with(fragment).load(url).into(imageView);
    }

    @BindingAdapter("loadDataWithBaseURL")
    public void loadDataWithBaseURL(WebView webView, String data) {
        webView.loadDataWithBaseURL(null, data, "text/html", "utf-8",
                null);
    }
}
