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
package org.heaven7.scrap.core.lifecycle;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.heaven7.java.base.anno.CalledInternal;
import com.heaven7.java.base.util.CopyOnWriteArray;

public final class ActivityLifeCycleDispatcher {

    private final CopyOnWriteArray<IActivityLifeCycleCallback> mCallbacks;

    public ActivityLifeCycleDispatcher() {
        mCallbacks = new CopyOnWriteArray<IActivityLifeCycleCallback>();
    }

    public void registerActivityLifeCycleCallback(IActivityLifeCycleCallback... callbacks) {
        CopyOnWriteArray<IActivityLifeCycleCallback> mCallbacks = this.mCallbacks;
        for (IActivityLifeCycleCallback callback : callbacks) {
            if (!mCallbacks.contains(callback))
                mCallbacks.add(callback);
        }
    }

    public void unregisterActivityLifeCycleCallback(IActivityLifeCycleCallback... callbacks) {
        CopyOnWriteArray<IActivityLifeCycleCallback> mCallbacks = this.mCallbacks;
        for (IActivityLifeCycleCallback callback : callbacks) {
            mCallbacks.remove(callback);
        }
    }

    public boolean containsActivityLifeCycleCallback(IActivityLifeCycleCallback callback) {
        return mCallbacks.contains(callback);
    }

    //==============================//
    @CalledInternal
    public void dispatchActivityOnCreate(Activity activity, Bundle savedInstanceState) {
        //activity.getWindow().getDecorView().getViewTreeObserver()
        final CopyOnWriteArray<IActivityLifeCycleCallback> listeners = mCallbacks;
        if (listeners.size() > 0) {
            CopyOnWriteArray.Access<IActivityLifeCycleCallback> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).onActivityCreate(activity, savedInstanceState);
                }
            } finally {
                listeners.end();
            }
        }
    }

    @CalledInternal
    public void dispatchActivityOnPostCreate(Activity activity, Bundle savedInstanceState) {

        final CopyOnWriteArray<IActivityLifeCycleCallback> listeners = mCallbacks;
        if (listeners.size() > 0) {
            CopyOnWriteArray.Access<IActivityLifeCycleCallback> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).onActivityPostCreate(activity, savedInstanceState);
                }
            } finally {
                listeners.end();
            }
        }
    }

    @CalledInternal
    public void dispatchActivityOnStart(Activity activity) {
        final CopyOnWriteArray<IActivityLifeCycleCallback> listeners = mCallbacks;
        if (listeners.size() > 0) {
            CopyOnWriteArray.Access<IActivityLifeCycleCallback> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).onActivityStart(activity);
                }
            } finally {
                listeners.end();
            }
        }
    }

    @CalledInternal
    public void dispatchActivityOnResume(Activity activity) {
        final CopyOnWriteArray<IActivityLifeCycleCallback> listeners = mCallbacks;
        if (listeners.size() > 0) {
            CopyOnWriteArray.Access<IActivityLifeCycleCallback> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).onActivityResume(activity);
                }
            } finally {
                listeners.end();
            }
        }
    }

    @CalledInternal
    public void dispatchActivityOnPause(Activity activity) {
        final CopyOnWriteArray<IActivityLifeCycleCallback> listeners = mCallbacks;
        if (listeners.size() > 0) {
            CopyOnWriteArray.Access<IActivityLifeCycleCallback> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).onActivityPause(activity);
                }
            } finally {
                listeners.end();
            }
        }
    }

    @CalledInternal
    public void dispatchActivityOnStop(Activity activity) {
        final CopyOnWriteArray<IActivityLifeCycleCallback> listeners = mCallbacks;
        if (listeners.size() > 0) {
            CopyOnWriteArray.Access<IActivityLifeCycleCallback> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).onActivityStop(activity);
                }
            } finally {
                listeners.end();
            }
        }
    }

    @CalledInternal
    public void dispatchActivityOnDestroy(Activity activity) {
        final CopyOnWriteArray<IActivityLifeCycleCallback> listeners = mCallbacks;
        if (listeners.size() > 0) {
            CopyOnWriteArray.Access<IActivityLifeCycleCallback> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).onActivityDestroy(activity);
                }
            } finally {
                listeners.end();
            }
        }
    }

    @CalledInternal
    public void dispatchActivityOnConfigurationChanged(Activity activity, Configuration newConfig) {
        final CopyOnWriteArray<IActivityLifeCycleCallback> listeners = mCallbacks;
        if (listeners.size() > 0) {
            CopyOnWriteArray.Access<IActivityLifeCycleCallback> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).onActivityConfigurationChanged(activity, newConfig);
                }
            } finally {
                listeners.end();
            }
        }
    }

    @CalledInternal
    public void dispatchActivityOnUserLeaveHint(Activity activity) {
        final CopyOnWriteArray<IActivityLifeCycleCallback> listeners = mCallbacks;
        if (listeners.size() > 0) {
            CopyOnWriteArray.Access<IActivityLifeCycleCallback> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).onActivityUserLeaveHint(activity);
                }
            } finally {
                listeners.end();
            }
        }
    }

    @CalledInternal
    public void dispatchActivityOnActivityResult(Activity activity,
                                                 int requestCode, int resultCode, Intent data) {
        final CopyOnWriteArray<IActivityLifeCycleCallback> listeners = mCallbacks;
        if (listeners.size() > 0) {
            CopyOnWriteArray.Access<IActivityLifeCycleCallback> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).onActivityResult(activity, requestCode, resultCode, data);
                }
            } finally {
                listeners.end();
            }
        }
    }

    @CalledInternal
    public void dispatchActivityOnActivityPermissionResult(Activity activity,
                                                           int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        final CopyOnWriteArray<IActivityLifeCycleCallback> listeners = mCallbacks;
        if (listeners.size() > 0) {
            CopyOnWriteArray.Access<IActivityLifeCycleCallback> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).onRequestPermissionsResult(activity, requestCode, permissions, grantResult);
                }
            } finally {
                listeners.end();
            }
        }
    }

    @CalledInternal
    public void dispatchActivityOnLowMemory(Activity activity) {
        final CopyOnWriteArray<IActivityLifeCycleCallback> listeners = mCallbacks;
        if (listeners.size() > 0) {
            CopyOnWriteArray.Access<IActivityLifeCycleCallback> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).onActivityLowMemory(activity);
                }
            } finally {
                listeners.end();
            }
        }
    }

    @CalledInternal
    public void dispatchActivityOnWindowFocusChanged(
            Activity activity, boolean hasFocus) {
        final CopyOnWriteArray<IActivityLifeCycleCallback> listeners = mCallbacks;
        if (listeners.size() > 0) {
            CopyOnWriteArray.Access<IActivityLifeCycleCallback> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).onActivityWindowFocusChanged(activity, hasFocus);
                }
            } finally {
                listeners.end();
            }
        }
    }

}
