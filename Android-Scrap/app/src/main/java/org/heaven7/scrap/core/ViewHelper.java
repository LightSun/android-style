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

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.data.RequestManager;
import com.android.volley.extra.ExpandNetworkImageView;
import com.android.volley.extra.ImageParam;

import org.heaven7.scrap.util.ViewCompatUtil;

/**
 * for better use same view's method. cached it automatic for reuse.
 * use can through {@link BaseScrapView#getViewHelper()} to get.
 * @author heaven7
 *
 */
public class ViewHelper {

	private final SparseArray<View> mViewMap;
	private final View mRootView;
	
	/**
	 * the loader to load image
	 * @author heaven7
	 */
	public interface IImageLoader{
		
		  void load(String url, ImageView iv);
	}
	
	public ViewHelper(View root) {
		this.mRootView = root;
		mViewMap = new SparseArray<View>();
	}
	
	public View getRootView() {
		return mRootView;
	}
	public Context getContext(){
		return mRootView.getContext();
	}
	
	public ViewHelper setText(int viewId,CharSequence text){
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}
	/**
	 * toogle the visibility of the view.such as: VISIBLE to gone or gone to VISIBLE
	 * @param viewId  the id of view
	 */
	public ViewHelper toogleVisibility(int viewId){
		View view = getView(viewId);
		if(view.getVisibility() == View.VISIBLE){
			view.setVisibility(View.GONE);
		}else{
			view.setVisibility(View.VISIBLE);
		}
		return this;
	}

	/** get the view in current layout . return null if the viewid can't find in current layout*/
	@SuppressWarnings("unchecked")
	public <T extends View > T getView(int viewId) {
		View view = mViewMap.get(viewId);
		if(view == null){
			view = mRootView.findViewById(viewId);
			if(view ==null)
				throw new IllegalStateException("can't find the view ,id = " +viewId);
			mViewMap.put(viewId, view);
		}
		return (T) view;
	}
	/**
	 * Will set the image of an ImageView from a resource id.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param imageResId
	 *            The image resource id.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setImageResource(int viewId, int imageResId) {
		ImageView view = retrieveView(viewId);
		view.setImageResource(imageResId);
		return this;
	}
	/**
	 * Will set background color of a view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param color
	 *            A color, not a resource id.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setBackgroundColor(int viewId, int color) {
		View view = getView(viewId);
		view.setBackgroundColor(color);
		return this;
	}

	/**
	 * Will set background of a view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param backgroundRes
	 *            A resource to use as a background. 0 to remove it.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setBackgroundRes(int viewId, int backgroundRes) {
		View view = getView(viewId);
		view.setBackgroundResource(backgroundRes);
		return this;
	}
	
	public ViewHelper setBackgroundDrawable(int viewId,Drawable drawable){
		View view = getView(viewId);
		ViewCompatUtil.setBackgroundCompatible(view, drawable);
		return this;
	}
	
	/**
	 * Will set text color of a TextView.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param textColor
	 *            The text color (not a resource id).
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setTextColor(int viewId, int textColor) {
		TextView view = retrieveView(viewId);
		view.setTextColor(textColor);
		return this;
	}

	/**
	 * Will set text color of a TextView.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param textColorRes
	 *            The text color resource id.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setTextColorRes(int viewId, int textColorRes) {
		TextView view = retrieveView(viewId);
		view.setTextColor(getContext().getResources().getColor(textColorRes));
		return this;
	}
	
	/**
	 * Will set the image of an ImageView from a drawable.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param drawable
	 *            The image drawable.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setImageDrawable(int viewId, Drawable drawable) {
		ImageView view = retrieveView(viewId);
		view.setImageDrawable(drawable);
		return this;
	}

	/**
	 * Will download an image from a URL and put it in an ImageView.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param imageUrl
	 *            The image URL.
	 * @param loader 
	 *             which to load image actually.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setImageUrl(int viewId, String imageUrl,IImageLoader loader) {
		ImageView view = retrieveView(viewId);
		loader.load(imageUrl, view);
		return this;
	}
	/** use volley extra to load image（this support circle image and round）.
	 * <li>@Note RequestManager must call {@link RequestManager#init(Context)} before this.
	 * such as in {@link Application#onCreate()}
	 * @param param  image param to control what bitmap to show!
	 * @param viewId must be {@link ExpandNetworkImageView}
	 * @see {@link ImageParam.Builder#circle()} and etc methods.
	 * */
	public ViewHelper setImageUrl(int viewId,String url,ImageParam param){
		ExpandNetworkImageView view = retrieveView(viewId);
		view.setImageParam(param);
		view.setImageUrl(url, RequestManager.getImageLoader());
		return this;
	}
	
	/**
	 * Add an action to set the image of an image view. Can be called multiple
	 * times.
	 */
	public ViewHelper setImageBitmap(int viewId, Bitmap bitmap) {
		ImageView view = retrieveView(viewId);
		view.setImageBitmap(bitmap);
		return this;
	}

	/**
	 * Add an action to set the alpha of a view. Can be called multiple times.
	 * Alpha between 0-1.
	 */
	@SuppressLint("NewApi")
	public ViewHelper setAlpha(int viewId, float value) {
		ViewCompatUtil.setAlpha(retrieveView(viewId), value);
		return this;
	}
	

