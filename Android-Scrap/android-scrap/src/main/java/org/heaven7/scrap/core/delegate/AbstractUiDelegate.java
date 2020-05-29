package org.heaven7.scrap.core.delegate;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.heaven7.core.util.AppUtils;

import org.heaven7.scrap.core.ScrapConstant;
import org.heaven7.scrap.core.event.ActivityEventCallbackGroup;
import org.heaven7.scrap.core.lifecycle.ActivityLifeCycleDispatcher;
import org.heaven7.scrap.util.Utils;

import java.util.List;

public abstract class AbstractUiDelegate {

    private FragmentActivity activity;
    private Object parameter;

    private ActivityLifeCycleDispatcher mLifecycleDispatcher;
    private ActivityEventCallbackGroup mEventGroup;

    public FragmentActivity getActivity() {
        return activity;
    }
    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }
    public <T extends View> T findViewById(@IdRes int id){
        return activity.findViewById(id);
    }
    public Resources getResources(){
        return activity.getResources();
    }
    @SuppressWarnings("unchecked")
    public <T> T getParameter(){
        return (T) parameter;
    }

    public ActivityLifeCycleDispatcher getLifeCycleDispatcher(){
        if(mLifecycleDispatcher == null){
            mLifecycleDispatcher = new ActivityLifeCycleDispatcher();
        }
        return mLifecycleDispatcher;
    }
    public ActivityEventCallbackGroup getEventGroup(){
        if(mEventGroup == null){
            mEventGroup = new ActivityEventCallbackGroup();
        }
        return mEventGroup;
    }

    //--------------------------------------------

    /**
     * called on pre setContentView
     */
    public void onPreSetContentView() {
    }

    public abstract int getLayoutId();

    @CallSuper
    public void onInitialize(Intent intent, Bundle savedInstanceState){
        Bundle extras = intent.getExtras();
        if(extras != null){
            parameter = extras.get(ScrapConstant.KEY_PARAMETER);
        }
    }

    public boolean onBackPressed() {
        return false;
    }
    @CallSuper
    public void onFinish() {

    }
    @CallSuper
    public void onDestroy(){

    }

    public void onSaveInstanceState(Bundle outState) {

    }
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }
    //------------------------------------ extra  ------------------------------------------
    /**
     * 清除所有已存在的 Fragment 防止因重建 Activity 时，前 Fragment 没有销毁和重新复用导致界面重复显示
     * 如果有自己实现 Fragment 的复用，请复写此方法并不实现内容
     */
    public void clearAllFragments() {
        List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
        if (fragments.size() == 0) return;

        FragmentTransaction transaction = getActivity()
                .getSupportFragmentManager()
                .beginTransaction();
        for (Fragment fragment : fragments) {
            transaction.remove(fragment);
        }
        transaction.commitNow();
    }

    /**
     * make action bar to fit system-bar height.
     * @param top the top view. often is the title-bar
     * @param fitSystemWindow true to fit system windows
     */
    public void fitActionbarHeight(View top, boolean fitSystemWindow) {
        LinearLayout.MarginLayoutParams layoutParams = (LinearLayout.MarginLayoutParams) top.getLayoutParams();
        layoutParams.topMargin = fitSystemWindow ? Utils.getSystemUIHeight(getActivity()) : 0;
        top.requestLayout();
    }

    /**
     * set status bar
     */
    public void setStatusBar() {
        FragmentActivity activity = getActivity();
        AppUtils.fitStatusBarHeight(activity);
        AppUtils.setStatusBar(activity.getWindow(), getStatusBarColor());
    }
    protected int getStatusBarColor() {
        return Color.LTGRAY;
    }
}
