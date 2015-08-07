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
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import org.heaven7.scrap.annotation.CalledByFramework;
import org.heaven7.scrap.core.event.IActivityEventCallback;
import org.heaven7.scrap.core.lifecycle.ActivityLifeCycleAdapter;
import org.heaven7.scrap.core.lifecycle.IActivityLifeCycleCallback;
import org.heaven7.scrap.util.ScrapHelper;
import org.heaven7.scrap.util.Toaster;

/**
 * this is a base view for 'the scrap view'. it help you to fast use.
 * the  BaseScrapView contains 3 views( Top, Middle, and bottom) as the three scrap.
 * offen subclass must override {@link #getBottomLayoutId()}, {@link #getMiddleLayoutId()}, {@link #getTopLayoutId()}
 * if you want different , also you can override {@link #getTopView()}, {@link #getMiddleView()}, {@link #getBottomView()}
 * <p> if you want to jump to a child of BaseScrapView,please use {@link ScrapHelper}, it has some useful methods.
 *  </p>
 *  <li> if you want to replace view dynamic.please use {@link #replaceView(View, ScrapPosition)}</li>
 * @author heaven7
 * @see  ActivityViewController
 * @see  ActivityController
 */
public abstract class BaseScrapView {
	
	private Context mContext;
	private LayoutInflater mInflater;
	/** the bundle data if you want to pass data from one BaseScrapView to another */
	private Bundle mBundle;
	/** a toast wrapper */
	private Toaster mToaster;
	/**
	 * a view helper to help you fast call some  methods.
	 * @see  ViewHelper
	 */
	private ViewHelper mViewHelper;
	/**
	 * the processor of activity's view
	 */
	private IActivityViewProcessor mViewProcessor;
	/** the default back event processor it will be autonomic set by framework */
	private IBackEventProcessor mDefaultBackEventProcessor;

	/** the callback used to same as the Activity's common life cycle */
	private IActivityLifeCycleCallback mLifecycleCallback;

	/** when set to true , this ScrapView will be stack.
	 * but if it can be replaced or cleared by another same classname of this.  */
	private boolean mInBackStack;

	/**
	 */
	public BaseScrapView(Context mContext) {
		super();
		this.mToaster = new Toaster(mContext);
		this.mContext = mContext;
		this.mInflater = LayoutInflater.from(mContext);
		onPostInit();
	}

	/** the automatic callbed to do the final init.
	 * if subclass want to init other please doing it here! */
	protected void onPostInit() {

	}

