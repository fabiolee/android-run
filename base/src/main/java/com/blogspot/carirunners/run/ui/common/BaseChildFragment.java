package com.blogspot.carirunners.run.ui.common;

import android.content.Context;

import com.blogspot.carirunners.run.MainFragment;

/**
 * @author fabiolee
 */
public class BaseChildFragment extends BaseFragment {
    protected MainFragment parentFragment;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            parentFragment = (MainFragment) getParentFragment();
        } catch (Exception e) {
            throw new IllegalArgumentException("Parent fragment must be MainFragment");
        }
    }
}
