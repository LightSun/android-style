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
package org.heaven7.scrap.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.heaven7.scrap.R;
import org.heaven7.scrap.annotation.CalledByFramework;
import org.heaven7.scrap.annotation.NonNull;
import org.heaven7.scrap.annotation.Nullable;
import org.heaven7.scrap.core.anim.AnimateExecutor;
import org.heaven7.scrap.core.lifecycle.ActivityLifeCycleAdapter;
import org.heaven7.scrap.core.lifecycle.IActivityLifeCycleCallback;
import org.heaven7.scrap.util.ArrayList2;
import org.heaven7.scrap.util.Reflector;
import org.heaven7.scrap.util.ScrapHelper;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * the controller of the activity's view.
 * <li> use {@link #jumpTo(BaseScrapView, Bundle)} to switch page.
 * <li> use {@link #cache(String, BaseScrapView)} to cache the page. and {@link #getCachedView(String)} to find it.
 * <li> if you want to  make the BaseScrapView add to the default back stack. use {@link #beginTransaction()}
 * <li> such as: <p>
 * <pre> BaseScrapView view = ...;
 * ActivityViewController.beginTransaction().cache(view).addBackAsTop().jump().commit() ;
 * // or use this: ActivityViewController.beginTransaction().addBackAsTop(view).jump().commit() ;
 * </pre>
 * in first statement <pre>
 * #cache(...)  means cache the BaseScrapView.
 * #addToBack() means make the BaseScrapView (which is cached called by #cache(...) ) add to the default back stack.
 * #jump()   means  jump to the page of BaseScrapView which is added to back.
 * #commit()  means commit this operation . this must be called in {@link Transaction}.
 *                   </pre>
 * in annotated statement . not cache the view , only add to the default back stack.
 * </p></br>
 * <li> Note : if the back event is handled by the other  {@link org.heaven7.scrap.core.event.IActivityEventCallback}.  {@link Transaction#addBackAsTop()}  will not  be effective.
 * the more to see in {@link Transaction}
 *
 * @author heaven7
 * @see Transaction
 */
public final class ActivityViewController implements Transaction.IJumper {

    static boolean sDebug = false;
    /**
     * the top container of activity
     */
    private ViewGroup mTopContainer;

    /**
     * the top middle of activity
     */
    private ViewGroup mMiddleContainer;

    /**
     * the top bottom of activity
     */
    private ViewGroup mBottomContainer;
    /** the loading container  */
    private ViewGroup mLoadingContainer;

    /**
     * indicate the current BaseScrapView which is showing or the last operating  view
     */
    private BaseScrapView mCurrentView;

    private WeakReference<Activity> mWrfActivity;
    /**
     * the global animate executor
     */
    private AnimateExecutor mGlobalAnimateExecutor;
    /**  used for back stack and cache scrap view. */
    private final CacheHelper mCacheHelper;

    private final JumpParam mJumpPram  = new JumpParam();
    /**
     * the default intent executor to start activity
     */
    private final IntentExecutor mDefaultIntentExecutor = new IntentExecutor() {
        @Override
        public void startActivity(Context context) {
            context.startActivity(new Intent(context, ContainerActivity.class)
                           /* .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)*/
            );
        }
    };

    private final IActivityViewProcessor mViewProcessor = new IActivityViewProcessor() {
        @Override
        public void replaceView(View view, ScrapPosition position) {
            if (position == null)
                throw new NullPointerException();
            switch (position) {
                case Top:
                    top(view);
                    break;
                case Middle:
                    middle(view);
                    break;
                case Bottom:
                    bottom(view);
                    break;
                default:
                    throw new IllegalStateException();
            }
        }
    };
    private final IBackEventProcessor mBackProcessor = new IBackEventProcessor() {
        @Override
        public boolean handleBackEvent() {
            return onBackPressed();
        }
    };
    private View mDefaultLoadingView;

    /* package */ ActivityViewController() {
        mCacheHelper = new CacheHelper();
    }

    //============================//
    void attachContainers(ViewGroup top, ViewGroup middle, ViewGroup bottom,ViewGroup loading) {
        this.mTopContainer = top;
        this.mMiddleContainer = middle;
        this.mBottomContainer = bottom;
        this.mLoadingContainer = loading;
        //default loading is gone
        if(loading != null){
            this.mDefaultLoadingView = loading.findViewById(R.id.pb);
            loading.removeAllViews();
            loading.setVisibility(View.GONE);
        }
    }

    /**
     * replace the top view
     */
    /*private*/ ActivityViewController top(@Nullable View v) {
        if (mTopContainer.getChildCount() > 0)
            mTopContainer.removeAllViews();
        if (v != null)
            mTopContainer.addView(v);
        return this;
    }

    /**
     * replace the middle view
     */
	/*private*/ ActivityViewController middle(@Nullable View v) {
        if (mMiddleContainer.getChildCount() > 0)
            mMiddleContainer.removeAllViews();
        if (v != null) {
            mMiddleContainer.addView(v);
        }
        return this;
    }

    /**
     * replace the bottom view
     */
    private ActivityViewController bottom(@Nullable View v) {
        if (mBottomContainer.getChildCount() > 0) {
            mBottomContainer.removeAllViews();
        }
        if (v != null) {
            mBottomContainer.addView(v);
        }
        return this;
    }
    private ActivityViewController loading(@Nullable View v) {
        if (mLoadingContainer.getChildCount() > 0) {
            mLoadingContainer.removeAllViews();
        }
        if (v != null) {
            mLoadingContainer.addView(v);
        }
        return this;
    }

    /***
     * remove the mapping  of key in the cache.
     * @param key
     * @return  the mapping value
     */
    public BaseScrapView removeCache(String key){
        return mCacheHelper.removeCacheView(key);
    }

    /**
     * make the BaseScrapView in the cache for reuse.
     *
     * @param key  the key to find for resue.
     * @param view which view you want to cache
     */
    public ActivityViewController cache(String key, BaseScrapView view) {
        mCacheHelper.cache(key, view);
        return this;
    }

    /**
     * make the BaseScrapView in the cache for reuse and the
     * default key is the view.getClass().getName()
     */
    public ActivityViewController cache(BaseScrapView view) {
        return cache(view.getClass().getName(), view);
    }

    /**
     * @return the cached view which you previous cached.
     * eg: {@link #cache(String, BaseScrapView)}
     * @see Transaction#cache(String, BaseScrapView)
     * @see CacheHelper#cache(String, BaseScrapView)
     */
    public BaseScrapView getCachedView(String key) {
        return mCacheHelper.getCacheView(key);
    }

    /**
     * open Transaction for better use.
     */
    public Transaction beginTransaction() {
        return new Transaction(mCacheHelper, this);
    }

    //======================= end  ===================//

    /**
     * show the scrap of activity
     */
    private ActivityViewController show(@NonNull ScrapPosition position) {
        switch (position) {
            case Top: {
                int visibility = mTopContainer.getVisibility();
                if (visibility != View.VISIBLE) {
                    mTopContainer.setVisibility(View.VISIBLE);
                    if (mCurrentView != null)
                        mCurrentView.onShow(ScrapPosition.Top);
                }
            }
            break;
            case Middle: {
                int visibility = mMiddleContainer.getVisibility();
                if (visibility != View.VISIBLE) {
                    mMiddleContainer.setVisibility(View.VISIBLE);
                    if (mCurrentView != null)
                        mCurrentView.onShow(ScrapPosition.Middle);
                }
            }
            break;
            case Bottom: {
                int visibility = mBottomContainer.getVisibility();
                if (visibility != View.VISIBLE) {
                    mBottomContainer.setVisibility(View.VISIBLE);
                    if (mCurrentView != null)
                        mCurrentView.onShow(ScrapPosition.Bottom);
                }
            }
            break;

            default:
                throw new RuntimeException("unsupprt");
        }

        return this;
    }

    /**
     * set one of the three scrap's visibility. if you want to toogle visibility.
     * use {@link #toogleVisibility(ScrapPosition)}
     *
     * @param position the position of the scrap.can't be null.
     * @param visible  true to visible  false to gone
     */
    public ActivityViewController setVisibility(@NonNull ScrapPosition position, boolean visible) {
        return visible ? show(position) : hide(position);
    }

    /**
     * hide to show  or show to hide the scrap.
     */
    public ActivityViewController toogleVisibility(@NonNull ScrapPosition position) {
        switch (position) {
            case Top: {
                int visibility = mTopContainer.getVisibility();
                if (visibility == View.VISIBLE) {
                    mTopContainer.setVisibility(View.GONE);
                    if (mCurrentView != null)
                        mCurrentView.onHide(ScrapPosition.Top);
                } else {
                    mTopContainer.setVisibility(View.VISIBLE);
                    if (mCurrentView != null)
                        mCurrentView.onShow(ScrapPosition.Top);
                }
            }
            break;
            case Middle: {
                int visibility = mMiddleContainer.getVisibility();
                if (visibility == View.VISIBLE) {
                    mMiddleContainer.setVisibility(View.GONE);
                    if (mCurrentView != null)
                        mCurrentView.onHide(ScrapPosition.Middle);
                } else {
                    mMiddleContainer.setVisibility(View.VISIBLE);
                    if (mCurrentView != null)
                        mCurrentView.onShow(ScrapPosition.Middle);
                }
            }
            break;
            case Bottom: {
                int visibility = mBottomContainer.getVisibility();
                if (visibility == View.VISIBLE) {
                    mBottomContainer.setVisibility(View.GONE);
                    if (mCurrentView != null)
                        mCurrentView.onHide(ScrapPosition.Bottom);
                } else {
                    mBottomContainer.setVisibility(View.VISIBLE);
                    if (mCurrentView != null)
                        mCurrentView.onShow(ScrapPosition.Bottom);
                }
            }
            break;
            default:
                throw new RuntimeException("unsupprt");
        }
        return this;
    }

    /**
     * hide the scrap of activity
     */
    private ActivityViewController hide(ScrapPosition position) {
        switch (position) {
            case Top: {
                int visibility = mTopContainer.getVisibility();
                if (visibility == View.VISIBLE) {
                    mTopContainer.setVisibility(View.GONE);
                    if (mCurrentView != null)
                        mCurrentView.onHide(ScrapPosition.Top);
                }
            }
            break;
            case Middle: {
                int visibility = mMiddleContainer.getVisibility();
                if (visibility == View.VISIBLE) {
                    mMiddleContainer.setVisibility(View.GONE);
                    if (mCurrentView != null)
                        mCurrentView.onHide(ScrapPosition.Middle);
                }
            }
            break;
            case Bottom: {
                int visibility = mBottomContainer.getVisibility();
                if (visibility == View.VISIBLE) {
                    mBottomContainer.setVisibility(View.GONE);
                    if (mCurrentView != null)
                        mCurrentView.onHide(ScrapPosition.Bottom);
                }
            }
            break;

            default:
                throw new RuntimeException("unsupprt");
        }

        return this;
    }

    /**
     * jump to the target BaseScrapView and no care about cache and back stack.
     * <li>now this method is suspensive, because reflect is not good choice to use.
     */
	/* suspensive */ <T extends BaseScrapView> void jumpTo(Context ctx,
                                                           final Class<T> clazz, final Bundle data) {
        T t = Reflector.from(clazz).constructor(Context.class).create(ctx);
        jumpTo(t, data);
    }

    public void jumpTo(@NonNull BaseScrapView v) {
        jumpTo(v, null,mDefaultIntentExecutor,null);
    }
    @Override
    public void jumpTo(@NonNull BaseScrapView v,Bundle data,AnimateExecutor executor) {
        jumpTo(v, data,mDefaultIntentExecutor,executor);
    }

    /**
     * jumto to target view  with data. if the activity isn't attached into this.
     * the executor will be called. and the target activity must like {@link ContainerActivity}
     *
     * @param v        the view to jump to
     * @param data     the data to carry
     * @param executor the startActivity executor if activity not attached to this or is finished..
     * @param animExecutor  the animate executor to perform this jump.
     */
    private void jumpTo(@NonNull BaseScrapView v, Bundle data, IntentExecutor executor,
                         AnimateExecutor animExecutor) {
        if (v == null)
            throw new NullPointerException();
        if(sDebug)
            Log.v("Scrap", "jumpTo ......" + v) ;

        if(data != null){
            v.setBundle(data);
        }
        final Activity activity = getActivity();
        //not attached
        if (activity == null) {
            mJumpPram.set(v, animExecutor);
            ScrapHelper.registerActivityLifeCycleCallback(mLifeCycleCallback);
            executor.startActivity(v.getContext());
        } else {
            //may is finishing. check it
            if (activity.isFinishing()) {
                attachActivity(null);
                jumpTo(v, data);
                return;
            }
            v.setContext(activity);
            v.setViewHelper(new ViewHelper(activity.getWindow().getDecorView()));
            jumpToImpl(v,animExecutor);
        }
    }

    /**
     * jump to the target view with data.
     * and start {@link ContainerActivity} if it not attached to this.
     *
     * @see BaseScrapView#setBundle(Bundle)
     */
    public void jumpTo(@NonNull BaseScrapView v, final Bundle data) {
        jumpTo(v, data, mDefaultIntentExecutor, null);
    }

    /**
     * jump to the target view really with animation if need.
     * @param next the target view
     * @param ae  the animate executor to perform this jump.
     */
    private void jumpToImpl(final BaseScrapView next,AnimateExecutor ae) {
        beforeAttach(next);
        final AnimateExecutor tempAnimExecutor = ae!=null ? ae :mGlobalAnimateExecutor;
        if (mCurrentView == null || tempAnimExecutor == null) {
            replace(next,tempAnimExecutor);
        } else {
            View root = mCurrentView.getViewHelper().getRootView();
            tempAnimExecutor.performAnimate(root, false, mCurrentView, next,
                    new AnimateExecutor.OnAnimateEndListener() {
                        @Override
                        public void onAnimateEnd(View target) {
                            replace(next,tempAnimExecutor);
                        }
                    });
        }
    }

    private void beforeAttach(BaseScrapView next) {
        synchronized (next) {
            next.registerActivityLifeCycleCallbacks();
            next.setDefaultBackEventProcessor(mBackProcessor);
            next.setActivityViewProcessor(mViewProcessor);
        }
    }

    /**
     *  use the target view's 'top/middle/bottom' and replace with them.
     * @param target the target scrap view to replace.
     * @param animExecutor  the animate executor to perform current jump, if is null use the global
     *                      animateExecutor{@link #mGlobalAnimateExecutor}
     */
    private void replace(BaseScrapView target,AnimateExecutor animExecutor) {
        final BaseScrapView previous = mCurrentView;
        final View topView = target.getTopView();
        final View middleView = target.getMiddleView();
        final View bottomView = target.getBottomView();

        top(topView).middle(middleView).bottom(bottomView);

        if (previous != null) {
            detachTarget(previous);
        }
        mCurrentView = target;
        target.onAttach();
        AnimateExecutor executor = animExecutor!=null? animExecutor : mGlobalAnimateExecutor;
        if (executor != null)
            executor.performAnimate(target.getViewHelper().getRootView(), true, previous, target, null);
    }

    /**
     * detach the target BaseScrapView
     */
    private void detachTarget(BaseScrapView target) {
        synchronized (target) {
            target.onDetach();
            target.setViewHelper(null);
            target.unregisterActivityLifeCycleCallbacks();
            target.setActivityViewProcessor(null);
            target.setDefaultBackEventProcessor(null);
            target.setContext(null); //else may cause problem
        }
    }

    /**
     * attach the activity  when activity is launched .this will called.
     *
     * @param activity
     */
	/*public*/ void attachActivity(Activity activity) {
        if (activity == null) {
            //means clear all
            reset();
        } else {
            this.mWrfActivity = new WeakReference<Activity>(activity);
        }
    }

    /**
     * reset all
     */
    private void reset() {
        this.mWrfActivity = null;
        mBottomContainer = null;
        mMiddleContainer = null;
        mTopContainer = null;
        mLoadingContainer = null;
        mDefaultLoadingView = null;
        if (mCurrentView != null) {
            detachTarget(mCurrentView);
            mCurrentView = null;
        }
    }

    /**
     * @return the animate executor which will be used to start animations (which across two BaseScrapView ).
     */
    public AnimateExecutor getAnimateExecutor() {
        return mGlobalAnimateExecutor;
    }

    /** set the global animate executor */
    public void setAnimateExecutor(@Nullable AnimateExecutor executor) {
        this.mGlobalAnimateExecutor = executor;
    }

    /**
     * return the attached activity. may be null if the activity is finished
     */
    public Activity getActivity() {
        return mWrfActivity != null ? mWrfActivity.get() : null;
    }

    /**
     * @return true if the attached Activity is running. false indicate it is finished or not attached.
     */
    public boolean isActivityRunning() {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            return true;
        }
        return false;
    }

    /**
     * get the back stack list of BaseScrapView. at the head is oldest. end is the newest.
     */
    public List<BaseScrapView> getStackList() {
        return mCacheHelper.getStackList();
    }

    /**
     * handle the default back behaviour of the activity's back event
     */
    @CalledByFramework
	/* package */ boolean onBackPressed() {
        // current a bug occoured. if scrapview first is in back stack. when back to it.
        // removed it from backstack.
        //second it will not in back stack. already fixed it
        if(sDebug){
            System.out.println(mCacheHelper.getStackList());
        }
        BaseScrapView view = mCacheHelper.pollStackTail();
        if(sDebug){
            System.out.println("tail 1: "+view);
        }
        if (view != null && view == mCurrentView) {
            detachTarget(mCurrentView);
            mCurrentView = null;
            view = mCacheHelper.pollStackTail();
            if(sDebug){
                System.out.println("tail 2: "+view);
            }
        }
        if (view != null) {
            if(view.isInBackStack()) {
                //here ignore the mode to avoid problem ,so first change and restore
                mCacheHelper.mViewStack.setMode(ArrayList2.ExpandArrayList2.Mode.Normal);
                mCacheHelper.addToStackTop(view);
                mCacheHelper.resetStackSetting();
            }
            jumpTo(view);
            return true;
        }
        if (mCurrentView != null) {
            //mCurrentView.getToaster().show("[ onBackPressed() ]: mCurrentView!=null");
            detachTarget(mCurrentView);
            mCurrentView = null;
        }
        return false;
    }

    /**
     * finish the activity which is attached into controller.
     */
	/* package */ void finishActivity() {
        Activity activity = getActivity();
        if (activity != null) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
            attachActivity(null);
        }
    }

    /***
     * get the current view of scrap.
     */
    public BaseScrapView getCurrentView() {
        return mCurrentView;
    }


    /**
     * is the target scrap view at the bottom of stack.
     * <li>bottom means it will be last removed from back stack.</li>
     *
     * @param target
     * @return
     */
    public boolean isScrapViewAtBottom(BaseScrapView target) {
        if (target == null)
            return false;
        return mCacheHelper.getStackHead() == target;
    }

    /** set the global loading view , this will be used if you call {@link BaseScrapView#setShowLoading(boolean)}*/
    public void setLoadingView(View loading) {
        this.mDefaultLoadingView = loading;
    }

    /** set to show or hide loading view */
    @CalledByFramework("about loading view")
    /*public*/ void showOrHideLoading(LoadingParam param) {
        if(param.showLoading ){
            //loading view : from not show -> show
            if(mLoadingContainer.getVisibility() != View.VISIBLE){
                if(mDefaultLoadingView.getParent() == null){
                    loading(mDefaultLoadingView);
                }
                mLoadingContainer.setVisibility(View.VISIBLE);
                mTopContainer.setVisibility(param.showTop? View.VISIBLE:View.GONE);
                mBottomContainer.setVisibility(param.showBottom? View.VISIBLE:View.GONE);
                mMiddleContainer.setVisibility(View.GONE);
            }
        }else{
            //loading view : from show -> not show. show others
            if(mLoadingContainer.getVisibility() == View.VISIBLE){
                mLoadingContainer.setVisibility(View.GONE);
                mMiddleContainer.setVisibility(View.VISIBLE);
                mTopContainer.setVisibility(param.showTop? View.VISIBLE:View.GONE);
                mBottomContainer.setVisibility(param.showBottom? View.VISIBLE:View.GONE);
            }
        }
    }

    /** internal life cycle callback.if the activity is not attached  ! */
    private final IActivityLifeCycleCallback mLifeCycleCallback = new ActivityLifeCycleAdapter(){
        @Override
        public void onActivityPostCreate(Activity activity, Bundle savedInstanceState) {
            ScrapHelper.unregisterActivityLifeCycleCallback(this);
            final JumpParam param = mJumpPram;
            BaseScrapView view = param.scrapView;
            view.setContext(activity);
            view.setViewHelper(new ViewHelper(activity.getWindow().getDecorView()));
            jumpToImpl(view, param.animateExecutor);
            // clear param to avoid memory leak.
            param.clear();
        }
    };

    private class JumpParam{
        public BaseScrapView scrapView;
        public AnimateExecutor animateExecutor;

        public void set(BaseScrapView scrapView,AnimateExecutor animateExecutor){
            this.scrapView = scrapView;
            this.animateExecutor = animateExecutor;
        }
        public void clear(){
            scrapView = null;
            animateExecutor = null;
        }
    }
}