	/**
	 * automatic called by framework,when this view is prepared to attach.
	 */
	@CalledByFramework("when this abstract view is prepared to attach and before attached!")
	/*public*/ void registerActivityLifeCycleCallbacks() {
		if(mLifecycleCallback == null) {
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
	@CalledByFramework("when this abstract view is prepared to detach and before detached!")
	/*public*/ void unregisterActivityLifeCycleCallbacks(){
		if(mLifecycleCallback != null) {
			ScrapHelper.unregisterActivityLifeCycleCallback(mLifecycleCallback);
		}
	}

	/*package */ void setViewHelper(ViewHelper vp){
		this.mViewHelper = vp;
	}
	public void setContext(Context ctx){
		this.mContext = ctx;
	}
	public Context getContext(){
		return mContext;
	}
	public LayoutInflater getLayoutInflater(){
		return mInflater;
	}
	
	public void setBundle(Bundle data){
		this.mBundle = data;
	}
	
	public Bundle getBundle() {
		return mBundle;
	}
	
	/** show the toast with the msg. */
	public void showToast(String msg){
		mToaster.show(msg);
	}
	/** show the toast with the msg of resId. */
	public void showToast(int resId){
		mToaster.show(resId);
	}
	/** get the ViewHelper to help use some good method.
	 * @see ViewHelper*/
	public ViewHelper getViewHelper(){
		return mViewHelper;
	}
	/** get the toast warpper */
	public Toaster getToaster(){
		return mToaster;
	}

	@CalledByFramework("set the activity's view processor")
	/*package*/ void setActivityViewProcessor(IActivityViewProcessor processor) {
		this.mViewProcessor = processor;
	}

	/** same as {@link View#getResources()} */
	public Resources getResources(){
		return getContext().getResources();
	}

	public boolean isInBackStack() {
		return mInBackStack;
	}
	/** set whether or not in back stack  ,often called by framework*/
	@CalledByFramework("often")
	/*public*/ void setInBackStack(boolean mInBackStack) {
		this.mInBackStack = mInBackStack;
	}

	/***
	 *  set the default back event processor.
	 * @param processor
	 */
	public void setDefaultBackEventProcessor(IBackEventProcessor processor) {
       this.mDefaultBackEventProcessor = processor;
	}
	
	//==================================//

	/** replace the view of this which is indicate by the scrap.
	 *  @param view  the view to replace
	 *  @param  scrap  the position of the view
	 */
	protected void replaceView(View view,ScrapPosition scrap){
		if(view == null)
			throw new NullPointerException("view cann't be null");
        mViewProcessor.replaceView(view, scrap);
	}
	@CalledByFramework
	public  View getTopView(){
		if(getTopLayoutId() == 0)
			return null;
		return mInflater.inflate(getTopLayoutId(), null);
	}
	@CalledByFramework
	public  View getMiddleView(){
		if(getMiddleLayoutId() == 0)
			return null;
		return mInflater.inflate(getMiddleLayoutId(), null);
	}
	@CalledByFramework
	public  View getBottomView(){
		if(getBottomLayoutId() == 0)
			return null;
		return mInflater.inflate(getBottomLayoutId(), null);
	}
	/** get the layout id of the top . called by {@link #getTopView()}*/
	protected abstract int getTopLayoutId();
	
	/** get the layout id of the middle .called by {@link #getMiddleView()}*/
	protected abstract int getMiddleLayoutId();
	
	/** get the layout id of the bottom . called by {@link #getBottomView()}*/
	protected abstract int getBottomLayoutId();

	//===========life cycle ===================//
	
	/**
	 * when the top/middle/bottom hide it's visibility,this will be called.
	 * @param position  which scrap of activity 
	 * @see ActivityViewController#setVisibility(ScrapPosition, boolean)
	 * @see ActivityViewController#toogleVisibility(ScrapPosition)
	 */
	@CalledByFramework
	protected void onHide(ScrapPosition position){
		
	}
	/**
	 * when the top/middle/bottom show it's visibility,this will be called.
	 * @param position  which scrap of activity 
	 * @see ActivityViewController#setVisibility(ScrapPosition, boolean)
	 * @see ActivityViewController#toogleVisibility(ScrapPosition)
	 */
	@CalledByFramework
	protected void onShow(ScrapPosition position){
		
	}
	/**
	 * when this view is attached done to the activity. this will be called.
	 */
	@CalledByFramework
	protected void onAttach() {
		//ActivityController.get().getLifeCycleDispatcher().
	}
	/**
	 * when this view is detach done to the activity. this will be called.
	 * after call this, the context will set to null to avoid memory leak! 
	 */
	@CalledByFramework
	protected void onDetach() {
		
	}

	//======================  life cycle from activity   ========================//
	/*
	 note:  if activity is created and attached. some method will not called.such as: onActivityCreate()
	 */

	/**
	 * comes from {@link android.app.Activity#onCreate(Bundle)}
	 * @param saveInstanceState
	 */
	protected void onActivityCreate(Bundle saveInstanceState) {
	}
	/**
	 * comes from {@link android.app.Activity#onPostCreate(Bundle)}
	 * @param saveInstanceState
	 */
	protected void onActivityPostCreate(Bundle saveInstanceState){

	}
	/**
	 * comes from {@link Activity#onStart()}
	 */
	protected void onActivityStart() {
	}
	/**
	 * comes from {@link Activity#onResume()}
	 */
	protected void onActivityResume() {
	}
	/**
	 * comes from {@link Activity#onPause()}
	 */
	protected void onActivityPause() {
	}
	/**
	 * comes from {@link Activity#onStop()}
	 */
	protected void onActivityStop() {
	}
	/**
	 * comes from {@link Activity#onDestroy()}
	 */
	protected void onActivityDestroy() {
	}

	/**
	 * handle the back event if you don't consume the back event previous.
	 * <li> default this called before {@link ActivityViewController#onBackPressed()}.so
	 * if this onBackPressed() return true. The ActivityViewController will not receive this back event.
	 * @return  true to consume the back event
	 * @see org.heaven7.scrap.core.event.ActivityEventCallbackGroup#registerActivityEventListener(IActivityEventCallback...)
	 * @see  IActivityEventCallback#onBackPressed()
	 */
	protected boolean onBackPressed() {
		return mDefaultBackEventProcessor!=null? mDefaultBackEventProcessor.handleBackEvent():false;
	}

}
