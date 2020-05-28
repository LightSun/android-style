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

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.heaven7.scrap.core.delegate.AbstractUiDelegate;
import org.heaven7.scrap.core.delegate.SingleActivityUiDelegate;
import org.heaven7.scrap.core.event.ActivityEventCallbackGroup;

/**
 * the container activity.
 *
 * @author heaven7
 */
public class ContainerActivity extends AppCompatActivity {

    private AbstractUiDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        setContentView(mDelegate.getLayoutId());
        mDelegate.setActivity(this);

        //on initialize
        mDelegate.onInitialize(getIntent(), savedInstanceState);
        //on-create
		mDelegate.getLifeCycleDispatcher().dispatchActivityOnCreate(this, savedInstanceState);
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
        mDelegate.getLifeCycleDispatcher()
                .dispatchActivityOnSaveInstanceState(this, outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mDelegate.getLifeCycleDispatcher()
                .dispatchActivityOnRestoreInstanceState(this, savedInstanceState);
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
