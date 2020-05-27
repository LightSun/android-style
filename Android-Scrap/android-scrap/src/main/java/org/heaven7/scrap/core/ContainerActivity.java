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
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import org.heaven7.scrap.R;
import org.heaven7.scrap.core.event.ActivityEventCallbackGroup;

/**
 * the container activity for this 'one activity' framework.
 * @author heaven7
 */
public class ContainerActivity extends AppCompatActivity {
	
	private FrameLayout mFl_top;
	private FrameLayout mFl_content;
	private FrameLayout mFl_bottom;
	private FrameLayout mFl_loading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(!useSelfLayout()){
			setContentView(R.layout.activity_container);
			
			mFl_top = (FrameLayout) findViewById(R.id.fl_top);
			mFl_bottom = (FrameLayout) findViewById(R.id.fl_bottom);
			mFl_content = (FrameLayout) findViewById(R.id.fl_content);
			mFl_loading = (FrameLayout) findViewById(R.id.fl_loading);

			attachAndDispatchOnCreate(mFl_top, mFl_content, mFl_bottom,mFl_loading,savedInstanceState);
		}
	}
	/** this must called after the container(top,middle,and bottom) is found ! */
	protected void attachAndDispatchOnCreate(ViewGroup top,
			ViewGroup middle, ViewGroup bottom, ViewGroup loading, Bundle savedInstanceState) {
		ActivityController.get().attach(this, top, middle, bottom,loading, savedInstanceState);
		ActivityController.get().getLifeCycleDispatcher()
		              .dispatchActivityOnCreate(this, savedInstanceState);
	}
	/** @return true if you want to use your self Layout.not use the default. */
	protected boolean useSelfLayout(){
		return false;
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		ActivityController.get().getLifeCycleDispatcher()
	     .dispatchActivityOnWindowFocusChanged(this, hasFocus);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		ActivityController.get().getLifeCycleDispatcher()
		     .dispatchActivityOnPostCreate(this, savedInstanceState);
	}
	@Override
	protected void onStart() {
		super.onStart();
		ActivityController.get().getLifeCycleDispatcher()
		     .dispatchActivityOnStart(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		ActivityController.get().getLifeCycleDispatcher()
	     .dispatchActivityOnResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		ActivityController.get().getLifeCycleDispatcher()
	     .dispatchActivityOnPause(this);
	}
	@Override
	protected void onStop() {
		super.onStop();
		ActivityController.get().getLifeCycleDispatcher()
	     .dispatchActivityOnStop(this);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityController.get().getLifeCycleDispatcher()
	     .dispatchActivityOnDestroy(this);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		ActivityController.get().getLifeCycleDispatcher()
	     .dispatchActivityOnConfigurationChanged(this, newConfig);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		ActivityController.get().getLifeCycleDispatcher()
	     .dispatchActivityOnSaveInstanceState(this, outState);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		ActivityController.get().getLifeCycleDispatcher()
	     .dispatchActivityOnRestoreInstanceState(this, savedInstanceState);
	}
	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		ActivityController.get().getLifeCycleDispatcher()
	     .dispatchActivityOnUserLeaveHint(this); 
	}
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		ActivityController.get().getLifeCycleDispatcher()
	     .dispatchActivityOnLowMemory(this); 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ActivityController.get().getLifeCycleDispatcher()
	     .dispatchActivityOnActivityResult(this, requestCode, resultCode, data);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		ActivityEventCallbackGroup dispatcher = ActivityController.get().getEventListenerGroup();
		if(dispatcher.onKeyDown(keyCode, event)){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		ActivityEventCallbackGroup dispatcher = ActivityController.get().getEventListenerGroup();
		if(dispatcher.onKeyUp(keyCode, event)){
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public boolean onKeyShortcut(int keyCode, KeyEvent event) {
		ActivityEventCallbackGroup dispatcher = ActivityController.get().getEventListenerGroup();
		if(dispatcher.onKeyShortcut(keyCode, event)){
			return true;
		}
		return super.onKeyShortcut(keyCode, event);
	}
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		ActivityEventCallbackGroup dispatcher = ActivityController.get().getEventListenerGroup();
		if(dispatcher.onKeyLongPress(keyCode, event)){
			return true;
		}
		return super.onKeyLongPress(keyCode, event);
	}
	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		ActivityEventCallbackGroup dispatcher = ActivityController.get().getEventListenerGroup();
		if(dispatcher.onKeyMultiple(keyCode, event)){
			return true;
		}
		return super.onKeyMultiple(keyCode, repeatCount, event);
	}
	@Override
	public void onBackPressed() {
		ActivityEventCallbackGroup dispatcher = ActivityController.get().getEventListenerGroup();
		if(dispatcher.onBackPressed()){
			return;
		}
		super.onBackPressed();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		ActivityEventCallbackGroup dispatcher = ActivityController.get().getEventListenerGroup();
		if(dispatcher.onTouchEvent(event)){
			return true;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void finish() {
		super.finish();
		ActivityController.get().attach(null,null,null,null,null,null);
	}
}
