package org.heaven7.scrap.core.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;

import org.heaven7.scrap.R;
import org.heaven7.scrap.core.ScrapConstant;

public abstract class StyledUiDelegate extends AbstractUiDelegate {

    private boolean mOverlapStyle;
    private ViewGroup mTopContainer;
    private ViewGroup mContentContainer;
    private ViewGroup mBottomContainer;

    public boolean isOverlapStyle() {
        return mOverlapStyle;
    }
    public ViewGroup getTopContainer() {
        return mTopContainer;
    }
    public ViewGroup getBottomContainer() {
        return mBottomContainer;
    }
    public ViewGroup getContentContainer() {
        return mContentContainer;
    }

    @Override
    public int getLayoutId() {
        return mOverlapStyle ? R.layout.lib_style_ac_overlap : R.layout.lib_style_ac_no_overlap;
    }
    @Override
    public void onInitialize(Intent intent, Bundle savedInstanceState) {
        super.onInitialize(intent, savedInstanceState);
        mTopContainer = findViewById(R.id.lib_style_top);
        mBottomContainer = findViewById(R.id.lib_style_bottom);
        mContentContainer = findViewById(R.id.lib_style_content);
    }

    @CallSuper
    @Override
    public void onPreSetContentView() {
        mOverlapStyle = getActivity().getIntent().getBooleanExtra(ScrapConstant.KEY_OVERLAP, false);
    }
}
