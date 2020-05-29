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

import android.os.Bundle;

import org.heaven7.scrap.core.anim.AnimateExecutor;

import java.util.NoSuchElementException;

/**
 * the Transaction object hold a group operation such as: {@link #cache(String, BaseScrapView)},
 *  {@link #addBackAsTop(BaseScrapView)},{@link #addBackAfterAnother(BaseScrapView, BaseScrapView)},
 *  {@link #addBackBeforeAnother(BaseScrapView, BaseScrapView)} , {@link #jump(BaseScrapView)},{@link #stackMode(StackMode)}
 *  and use {@link #commit()} to commit it at last. here is the sample code:<p>
 *  <pre> Transaction transaction = ActivityController.get().beginTransaction();
 *  transaction.cache(view).stackMode(StackMode.ReplacePreviousAndClearAfter)
 *     addBackAsTop().jump().commit();
 *  </pre></p>
 * @author heaven7
 *
 */
public final class Transaction {

	/**
	 * the recorded  BaseScrapView of your last operation. which will be null after you call {@link #commit()}
	 * */
	private BaseScrapView mLastView;
	/**
	 * indicate whether or not jump to the target view when you called {@link #commit()}
	 */
	private boolean mNeedJump;

	/** the cache helper to help you cache  */
	private final CacheHelper mHelper;

	/** the jumper which can used to jump to target BaseScrapView */
	private final IJumper mJumper;

	private AnimateExecutor mAnimateExecutor;
	private Bundle mBundle;

	/**
	 * the jumper which can used to jump to the target BaseScrapView
	 * @author heaven7
	 *
	 */
	protected interface IJumper {
		/**
		 * jump to the target view with data and animate executor
		 * @param target  the scrap View to jump .
		 * @param data    the data to carry
		 * @param executor  the animate executor to perform this jump
		 */
		void jumpTo(BaseScrapView target, Bundle data, AnimateExecutor executor);
	}

	/**
	 * create a transaction by {@link CacheHelper} and {@link IJumper}
	 */
	/*public*/ Transaction(CacheHelper mHelper, IJumper jumper) {
		super();
		this.mJumper = jumper;
		this.mHelper = mHelper;
	}

	/** change the behaviour of the default back stack.  after call {@link #commit()} the mode will restore.
	 * @param mode which mode you want to. default is {@link StackMode#ClearPrevious}
	 * @see StackMode */
	public Transaction stackMode(StackMode mode){
		mHelper.mViewStack.setStackMode(mode);
		return this;
	}

	/***
	 * remove the mapping  of key in the cache.
	 * @param key the key of scrap-view
	 * @return  the mapping value
	 */
	public BaseScrapView removeCache(String key){
		return mHelper.removeCacheView(key);
	}

	/** set the animate executor to perform this jump, the animate executor will only be used
	 * if then you call ...jump().commit(). */
	public Transaction animateExecutor(AnimateExecutor executor){
		this.mAnimateExecutor = executor;
		return this;
	}
	/** set the extra data to carry to the target scrap view.the extra data will only be used
	 *  if then you call ...jump().commit(). */
	public Transaction arguments(Bundle data){
		this.mBundle = data;
		return this;
	}

	/**
	 * @see CacheHelper#cache(String, BaseScrapView)
	 */
	public Transaction cache(String key, BaseScrapView view) {
		mHelper.cache(key, view);
		if(mLastView != view)
		     mLastView = view;
		return this;
	}

	/**
	 * cache the target view and the key is view.getClass().getName().
	 * @see CacheHelper#cache(BaseScrapView)
	 */
	public Transaction cache(BaseScrapView view) {
		mHelper.cache(view);
		if(mLastView != view)
		     mLastView = view;
		return this;
	}

	private void checkLastViewExist() {
		if (mLastView == null) {
			throw new IllegalStateException("the lastview can't be null. you can call " +
					"addBack(...)/cache(...)/jump(...) to extra record to lastview!");
		}
	}
	/**
	 * add the lastview(from last operation) as target view to back stack's top.
	 * @see #addBackAsTop(BaseScrapView)
	 */
	public Transaction addBackAsTop() {
		checkLastViewExist();
		return addBackAsTop(mLastView);
	}

