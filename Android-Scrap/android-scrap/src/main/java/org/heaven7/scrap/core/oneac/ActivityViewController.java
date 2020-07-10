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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.heaven7.core.util.MainWorker;
import com.heaven7.java.base.anno.CalledInternal;
import com.heaven7.java.base.anno.NonNull;
import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.Reflector;

import org.heaven7.scrap.R;
import org.heaven7.scrap.core.ContainerActivity;
import org.heaven7.scrap.core.anim.AnimateExecutor;
import org.heaven7.scrap.core.lifecycle.ActivityLifeCycleAdapter;
import org.heaven7.scrap.core.lifecycle.IActivityLifeCycleCallback;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * the controller of the one-activity's view.
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

    private static final String KEY_CUR_SCRAP_CLASS = "avc:cur_scrap_class";
    private static final String KEY_CUR_SCRAP_HASH = "avc:cur_scrap_hash";
    private static boolean sDebug = false;

    /**
     * the top middle of activity
     */
    private ViewGroup mContentContainer;

    /**
     * indicate the current BaseScrapView which is showing or the last operating  view
     */
    private BaseScrapView mCurrentView;

    private WeakReference<Activity> mWrfActivity;
    /**
     * the global animate executor
     */
    private AnimateExecutor mGlobalAnimateExecutor;
    /**
     * used for back stack and cache scrap view.
     */
    private final CacheHelper mCacheHelper;

    private final JumpParam mJumpPram = new JumpParam();
    /**
     * the default intent executor to start activity
     */
    private IntentExecutor mDefaultIntentExecutor = new IntentExecutor() {
        @Override
        public void startActivity(Context context) {
            context.startActivity(new Intent(context, ContainerActivity.class)
                    /* .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)*/
            );
        }
    };

    private final IActivityViewProcessor mViewProcessor = new IActivityViewProcessor() {
        @Override
        public void replaceView(View view) {
            addView(view, true);
        }
    };
    private final IBackEventProcessor mBackProcessor = new IBackEventProcessor() {
        @Override
        public boolean handleBackEvent() {
            return onBackPressed();
        }
    };

    /* package */ ActivityViewController() {
        mCacheHelper = new CacheHelper();
    }

    //============================//
    void attachContainers(ViewGroup content) {
        this.mContentContainer = content;
    }

    /***
     * remove the mapping  of key in the cache.
     * @param key the key of view
     * @return the mapping value
     */
    public BaseScrapView removeCache(String key) {
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
    private ActivityViewController show() {
        int visibility = mContentContainer.getVisibility();
        if (visibility != View.VISIBLE) {
            mContentContainer.setVisibility(View.VISIBLE);
            if (mCurrentView != null){
                mCurrentView.onShow();
            }
        }
        return this;
    }

    /**
     * set one of the three scrap's visibility. if you want to toogle visibility.
     *
     * @param visible  true to visible  false to gone
     */
    public ActivityViewController setVisibility(boolean visible) {
        return visible ? show() : hide();
    }

    /**
     * hide to show  or show to hide the scrap.
     */
    public ActivityViewController toggleVisibility() {
        int visibility = mContentContainer.getVisibility();
        if (visibility == View.VISIBLE) {
            mContentContainer.setVisibility(View.GONE);
            if (mCurrentView != null){
                mCurrentView.onHide();
            }
        } else {
            mContentContainer.setVisibility(View.VISIBLE);
            if (mCurrentView != null){
                mCurrentView.onShow();
            }
        }
        return this;
    }

    /**
     * hide the scrap of activity
     */
    private ActivityViewController hide() {
        int visibility = mContentContainer.getVisibility();
        if (visibility == View.VISIBLE) {
            mContentContainer.setVisibility(View.GONE);
            if (mCurrentView != null){
                mCurrentView.onHide();
            }
        }
        return this;
    }
    private void addView(View view, boolean removePrevious){
        if(removePrevious){
            mContentContainer.removeAllViews();
        }
        //may share a view
        mContentContainer.removeView(view);
        mContentContainer.addView(view);
    }

    public void setDefaultIntentExecutor(IntentExecutor executor) {
        mDefaultIntentExecutor = executor;
    }

    /**
     * jump to the target BaseScrapView and no care about cache and back stack.
     * <li>now this method is suspensive, because reflect is not good choice to use.
     */
    public <T extends BaseScrapView> void jumpTo(Context ctx,
                                                 final Class<T> clazz, final Bundle data) {
        T t = Reflector.from(clazz).constructor(Context.class).newInstance(ctx);
        jumpTo(t, data);
    }

    public void jumpTo(@NonNull BaseScrapView v) {
        jumpTo(v, null, mDefaultIntentExecutor, null);
    }

    @Override
    public void jumpTo(@NonNull BaseScrapView v, Bundle data, AnimateExecutor executor) {
        jumpTo(v, data, mDefaultIntentExecutor, executor);
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
     * jump to to target view  with data. if the activity isn't attached into this.
     * the executor will be called. and the target activity must like {@link ContainerActivity}
     *
     * @param v            the view to jump to
     * @param data         the data to carry
     * @param executor     the startActivity executor if activity not attached to this or is finished..
     * @param animExecutor the animate executor to perform this jump.
     */
    private void jumpTo(@NonNull BaseScrapView v, Bundle data, IntentExecutor executor,
                        AnimateExecutor animExecutor) {
        if (v == null)
            throw new NullPointerException();
        if (sDebug)
            Log.v("Scrap", "jumpTo ......" + v);

        if (data != null) {
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
            v.setView(activity.getWindow().getDecorView());
            jumpToImpl(v, animExecutor);
        }
    }

    /**
     * jump to the target view really with animation if need.
     *
     * @param next the target view
     * @param ae   the animate executor to perform this jump.
     */
    private void jumpToImpl(final BaseScrapView next, AnimateExecutor ae) {
        beforeAttach(next);
        final AnimateExecutor tempAnimExecutor = ae != null ? ae : mGlobalAnimateExecutor;
        if (mCurrentView == null || tempAnimExecutor == null) {
            replace(next, tempAnimExecutor);
        } else {
            View root = mCurrentView.getView();
            tempAnimExecutor.performAnimate(root, false, mCurrentView, next,
                    new AnimateExecutor.OnAnimateEndListener() {
                        @Override
                        public void onAnimateEnd(View target) {
                            replace(next, tempAnimExecutor);
                        }
                    });
        }
    }

    /**
     * use the target view's 'top/middle/bottom' and replace with them.
     *
     * @param target       the target scrap view to replace.
     * @param animExecutor the animate executor to perform current jump, if is null use the global
     *                     animateExecutor{@link #mGlobalAnimateExecutor}
     */
    private void replace(BaseScrapView target, AnimateExecutor animExecutor) {
        final AnimateExecutor executor = animExecutor != null ? animExecutor : mGlobalAnimateExecutor;
        final BaseScrapView previous = mCurrentView;
        final View contentView = target.getContentView(mContentContainer);

        mContentContainer.removeAllViews();
        mContentContainer.addView(contentView);

        if (previous != null) {
            detachTarget(previous);
        }
        mCurrentView = target;
        target.onAttach();

        if (executor != null){
            executor.performAnimate(target.getView(), true, previous, target, null);
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
     * detach the target BaseScrapView
     */
    private void detachTarget(BaseScrapView target) {
        synchronized (target) {
            target.onDetach();
            target.setView(null);
            target.unregisterActivityLifeCycleCallbacks();
            target.setActivityViewProcessor(null);
            target.setDefaultBackEventProcessor(null);
            target.setContext(null); //else may cause problem
        }
    }

    /**
     * attach the activity  when activity is launched .this will called.
     *
     * @param activity the activity
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
        mContentContainer = null;
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

    /**
     * set the global animate executor
     */
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
    @CalledInternal
    /* package */ boolean onBackPressed() {
        // current a bug occoured. if scrapview first is in back stack. when back to it.
        // removed it from backstack.
        //second it will not in back stack. already fixed it
        if (sDebug) {
            System.out.println(mCacheHelper.getStackList());
        }
        BaseScrapView view = mCacheHelper.pollStackTail();
        if (sDebug) {
            System.out.println("tail 1: " + view);
        }
        if (view != null && view == mCurrentView) {
            detachTarget(mCurrentView);
            mCurrentView = null;
            view = mCacheHelper.pollStackTail();
            if (sDebug) {
                System.out.println("tail 2: " + view);
            }
        }
        if (view != null) {
            if (view.isInBackStack()) {
                //here ignore the mode to avoid problem ,so first change and restore
                mCacheHelper.mViewStack.setStackMode(StackMode.Normal);
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
     * @param target the scrap view to judge
     */
    public boolean isScrapViewAtBottom(BaseScrapView target) {
        if (target == null)
            return false;
        return mCacheHelper.getStackHead() == target;
    }

    /**
     * internal life cycle callback.if the activity is not attached  !
     */
    private final IActivityLifeCycleCallback mLifeCycleCallback = new ActivityLifeCycleAdapter() {
        @Override
        public void onActivityPostCreate(Activity activity, Bundle savedInstanceState) {
            ScrapHelper.unregisterActivityLifeCycleCallback(this);
            final JumpParam param = mJumpPram;
            BaseScrapView view = param.scrapView;
            view.setContext(activity);
            view.setView(activity.getWindow().getDecorView());
            jumpToImpl(view, param.animateExecutor);
            // clear param to avoid memory leak.
            param.clear();
        }
    };

    @CalledInternal
    public void onSaveInstanceState(Bundle out) {
        int mCurIndex = mCacheHelper.getStackList().indexOf(mCurrentView);
        mCacheHelper.onSaveInstanceState(out, mCurIndex);
    }

    public boolean onRestoreInstanceState(Bundle in) {
        if (in != null) {
            int curIndex = mCacheHelper.onRestoreInstanceState(mContentContainer.getContext(), in);
            List<BaseScrapView> stackList = mCacheHelper.getStackList();
            if (stackList.size() <= 0) {
                return false;
            }
            if (curIndex == -1) {
                //not in stack, default first
                curIndex = 0;
            }
            final BaseScrapView view = stackList.get(curIndex);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    jumpTo(view, view.getBundle());
                }
            });
            return true;
        }
        return false;
    }

    private static class JumpParam {
        public BaseScrapView scrapView;
        public AnimateExecutor animateExecutor;

        public void set(BaseScrapView scrapView, AnimateExecutor animateExecutor) {
            this.scrapView = scrapView;
            this.animateExecutor = animateExecutor;
        }

        public void clear() {
            scrapView = null;
            animateExecutor = null;
        }
    }
}
