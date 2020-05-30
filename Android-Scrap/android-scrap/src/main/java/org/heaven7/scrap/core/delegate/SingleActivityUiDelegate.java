package org.heaven7.scrap.core.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import org.heaven7.scrap.R;
import org.heaven7.scrap.core.event.ActivityEventCallbackGroup;
import org.heaven7.scrap.core.lifecycle.ActivityLifeCycleDispatcher;
import org.heaven7.scrap.core.oneac.ActivityController;

public class SingleActivityUiDelegate extends AbstractUiDelegate {

    private static final String KEY_DATA = "__saud:data";
    private FrameLayout mFl_top;
    private FrameLayout mFl_content;
    private FrameLayout mFl_bottom;
    private FrameLayout mFl_loading;

    @Override
    public int getLayoutId() {
        return R.layout.lib_style_ac_container;
    }

    @Override
    public ActivityEventCallbackGroup getEventGroup() {
        return ActivityController.get().getEventListenerGroup();
    }
    @Override
    public ActivityLifeCycleDispatcher getLifeCycleDispatcher() {
        return ActivityController.get().getLifeCycleDispatcher();
    }

    @Override
    public void onInitialize(Intent intent, Bundle savedInstanceState) {
        super.onInitialize(intent, savedInstanceState);
        init(savedInstanceState);
    }

    @Override
    public void onFinish() {
        super.onFinish();
        ActivityController.get().attach(null,null,
                null,null,
                null,null);
    }

    private void init(Bundle savedInstanceState) {
        mFl_top = (FrameLayout) findViewById(R.id.lib_style_top);
        mFl_bottom = (FrameLayout) findViewById(R.id.lib_style_bottom);
        mFl_content = (FrameLayout) findViewById(R.id.lib_style_content);
        mFl_loading = (FrameLayout) findViewById(R.id.lib_style_loading);

        /* this must called after the container(top,middle,and bottom) is found ! */
        ActivityController.get().attach(getActivity(), mFl_top, mFl_content, mFl_bottom, mFl_loading, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle b = new Bundle();
        ActivityController.get().getViewController().onSaveInstanceState(b);
        outState.putBundle(KEY_DATA, b);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            Bundle b = savedInstanceState.getBundle(KEY_DATA);
            ActivityController.get().getViewController().onRestoreInstanceState(b);
        }
    }
}
