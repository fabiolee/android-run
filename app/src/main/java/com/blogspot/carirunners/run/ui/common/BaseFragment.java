package com.blogspot.carirunners.run.ui.common;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.blogspot.carirunners.run.MainActivity;

/**
 * @author fabiolee
 */
public class BaseFragment extends Fragment {
    protected MainActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activity = (MainActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must be MainActivity");
        }
    }
}
