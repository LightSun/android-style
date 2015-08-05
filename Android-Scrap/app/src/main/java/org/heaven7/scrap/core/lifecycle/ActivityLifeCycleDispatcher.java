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

import org.heaven7.scrap.annotation.CalledByFramework;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ActivityLifeCycleDispatcher{

	private final List<IActivityLifeCycleCallback> mCallbacks ;
	
	public ActivityLifeCycleDispatcher() {
		mCallbacks = new CopyOnWriteArrayList<IActivityLifeCycleCallback>();
	}
	
	public void registerActivityLifeCycleCallback(IActivityLifeCycleCallback...callbacks){
		List<IActivityLifeCycleCallback> mCallbacks = this.mCallbacks;
		for(IActivityLifeCycleCallback callback : callbacks){
			mCallbacks.add(callback);
		}
	}
	public void unregisterActivityLifeCycleCallback(IActivityLifeCycleCallback...callbacks){
		List<IActivityLifeCycleCallback> mCallbacks = this.mCallbacks;
		for(IActivityLifeCycleCallback callback : callbacks){
			mCallbacks.remove(callback);
		}
	}

	public boolean containsActivityLifeCycleCallback(IActivityLifeCycleCallback callback){
		return mCallbacks.contains(callback);
	}
	
	//==============================//
	@CalledByFramework
	public void dispatchActivityOnCreate(Activity activity,Bundle savedInstanceState){
		List<IActivityLifeCycleCallback> mCallbacks = this.mCallbacks;
		for(IActivityLifeCycleCallback callback : mCallbacks){
			callback.onActivityCreate(activity, savedInstanceState);
		}
	}
	@CalledByFramework
	public void dispatchActivityOnPostCreate(Activity activity,Bundle savedInstanceState){
		List<IActivityLifeCycleCallback> mCallbacks = this.mCallbacks;
		for(IActivityLifeCycleCallback callback : mCallbacks){
			callback.onActivityPostCreate(activity, savedInstanceState);
		}
	}
	@CalledByFramework
	public void dispatchActivityOnStart(Activity activity) {
		List<IActivityLifeCycleCallback> mCallbacks = this.mCallbacks;
		for(IActivityLifeCycleCallback callback : mCallbacks){
			callback.onActivityStart(activity);
		}
	}
	@CalledByFramework
	public void dispatchActivityOnResume(Activity activity) {
		List<IActivityLifeCycleCallback> mCallbacks = this.mCallbacks;
		for(IActivityLifeCycleCallback callback : mCallbacks){
			callback.onActivityResume(activity);
		}
	}
	@CalledByFramework
	public void dispatchActivityOnPause(Activity activity) {
		List<IActivityLifeCycleCallback> mCallbacks = this.mCallbacks;
		for(IActivityLifeCycleCallback callback : mCallbacks){
			callback.onActivityPause(activity);
		}
	}
	@CalledByFramework
	public void dispatchActivityOnStop(Activity activity) {
		List<IActivityLifeCycleCallback> mCallbacks = this.mCallbacks;
		for(IActivityLifeCycleCallback callback : mCallbacks){
			callback.onActivityStop(activity);
		}
	}
	@CalledByFramework
	public void dispatchActivityOnDestroy(Activity activity) {
		List<IActivityLifeCycleCallback> mCallbacks = this.mCallbacks;
		for(IActivityLifeCycleCallback callback : mCallbacks){
			callback.onActivityDestroy(activity);
		}
	}
	@CalledByFramework
	public void dispatchActivityOnConfigurationChanged(Activity activity, Configuration newConfig) {
		List<IActivityLifeCycleCallback> mCallbacks = this.mCallbacks;
		for(IActivityLifeCycleCallback callback : mCallbacks){
			callback.onActivityConfigurationChanged(activity,newConfig);
		}
	}
	@CalledByFramework
	public void dispatchActivityOnSaveInstanceState(Activity activity,
			Bundle outState) {
		List<IActivityLifeCycleCallback> mCallbacks = this.mCallbacks;
		for(IActivityLifeCycleCallback callback : mCallbacks){
			callback.onActivitySaveInstanceState(activity,outState);
		}
	}
	@CalledByFramework
	public void dispatchActivityOnRestoreInstanceState(
			Activity activity, Bundle savedInstanceState) {
		List<IActivityLifeCycleCallback> mCallbacks = this.mCallbacks;
		for(IActivityLifeCycleCallback callback : mCallbacks){
			callback.onActivityRestoreInstanceState(activity,savedInstanceState);
		}
	}
	@CalledByFramework
	public void dispatchActivityOnUserLeaveHint(Activity activity) {
		List<IActivityLifeCycleCallback> mCallbacks = this.mCallbacks;
		for(IActivityLifeCycleCallback callback : mCallbacks){
			callback.onActivityUserLeaveHint(activity);
		}
	}
	@CalledByFramework
	public void dispatchActivityOnActivityResult(Activity activity,
			int requestCode, int resultCode, Intent data) {
		List<IActivityLifeCycleCallback> mCallbacks = this.mCallbacks;
		for(IActivityLifeCycleCallback callback : mCallbacks){
			callback.OnActivityResult(activity,requestCode,resultCode,data);
		}
	}
	@CalledByFramework
	public void dispatchActivityOnLowMemory(Activity activity) {
		List<IActivityLifeCycleCallback> mCallbacks = this.mCallbacks;
		for(IActivityLifeCycleCallback callback : mCallbacks){
			callback.OnActivityLowMemory(activity);
		}
	}

	@CalledByFramework
	public void dispatchActivityOnWindowFocusChanged(
			Activity activity, boolean hasFocus) {
		List<IActivityLifeCycleCallback> mCallbacks = this.mCallbacks;
		for(IActivityLifeCycleCallback callback : mCallbacks){
			callback.OnActivityWindowFocusChanged(activity,hasFocus);
		}
	}
	
}
