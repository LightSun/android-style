package org.heaven7.scrap.core.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.heaven7.scrap.R;
import org.heaven7.scrap.core.ActivityController;
import org.heaven7.scrap.core.event.ActivityEventCallbackGroup;
import org.heaven7.scrap.core.lifecycle.ActivityLifeCycleDispatcher;

public class SingleActivityUiDelegate extends AbstractUiDelegate {

    private FrameLayout mFl_top;
    private FrameLayout mFl_content;
    private FrameLayout mFl_bottom;
    private FrameLayout mFl_loading;

    @Override
    public int getLayoutId() {
        return R.layout.activity_container;
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
        mFl_top = (FrameLayout) findViewById(R.id.fl_top);
        mFl_bottom = (FrameLayout) findViewById(R.id.fl_bottom);
        mFl_content = (FrameLayout) findViewById(R.id.fl_content);
        mFl_loading = (FrameLayout) findViewById(R.id.fl_loading);

        attachAndDispatchOnCreate(mFl_top, mFl_content, mFl_bottom, mFl_loading, savedInstanceState);
    }

    /** this must called after the container(top,middle,and bottom) is found ! */
    protected void attachAndDispatchOnCreate(ViewGroup top,
                                             ViewGroup middle, ViewGroup bottom, ViewGroup loading, Bundle savedInstanceState) {
        ActivityController.get().attach(getActivity(), top, middle, bottom,loading, savedInstanceState);
        ActivityController.get().getLifeCycleDispatcher()
                .dispatchActivityOnCreate(getActivity(), savedInstanceState);
    }
}
