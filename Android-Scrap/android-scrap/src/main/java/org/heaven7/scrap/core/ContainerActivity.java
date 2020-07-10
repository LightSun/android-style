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
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.heaven7.core.util.AppUtils;

import org.heaven7.scrap.core.delegate.AbstractUiDelegate;
import org.heaven7.scrap.core.delegate.SingleActivityUiDelegate;
import org.heaven7.scrap.core.event.ActivityEventCallbackGroup;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * the container activity.
 *
 * @author heaven7
 */
public class ContainerActivity extends AppCompatActivity {

    private AbstractUiDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
         * 修复部分 Android 8.0 手机在TargetSDK 大于 26 时，在透明主题时指定 Activity 方向时崩溃的问题
         */
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            fixOrientation();
        }
        super.onCreate(savedInstanceState);

        String cn = getIntent().getStringExtra(ScrapConstant.KEY_UI_DELEGATE);
        if(cn == null){
        	mDelegate = new SingleActivityUiDelegate();
		}else {
			try {
				mDelegate = (AbstractUiDelegate) Class.forName(cn).newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
        mDelegate.setActivity(this);
        mDelegate.onPreSetContentView();
        mDelegate.clearAllFragments();
        setContentView(mDelegate.getLayoutId());
        mDelegate.setStatusBar();

        //on initialize
        mDelegate.onInitialize(getIntent(), savedInstanceState);
        //on-create
		mDelegate.getLifeCycleDispatcher().dispatchActivityOnCreate(this, savedInstanceState);
    }
    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 清除所有已存在的 Fragment 防止因重建 Activity 时，前 Fragment 没有销毁和重新复用导致界面重复显示
     * 如果有自己实现 Fragment 的复用，请复写此方法并不实现内容
     */
    protected void clearAllFragmentExistBeforeCreate() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments.size() == 0) return;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : fragments) {
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.commitNow();
    }
    /**
     * 判断当前主题是否是透明悬浮
     */
    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

	@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mDelegate.onInitialize(getIntent(), null);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        mDelegate.getLifeCycleDispatcher()
                .dispatchActivityOnWindowFocusChanged(this, hasFocus);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDelegate.getLifeCycleDispatcher()
                .dispatchActivityOnPostCreate(this, savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDelegate.getLifeCycleDispatcher()
                .dispatchActivityOnStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDelegate.getLifeCycleDispatcher()
                .dispatchActivityOnResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDelegate.getLifeCycleDispatcher()
                .dispatchActivityOnPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDelegate.getLifeCycleDispatcher()
                .dispatchActivityOnStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDelegate.onDestroy();
        mDelegate.getLifeCycleDispatcher().dispatchActivityOnDestroy(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDelegate.getLifeCycleDispatcher()
                .dispatchActivityOnConfigurationChanged(this, newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDelegate.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mDelegate.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        mDelegate.getLifeCycleDispatcher()
                .dispatchActivityOnUserLeaveHint(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mDelegate.getLifeCycleDispatcher()
                .dispatchActivityOnLowMemory(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mDelegate.getLifeCycleDispatcher()
                .dispatchActivityOnActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ActivityEventCallbackGroup dispatcher = mDelegate.getEventGroup();
        if (dispatcher.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        ActivityEventCallbackGroup dispatcher = mDelegate.getEventGroup();
        if (dispatcher.onKeyUp(keyCode, event)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        ActivityEventCallbackGroup dispatcher = mDelegate.getEventGroup();
        if (dispatcher.onKeyShortcut(keyCode, event)) {
            return true;
        }
        return super.onKeyShortcut(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        ActivityEventCallbackGroup dispatcher = mDelegate.getEventGroup();
        if (dispatcher.onKeyLongPress(keyCode, event)) {
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        ActivityEventCallbackGroup dispatcher = mDelegate.getEventGroup();
        if (dispatcher.onKeyMultiple(keyCode, event)) {
            return true;
        }
        return super.onKeyMultiple(keyCode, repeatCount, event);
    }

    @Override
    public void onBackPressed() {
    	if(mDelegate.onBackPressed()){
    		return;
		}
        ActivityEventCallbackGroup dispatcher = mDelegate.getEventGroup();
        if (dispatcher.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ActivityEventCallbackGroup dispatcher = mDelegate.getEventGroup();
        if (dispatcher.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		mDelegate.getLifeCycleDispatcher().dispatchActivityOnActivityPermissionResult(this,
				requestCode, permissions, grantResults);
	}

	@Override
    public void finish() {
        super.finish();
        mDelegate.onFinish();
    }
}
