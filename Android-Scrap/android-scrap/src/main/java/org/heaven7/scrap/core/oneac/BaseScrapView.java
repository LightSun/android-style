/*
 * Copyright (C) 2015
 *            heaven7(donshine723@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.heaven7.scrap.core.oneac;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heaven7.java.base.anno.CalledInternal;

import org.heaven7.scrap.core.event.IActivityEventCallback;
import org.heaven7.scrap.core.lifecycle.ActivityLifeCycleAdapter;
import org.heaven7.scrap.core.lifecycle.IActivityLifeCycleCallback;

/**
 * this is a base view for 'the scrap view'. it help you to fast use.
 * the  BaseScrapView contains 3 views( Top, Middle, and bottom) as the three scrap.
 * often subclass must override {@link #getLayoutId()} ()},
 * if you want different , also you can override {@link #getContentView(ViewGroup)}.
 * <p> if you want to jump to a child of BaseScrapView,please use {@link ScrapHelper}, it has some useful methods.
 * </p>
 *
 * @author heaven7
 * @see ActivityViewController
 * @see ActivityController
 */
public abstract class BaseScrapView implements Comparable<BaseScrapView> {

    private static final String KEY_DATA = "data";
    private static final String KEY_IN_BACK_STACK = "inBackStack";

    private Context mContext;
    private LayoutInflater mInflater;
    /**
     * the bundle data if you want to pass data from one BaseScrapView to another
     */
    private Bundle mArguments;
    /**
     * the content view
     */
    private View mContentView;
    /**
     * the default back event processor it will be autonomic set by framework
     */
    private IBackEventProcessor mDefaultBackEventProcessor;

    /**
     * the callback used to same as the Activity's common life cycle
     */
    private IActivityLifeCycleCallback mLifecycleCallback;

    /**
     * when set to true , this ScrapView will be stack.
     * but if it can be replaced or cleared by another same classname of this.
     */
    private boolean mInBackStack;

    /**
     *
     */
    public BaseScrapView(Context mContext) {
        super();
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        onPostInit();
    }

    /**
     * the automatic called to do the final init.
     * if subclass want to init other please doing it here!
     */
    protected void onPostInit() {

    }

    @Override
    public int compareTo(BaseScrapView o) {
        if (o != null) {
            return getClass().getName().compareTo(o.getClass().getName());
        }
        return 1;
    }
    /**
     * automatic called by framework,when this view is prepared to attach.
     */
    @CalledInternal
    //"when this abstract view is prepared to attach and before attached!"
    /*public*/ void registerActivityLifeCycleCallbacks() {
        if (mLifecycleCallback == null) {
            mLifecycleCallback = new ActivityLifeCycleAdapter() {
                @Override
                public void onActivityCreate(Activity activity, Bundle savedInstanceState) {
                    BaseScrapView.this.onActivityCreate(savedInstanceState);
                }

                public void onActivityPostCreate(Activity activity, Bundle savedInstanceState) {
                    BaseScrapView.this.onActivityPostCreate(savedInstanceState);
                }

                public void onActivityStart(Activity activity) {
                    BaseScrapView.this.onActivityStart();
                }

                public void onActivityResume(Activity activity) {
                    BaseScrapView.this.onActivityResume();
                }

                public void onActivityPause(Activity activity) {
                    BaseScrapView.this.onActivityPause();
                }

                public void onActivityStop(Activity activity) {
                    BaseScrapView.this.onActivityStop();
                }

                public void onActivityDestroy(Activity activity) {
                    BaseScrapView.this.onActivityDestroy();
                }
            };
        }
        ScrapHelper.registerActivityLifeCycleCallback(mLifecycleCallback);
    }

    /**
     * automatic called by framework,when this view is prepared to detach.
     */
    @CalledInternal
//("when this abstract view is prepared to detach and before detached!")
    /*public*/ void unregisterActivityLifeCycleCallbacks() {
        if (mLifecycleCallback != null) {
            ScrapHelper.unregisterActivityLifeCycleCallback(mLifecycleCallback);
        }
    }
    public final View getDecorView() {
        Activity ac = (Activity) getContext();
        return ac.getWindow().getDecorView();
    }

    /*public*/ final View getContentView(ViewGroup group) {
        if (mContentView == null) {
            mContentView = onCreateView(group);
        }
        return mContentView;
    }
    public final View getView(){
    	return mContentView;
	}

