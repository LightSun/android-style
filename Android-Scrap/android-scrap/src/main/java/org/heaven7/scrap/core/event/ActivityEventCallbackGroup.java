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
package org.heaven7.scrap.core.event;

import android.view.KeyEvent;
import android.view.MotionEvent;

import com.heaven7.java.base.anno.CalledInternal;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * the group event callback of activity.
 * if one callback return true, the other callback after it will not receive the event.
 *
 * @author heaven7
 * @see ActivityEventCallbackGroup#registerActivityEventListener(IActivityEventCallback...)
 * @see #unregisterActivityEventListener(IActivityEventCallback...)
 */
public final class ActivityEventCallbackGroup implements IActivityEventCallback {

    private final List<IActivityEventCallback> mListeners;
    private IActivityEventCallback mLastEventListener;

    public ActivityEventCallbackGroup() {
        mListeners = new CopyOnWriteArrayList<IActivityEventCallback>();
    }

    public void clear() {
        mListeners.clear();
    }

    @CalledInternal
    public void setActivityLastEventListener(IActivityEventCallback l) {
        mLastEventListener = l;
    }

    /**
     * register a activity event listener.
     *
     * @param ls the listeners want to register.
     */
    public void registerActivityEventListener(IActivityEventCallback... ls) {
        if (ls == null || ls.length == 0)
            return;
        final List<IActivityEventCallback> mListeners = this.mListeners;
        for (int i = 0, size = ls.length; i < size; i++) {
            mListeners.add(ls[i]);
        }
    }

    /**
     * unregister a activity event listener.
     *
     * @param ls the listeners want to unregister.
     */
    public void unregisterActivityEventListener(IActivityEventCallback... ls) {
        if (ls == null || ls.length == 0)
            return;
        final List<IActivityEventCallback> mListeners = this.mListeners;
        for (IActivityEventCallback l : ls) {
            mListeners.remove(l);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        final List<IActivityEventCallback> mListeners = this.mListeners;
        for (int i = 0, size = mListeners.size(); i < size; i++) {
            if (mListeners.get(i).onKeyDown(keyCode, event)) {
                return true;
            }
        }
        if (mLastEventListener != null && mLastEventListener.onKeyDown(keyCode, event)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        final List<IActivityEventCallback> mListeners = this.mListeners;
        for (int i = 0, size = mListeners.size(); i < size; i++) {
            if (mListeners.get(i).onKeyUp(keyCode, event)) {
                return true;
            }
        }
        if (mLastEventListener != null && mLastEventListener.onKeyUp(keyCode, event)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        final List<IActivityEventCallback> mListeners = this.mListeners;
        for (int i = 0, size = mListeners.size(); i < size; i++) {
            if (mListeners.get(i).onKeyShortcut(keyCode, event)) {
                return true;
            }
        }
        if (mLastEventListener != null && mLastEventListener.onKeyShortcut(keyCode, event)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        final List<IActivityEventCallback> mListeners = this.mListeners;
        for (int i = 0, size = mListeners.size(); i < size; i++) {
            if (mListeners.get(i).onKeyLongPress(keyCode, event)) {
                return true;
            }
        }
        if (mLastEventListener != null && mLastEventListener.onKeyLongPress(keyCode, event)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyMultiple(int keyCode, KeyEvent event) {
        final List<IActivityEventCallback> mListeners = this.mListeners;
        for (int i = 0, size = mListeners.size(); i < size; i++) {
            if (mListeners.get(i).onKeyMultiple(keyCode, event)) {
                return true;
            }
        }
        if (mLastEventListener != null && mLastEventListener.onKeyMultiple(keyCode, event)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onBackPressed() {
        final List<IActivityEventCallback> mListeners = this.mListeners;
        for (int i = 0, size = mListeners.size(); i < size; i++) {
            if (mListeners.get(i).onBackPressed()) {
                return true;
            }
        }
        if (mLastEventListener != null && mLastEventListener.onBackPressed()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final List<IActivityEventCallback> mListeners = this.mListeners;
        for (int i = 0, size = mListeners.size(); i < size; i++) {
            if (mListeners.get(i).onTouchEvent(event)) {
                return true;
            }
        }
        if (mLastEventListener != null && mLastEventListener.onTouchEvent(event)) {
            return true;
        }
        return false;
    }

}
