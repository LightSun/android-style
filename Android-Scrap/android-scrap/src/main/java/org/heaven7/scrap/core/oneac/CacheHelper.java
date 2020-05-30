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

import android.content.Context;
import android.os.Bundle;

import com.heaven7.java.base.util.Reflector;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * the cache helper for internal use.
 * the view stack only cache one instance of the same class name which is a child of BaseScrapView.
 * if {@link ExpandArrayList#getMode()} != {@link StackMode#Normal}
 * <li> you can use {@link Transaction#stackMode(StackMode)} to change the default behavior. and the {@link Transaction#commit()} will restore mode
 * to default({@link StackMode#ClearPrevious}).
 * @see Transaction#stackMode(StackMode)
 * @see Transaction#commit()
 * @author heaven7
 */
/* package */ final class CacheHelper {

	private static final String KEY_STACK_PREFIX = "stack_";
	private static final String KEY_CACHE_PREFIX = "cache_";
	private static final String KEY_SIZE  = "size_";
	private static final String KEY_CLASS = "class_";
	private static final String KEY_DATA  = "data_";
	private static final String KEY_MAP_KEY = "key_";
	private static final String KEY_CUR_INDEX = "curIndex_";

	final ExpandArrayList<BaseScrapView> mViewStack;
	private final HashMap<String, BaseScrapView> mCachedViewMap;

    private static final Comparator<BaseScrapView> DEFAULT_COMPARATOR = new Comparator<BaseScrapView>() {
		@Override
		public int compare(BaseScrapView lhs, BaseScrapView rhs) {
			return lhs.compareTo(rhs);
		}
	};

	public CacheHelper() {
		mCachedViewMap = new HashMap<String, BaseScrapView>();
		mViewStack = new ExpandArrayList<BaseScrapView>(){
			@Override
			public boolean add(BaseScrapView baseScrapView) {
				if(!baseScrapView.isInBackStack())
				    baseScrapView.setInBackStack(true);
				return super.add(baseScrapView);
			}
			@Override
			public void add(int index, BaseScrapView baseScrapView) {
				if(!baseScrapView.isInBackStack())
					baseScrapView.setInBackStack(true);
				super.add(index, baseScrapView);
			}
		};
		// set Comparator to prevent the same class
		mViewStack.setComparator(DEFAULT_COMPARATOR);
		mViewStack.setStackMode(StackMode.Normal);
	}

	/** restore the setting of back stack. */
	public void resetStackSetting(){
		if(mViewStack.getComparator() != DEFAULT_COMPARATOR){
		   mViewStack.setComparator(DEFAULT_COMPARATOR);
		}
		if(mViewStack.getMode() != StackMode.Normal)
		   mViewStack.setStackMode(StackMode.Normal);
	}

	public List<BaseScrapView> getStackList() {
		return Collections.unmodifiableList(mViewStack);
	}

	/**
	 *  make the BaseScrapView in the cache for reuse.
	 * @param key the key to find for resue.
	 * @param view which view you want to cache
	 */
	public CacheHelper cache(String key, BaseScrapView view){
		if(view == null)
			throw new NullPointerException();
		mCachedViewMap.put(key, view);
		return this;
	}

	/** default key is the view.getClass().getName()*/
	public CacheHelper cache(BaseScrapView view){
		return cache(view.getClass().getName(), view);
	}

	public BaseScrapView getCacheView(String key){
		return mCachedViewMap.get(key);
	}

	/** remove the mapping of the key
	 * @param key  */
	public BaseScrapView removeCacheView(String key){
		return mCachedViewMap.remove(key);
	}

	/**
	 *  add view to the top of stack. it means it will be first removed from next first back event.
	 * @param view
	 */
	public CacheHelper addToStackTop(BaseScrapView view){
		mViewStack.addLast(view);
		return this;
	}
	/**
	 *  add view to the bottom of stack. it means it will be last removed from back stack..
	 * @param view
	 */
	public CacheHelper addToStackBottom(BaseScrapView view){
		mViewStack.addFirst(view);
		return this;
	}

	/**
	 * @return the size of back stack.
	 */
	public int getStackSize(){
		return mViewStack.size();
	}

	public BaseScrapView pollStackTail(){
		BaseScrapView view = mViewStack.pollLast();
		view.setInBackStack(false);
		return view;
	}

	public BaseScrapView pollStackHead(){
		BaseScrapView view = mViewStack.pollFirst();
		view.setInBackStack(false);
		return view;
	}

	/** get the bottom of stack.*/
	public BaseScrapView getStackHead(){
		return mViewStack.getFirst();
	}

	public void clearStack(){
		mViewStack.clear();
	}
	public void clearCache(){
		mCachedViewMap.clear();
	}

	public void clearAll(){
		mCachedViewMap.clear();
		mViewStack.clear();
	}
    /**
     * add the view to stack befor or after the referencedView.
     * @param referencedView the view to referenced
     * @param target   the target view to add
     * @param before  true to make the view before referencedView.false to after it.
     * @throws NoSuchElementException if referencedView isn't in stack.
     */
	public void addToStack(BaseScrapView referencedView, BaseScrapView target,
                           boolean before) {
		if(before)
			mViewStack.addBefore(referencedView, target);
		else
			mViewStack.addAfter(referencedView, target);
	}
	/**
     * add the view to stack befor or after the referencedView.
     * @param referencedClass the referencedClass of BaseScrapView
     * @param target   the view to add
     * @param before  true to make the view before referencedView.false to after it.
     * @throws NoSuchElementException if the referencedView isn't in stack.
     */
	/*public*/ void addToStack(Class<? extends BaseScrapView> referencedClass, BaseScrapView target,
                               boolean before) {
		//create a simulate BaseScrapView to indexOf
		BaseScrapView referencedView = Reflector.from(referencedClass).constructor(Context.class)
				 .newInstance(target.getContext());
		addToStack(referencedView, target, before);
	}

	public void onSaveInstanceState(Bundle out, int curIndex) {
		//save stack
		int size = mViewStack.size();
		out.putInt(KEY_STACK_PREFIX + KEY_SIZE, size);
		out.putInt(KEY_STACK_PREFIX + KEY_CUR_INDEX, curIndex);
		for (int i = 0 ; i < size ; i ++){
			BaseScrapView view = mViewStack.get(i);
			Bundle b = new Bundle();
			view.onSaveInstanceState(b);
			out.putBundle(KEY_STACK_PREFIX + KEY_DATA + i, b);
			out.putString(KEY_STACK_PREFIX + KEY_CLASS + i, view.getClass().getName());
		}
        //save cache
		size = mCachedViewMap.size();
		out.putInt(KEY_CACHE_PREFIX + KEY_SIZE, size);
		int i = 0;
		for (Map.Entry<String, BaseScrapView> en : mCachedViewMap.entrySet()){
			BaseScrapView view = en.getValue();

			Bundle b = new Bundle();
			view.onSaveInstanceState(b);
			out.putBundle(KEY_CACHE_PREFIX + KEY_DATA + i, b);
			out.putString(KEY_CACHE_PREFIX + KEY_CLASS + i, view.getClass().getName());
			out.putString(KEY_CACHE_PREFIX + KEY_MAP_KEY + i, en.getKey());
			i ++;
		}
	}
	public int onRestoreInstanceState(Context context, Bundle in) {
		int curIndex = -2;
        if(in != null){
			int size = in.getInt(KEY_STACK_PREFIX + KEY_SIZE);
			curIndex = in.getInt(KEY_STACK_PREFIX + KEY_CUR_INDEX);
			mViewStack.clear();
			for (int i = 0 ; i < size ; i++){
				String cn = in.getString(KEY_STACK_PREFIX + KEY_CLASS + i);
				Bundle b = in.getBundle(KEY_STACK_PREFIX + KEY_DATA + i);
				try {
					BaseScrapView view = Reflector.from(Class.forName(cn)).constructor(Context.class).newInstance(context);
					view.onRestoreInstanceState(b);
					mViewStack.add(view);
				} catch (ClassNotFoundException e) {
					throw new IllegalStateException(e);
				}
			}
			//restore cache
			mCachedViewMap.clear();
			size = in.getInt(KEY_CACHE_PREFIX + KEY_SIZE);
			for (int i = 0 ; i < size ; i ++){
				Bundle b = in.getBundle(KEY_CACHE_PREFIX + KEY_DATA + i);
				String cn = in.getString(KEY_CACHE_PREFIX + KEY_CLASS + i);
				String mapKey = in.getString(KEY_CACHE_PREFIX + KEY_MAP_KEY + i);

				try {
					BaseScrapView view = Reflector.from(Class.forName(cn)).constructor(Context.class).newInstance(context);
					view.onRestoreInstanceState(b);
					mCachedViewMap.put(mapKey, view);
				} catch (ClassNotFoundException e) {
					throw new IllegalStateException(e);
				}
			}
		}
        return curIndex;
	}
}