    public final <T extends View> T getView(int id) {
        return getView().findViewById(id);
    }
    public void setContext(Context ctx) {
        this.mContext = ctx;
    }
    public Context getContext() {
        return mContext;
    }

    public final LayoutInflater getLayoutInflater() {
        return mInflater;
    }
    public void setArguments(Bundle data) {
        this.mArguments = data;
    }
    public Bundle getArguments() {
        return mArguments;
    }

    /**
     * same as {@link View#getResources()}
     */
    public Resources getResources() {
        return getContext().getResources();
    }

    /**
     * this is used if scrap view is backed from back stack.
     */
    public boolean isInBackStack() {
        return mInBackStack;
    }

    /**
     * set whether or not in back stack  ,often called by framework
     */
    @CalledInternal
    /*public*/ void setInBackStack(boolean mInBackStack) {
        this.mInBackStack = mInBackStack;
    }

    /***
     *  set the default back event processor.
     * @param processor the back processor
     */
    public void setDefaultBackEventProcessor(IBackEventProcessor processor) {
        this.mDefaultBackEventProcessor = processor;
    }

    /**
     * post the runnable run on ui thread
     */
    protected void runOnUiThread(Runnable r) {
        ((Activity) mContext).runOnUiThread(r);
    }

    /**
     * get the middle view as content view
     *
     * @return the content view
     */
    protected View onCreateView(ViewGroup parent) {
        return mInflater.inflate(getLayoutId(), parent, false);
    }
    /**
     * get the layout id of the middle .called by {@link #getContentView(ViewGroup)}
     */
    protected abstract int getLayoutId();

    //===========life cycle ===================//

    /**
     * when the top/middle/bottom hide it's visibility,this will be called.
     */
    @CalledInternal
    protected void onHide() {
    }

    /**
     * when the top/middle/bottom show it's visibility,this will be called.
     */
    @CalledInternal
    protected void onShow() {

    }

    /**
     * when this view is attached done to the activity. this will be called.
     */
    @CalledInternal
    protected void onAttach() {
    }

    /**
     * when this view is detach done to the activity. this will be called.
     * after call this, the context will set to null to avoid memory leak!
     */
    @CalledInternal
    protected void onDetach() {

    }

    //======================  life cycle from activity   ========================//
	/*
	 note:  if activity is created and attached. some method will not called.such as: onActivityCreate()
	 */

    /**
     * comes from 'Activity#onCreate(Bundle)'
     *
     * @param saveInstanceState the state
     */
    protected void onActivityCreate(Bundle saveInstanceState) {
    }

    /**
     * comes from 'Activity#onPostCreate(Bundle)'
     *
     * @param saveInstanceState the state
     */
    protected void onActivityPostCreate(Bundle saveInstanceState) {

    }

    /**
     * comes from 'Activity#onStart()'
     */
    protected void onActivityStart() {
    }

    /**
     * comes from 'Activity#onResume()'
     */
    protected void onActivityResume() {
    }

    /**
     * comes from 'Activity#onPause()'
     */
    protected void onActivityPause() {
    }

    /**
     * comes from 'Activity#onStop()'
     */
    protected void onActivityStop() {
    }

    /**
     * comes from 'Activity#onDestroy()'
     */
    protected void onActivityDestroy() {
    }

    /**
     * handle the back event if you don't consume the back event previous.
     * <li> default this called before {@link ActivityViewController#onBackPressed()}.so
     * if this onBackPressed() return true. The ActivityViewController will not receive this back event.
     *
     * @return true to consume the back event
     * @see org.heaven7.scrap.core.event.ActivityEventCallbackGroup#registerActivityEventListener(IActivityEventCallback...)
     * @see IActivityEventCallback#onBackPressed()
     */
    protected boolean onBackPressed() {
        return mDefaultBackEventProcessor != null && mDefaultBackEventProcessor.handleBackEvent();
    }

    public void onSaveInstanceState(Bundle out) {
        out.putBundle(KEY_DATA, mArguments);
        out.putBoolean(KEY_IN_BACK_STACK, mInBackStack);
    }

    public void onRestoreInstanceState(Bundle in) {
        setArguments(in.getBundle(KEY_DATA));
        setInBackStack(in.getBoolean(KEY_IN_BACK_STACK));
    }
}
