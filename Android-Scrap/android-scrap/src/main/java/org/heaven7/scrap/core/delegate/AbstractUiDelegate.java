package org.heaven7.scrap.core.delegate;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

import org.heaven7.scrap.core.ScrapConstant;
import org.heaven7.scrap.core.event.ActivityEventCallbackGroup;
import org.heaven7.scrap.core.lifecycle.ActivityLifeCycleDispatcher;

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

    public void onFinish() {

    }
    public void onDestroy(){

    }
}