	/**
	 * add the lastview(from last operation) as the target view to back stack and position it as the bottom of stack.
	 */
	public Transaction addBackAsBottom() {
		checkLastViewExist();
		return addBackAsBottom(mLastView);
	}
	/**
	 * add the target view to back stack  and position it as bottom .
	 * @param target the target view
	 */
	public Transaction addBackAsBottom(BaseScrapView target) {
		if (target == null)
			throw new NullPointerException();
		if(mLastView != target)
		     mLastView = target;
		mHelper.addToStackBottom(target);
		return this;
	}

	/**
	 * add the target view to back stack's top .
	 * @param target  the target view
	 */
	public Transaction addBackAsTop(BaseScrapView target) {
		if (target == null)
			throw new NullPointerException();
		if(mLastView != target)
		     mLastView = target;
		mHelper.addToStackTop(target);
		return this;
	}
	/**
	 * add the lastview(from last operation) as the target view to back stack and befor the another view .
	 * @param anotherView the another view
	 *  @throws NoSuchElementException if the anotherView  can't find.
	 */
	public Transaction addBackBeforeAnother(BaseScrapView anotherView) {
		checkLastViewExist();
		return addBackBeforeAnother(anotherView, mLastView);
	}
	/**
	 * add the target view to back stack and befor the the another view.
	 * @param anotherView the another view
	 * @param targetView the target view  add to the back stack.
	 * @throws NoSuchElementException if the anotherView  can't find.
	 */
	public Transaction addBackBeforeAnother(BaseScrapView anotherView, BaseScrapView targetView) {
		if (targetView == null)
			throw new NullPointerException();
		if(mLastView != targetView)
		     mLastView = targetView;
		mHelper.addToStack(anotherView,targetView,true);
		return this;
	}
	/**
	 * add the lastview(from last operation) as the target view to back stack and  after the another view.
	 * @param anotherView the another view
	 * @throws NoSuchElementException if the anotherView  can't find.
	 */
	public Transaction addBackAfterAnother(BaseScrapView anotherView) {
		checkLastViewExist();
		return addBackAfterAnother(anotherView, mLastView);
	}
	/**
	 * add the target view to back stack and  after the another view.
	 * @param another the another view
	 * @param target the target view to add back stack.
	 * @throws NoSuchElementException if the anotherView  can't find.
	 */
	public Transaction addBackAfterAnother(BaseScrapView another, BaseScrapView target) {
		if (target == null)
			throw new NullPointerException();
		if(mLastView != target)
		     mLastView = target;
		mHelper.addToStack(another,target,false);
		return this;
	}

	/**
	 * add the target view to back stack and  after the referencedView.
	 * @param another the another class of view
	 * @param target the taget view to add back stack.
	 * @throws NoSuchElementException if the anotherView of another class can't find.
	 */
	public Transaction addBackAfterAnother(Class<? extends BaseScrapView> another, BaseScrapView target) {
		if (target == null)
			throw new NullPointerException();
		if(mLastView != target)
		     mLastView = target;
		mHelper.addToStack(another,target,false);
		return this;
	}
	/**
	 * add the target view to back stack and  after the another view.
	 * @param another the another class of view
	 * @param target the target view to add back stack.
	 *  @throws NoSuchElementException if the anotherView  of another class can't find.
	 */
	public Transaction addBackBeforeAnother(Class<? extends BaseScrapView> another, BaseScrapView target) {
		if (target == null)
			throw new NullPointerException();
		if(mLastView != target)
		     mLastView = target;
		mHelper.addToStack(another,target,true);
		return this;
	}

	/**
	 * make the lastview(from last operation) as the target view. and will jump to it.
	 */
	public Transaction jump() {
		checkLastViewExist();
		return jump(mLastView);
	}

	/**
	 * jump to the target BaseScrapView's page .
	 * @param target  the target view
	 */
	public Transaction jump(BaseScrapView target) {
		if (target == null)
			throw new NullPointerException();
		if(mLastView != target)
		     mLastView = target;
		mNeedJump = true;
		return this;
	}

	/**
	 *  commit all operation ,you must call this or else my cause some problem.
	 */
	public void commit() {
		checkLastViewExist();
		mHelper.resetStackSetting();
		if (mNeedJump) {
			mNeedJump = false;
			mJumper.jumpTo(mLastView,mBundle,mAnimateExecutor);
		}
		mLastView = null;
		mAnimateExecutor = null;
		mBundle = null;
	}
}