	/**
	 * Set a view visibility to VISIBLE (true) or GONE (false).
	 * 
	 * @param viewId
	 *            The view id.
	 * @param visible
	 *            True for VISIBLE, false for GONE.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setVisibility(int viewId, boolean visible) {
		View view = retrieveView(viewId);
		view.setVisibility(visible ? View.VISIBLE : View.GONE);
		return this;
	}

	
	/**
	 * Add links into a TextView. default is 
	 * 
	 * @param viewId
	 *            The id of the TextView to linkify.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper linkify(int viewId) {
		TextView view = retrieveView(viewId);
		Linkify.addLinks(view, Linkify.ALL);
		return this;
	}
	private <T extends View> T retrieveView(int viewId) {
		return getView(viewId);
	}

	/** Add links into a TextView,
	 * @param  linkifyMask ,see {@link Linkify#ALL} and etc.*/
	public ViewHelper linkify(int viewId,int linkifyMask) {
		TextView view = retrieveView(viewId);
		Linkify.addLinks(view, linkifyMask);
		return this;
	}

	/** Apply the typeface to the given viewId, and enable subpixel rendering. */
	public ViewHelper setTypeface(int viewId, Typeface typeface) {
		TextView view = retrieveView(viewId);
		view.setTypeface(typeface);
		view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
		return this;
	}

	/**
	 * Apply the typeface to all the given viewIds, and enable subpixel
	 * rendering.
	 */
	public ViewHelper setTypeface(Typeface typeface, int... viewIds) {
		for (int viewId : viewIds) {
			setTypeface(viewId,typeface);
		}
		return this;
	}
	
	/**
	 * Sets the progress of a ProgressBar.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param progress
	 *            The progress.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setProgress(int viewId, int progress) {
		ProgressBar view = retrieveView(viewId);
		view.setProgress(progress);
		return this;
	}

	/**
	 * Sets the progress and max of a ProgressBar.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param progress
	 *            The progress.
	 * @param max
	 *            The max value of a ProgressBar.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setProgress(int viewId, int progress, int max) {
		ProgressBar view = retrieveView(viewId);
		view.setMax(max);
		view.setProgress(progress);
		return this;
	}

	/**
	 * Sets the range of a ProgressBar to 0...max.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param max
	 *            The max value of a ProgressBar.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setMax(int viewId, int max) {
		ProgressBar view = retrieveView(viewId);
		view.setMax(max);
		return this;
	}

	/**
	 * Sets the rating (the number of stars filled) of a RatingBar.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param rating
	 *            The rating.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setRating(int viewId, float rating) {
		RatingBar view = retrieveView(viewId);
		view.setRating(rating);
		return this;
	}

	/**
	 * Sets the rating (the number of stars filled) and max of a RatingBar.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param rating
	 *            The rating.
	 * @param max
	 *            The range of the RatingBar to 0...max.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setRating(int viewId, float rating, int max) {
		RatingBar view = retrieveView(viewId);
		view.setMax(max);
		view.setRating(rating);
		return this;
	}

	/**
	 * Sets the tag of the view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param tag
	 *            The tag;
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setTag(int viewId, Object tag) {
		View view = retrieveView(viewId);
		view.setTag(tag);
		return this;
	}

	/**
	 * Sets the tag of the view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param key
	 *            The key of tag;
	 * @param tag
	 *            The tag;
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setTag(int viewId, int key, Object tag) {
		View view = retrieveView(viewId);
		view.setTag(key, tag);
		return this;
	}

	/**
	 * Sets the checked status of a checkable.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param checked
	 *            The checked status;
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setChecked(int viewId, boolean checked) {
		Checkable view = (Checkable) retrieveView(viewId);
		view.setChecked(checked);
		return this;
	}
	
	/** set OnCheckedChangeListener to CompoundButton or it's children. */
	public ViewHelper setOnCheckedChangeListener(int viewId, OnCheckedChangeListener l){
		CompoundButton view = retrieveView(viewId);
		view.setOnCheckedChangeListener(l);
		return this;
	}

	/**
	 * Sets the adapter of a adapter view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param adapter
	 *            The adapter;
	 * @return The ViewHelper for chaining.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ViewHelper setAdapter(int viewId, Adapter adapter) {
		AdapterView view = retrieveView(viewId);
		view.setAdapter(adapter);
		return this;
	}

	/**
	 * Sets the on click listener of the view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param listener
	 *            The on click listener;
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setOnClickListener(int viewId,
			View.OnClickListener listener) {
		View view = retrieveView(viewId);
		view.setOnClickListener(listener);
		return this;
	}

	/**
	 * Sets the on touch listener of the view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param listener
	 *            The on touch listener;
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setOnTouchListener(int viewId,
			View.OnTouchListener listener) {
		View view = retrieveView(viewId);
		view.setOnTouchListener(listener);
		return this;
	}

	/**
	 * Sets the on long click listener of the view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param listener
	 *            The on long click listener;
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setOnLongClickListener(int viewId,
			View.OnLongClickListener listener) {
		View view = retrieveView(viewId);
		view.setOnLongClickListener(listener);
		return this;
	}
	
}
