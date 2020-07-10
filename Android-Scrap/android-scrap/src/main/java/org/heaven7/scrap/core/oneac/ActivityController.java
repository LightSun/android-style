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
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RestrictTo;

import com.heaven7.java.base.anno.CalledInternal;
import com.heaven7.java.base.util.Reflector;

import org.heaven7.scrap.core.ScrapConstant;
import org.heaven7.scrap.core.anim.AnimateExecutor;
import org.heaven7.scrap.core.event.ActivityEventAdapter;
import org.heaven7.scrap.core.event.ActivityEventCallbackGroup;
import org.heaven7.scrap.core.event.IActivityEventCallback;
import org.heaven7.scrap.core.lifecycle.ActivityLifeCycleDispatcher;
import org.heaven7.scrap.util.Utils;

import java.util.Properties;

/**
 * the controller of 'one-activity'. contains: lifeCycle({@link ActivityLifeCycleDispatcher}) and
 * event ( {@link ActivityEventCallbackGroup}) and the view controller({@link ActivityViewController})
 *
 * @author heaven7
 * @see ActivityViewController
 * @see ActivityLifeCycleDispatcher
 * @see ActivityEventCallbackGroup
 */
public final class ActivityController {

    private static class Creator {
        public static final ActivityController INSTANCE = new ActivityController();
    }
    /**
     * the activity's view controller
     */
    private final ActivityViewController mViewController;
    /**
     * the activity's life cycle controller
     */
    private final ActivityLifeCycleDispatcher mLifeCycleDispatcher;
    /**
     * the activity's event listener controller
     */
    private final ActivityEventCallbackGroup mEventDispatcher;

    private final IActivityEventCallback mDefaultLastListener;
    private Bundle mSaveInstanceState;

    private ActivityController() {
        mViewController = new ActivityViewController();
        mLifeCycleDispatcher = new ActivityLifeCycleDispatcher();
        mEventDispatcher = new ActivityEventCallbackGroup();
        mDefaultLastListener = new ActivityEventAdapter() {
            @Override
            public boolean onBackPressed() {
                BaseScrapView v = mViewController.getCurrentView();
                if (v != null && v.onBackPressed()) {
                    return true;
                }
                return mViewController.onBackPressed();
            }
        };
        mEventDispatcher.setActivityLastEventListener(mDefaultLastListener);
    }

    /**
     * obtain the single instance of  ActivityController
     */
    public static ActivityController get() {
        return Creator.INSTANCE;
    }

    /**
     * reset the all event listener of the activity.
     */
    public void resetAllActivityEventListeners() {
        mEventDispatcher.clear();
        mEventDispatcher.setActivityLastEventListener(mDefaultLastListener);
    }

    /**
     * this will be called automatic is the one Activity.*
     */
    @CalledInternal
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public void attach(Activity activity, ViewGroup content, Bundle savedInstanceState) {
        mSaveInstanceState = savedInstanceState != null ? new Bundle(savedInstanceState) : null;
        mViewController.attachActivity(activity);
        mViewController.attachContainers(content);
        //if comes from save- State
        if(!getViewController().onRestoreInstanceState(mSaveInstanceState)){
            jumpToMainScrapViewIfNeed(activity);
        }
    }

    /**
     * jump to the main scrap view
     */
    private void jumpToMainScrapViewIfNeed(Activity activity) {
        if (activity == null) return;
        Properties prop = null;
        try {
            prop = Utils.loadRawConfig(activity, ScrapConstant.CONFIG_FILENAME);
        } catch (RuntimeException e) {
            //ignore
        }
        if (prop != null) {
            String classname = prop.getProperty(ScrapConstant.CONFIG_KEY_MAIN_SCRAP_VIEW);
            if (classname != null) {
                try {
                    String backStack = prop.getProperty(ScrapConstant.CONFIG_KEY_MAIN_SCRAP_VIEW_BACK_STACK);
                    BaseScrapView view = Reflector.from(Class.forName(classname)).constructor(Context.class)
                            .newInstance(activity);
                    if (!Boolean.valueOf(backStack)) {
                        jumpTo(view);
                    } else {
                        beginTransaction().addBackAsBottom(view).jump().commit();
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Get error while load main scrap view , classname = " + classname, e);
                }
            }
        }
    }

    /**
     * set the global loading view , this will be used if you call {@link BaseScrapView#setShowLoading(boolean)}
     */
    public void setLoadingView(View loading) {
        mViewController.setLoadingView(loading);
    }

    /**
     * get the bundle data of activity's save instance.
     * this comes from Activity_onCreate(savedInstanceState).
     *
     * @return the save instance data ,may be null if activity is finished.
     */
    public Bundle getSaveInstanceData() {
        return mSaveInstanceState;
    }

    /**
     * return the attached activity. may be null if the activity not attached or is finished
     */
    public Activity getActivity() {
        return mViewController.getActivity();
    }

    /**
     * get the life cycle dispatcher/controller
     */
    public ActivityLifeCycleDispatcher getLifeCycleDispatcher() {
        return mLifeCycleDispatcher;
    }

    /**
     * get the activity event listener group
     */
    public ActivityEventCallbackGroup getEventListenerGroup() {
        return mEventDispatcher;
    }

    /**
     * get the view controller of the one activity.
     */
    public ActivityViewController getViewController() {
        return mViewController;
    }

    /**
     * finish the activity which is attached into controller.
     *
     * @see ActivityViewController#finishActivity()
     */
    public void finishActivity() {
        mViewController.finishActivity();
    }

    //=================== about jump ================= //

    /**
     * jump to the target view with no data
     */
    public void jumpTo(BaseScrapView view) {
        jumpTo(view, null);
    }

    /**
     * jump to the target view with data.
     *
     * @param view the view to jump
     * @param data the data to carry.
     */
    public void jumpTo(BaseScrapView view, Bundle data) {
        mViewController.jumpTo(view, data);
    }

    /**
     * open the transaction of {@link ActivityViewController}, this is very useful.
     * here is the sample code: <p>
     * <pre> Transaction transaction = ActivityController.get().beginTransaction();
     *       transaction.cache(view).stackMode(Mode.ReplacePreviousAndClearAfter)
     *           .addBackAsTop().jump().commit();
     *  </pre></p>
     * the more to see {@link Transaction}
     *
     * @see Transaction
     */
    public Transaction beginTransaction() {
        return mViewController.beginTransaction();
    }

    // =============== other ====================//

    /**
     * set the animation executor which used to pass one {@link BaseScrapView} to another BaseScrapView
     *
     * @param executor the animate executor
     */
    public void setAnimateExecutor(AnimateExecutor executor) {
        mViewController.setAnimateExecutor(executor);
    }

}
