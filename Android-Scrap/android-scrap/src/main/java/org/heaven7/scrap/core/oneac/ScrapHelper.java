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
import org.heaven7.scrap.core.event.IActivityEventCallback;
import org.heaven7.scrap.core.lifecycle.IActivityLifeCycleCallback;

/**
 * a helpful util to use the 'OneActivity' framework.such as: jump to different page of {@linkplain BaseScrapView}
 * if you want more ,please see {@link ActivityController} and {@link ActivityViewController}.
 * <p>how to get them ?</p>
 * <pre>ActivityController controller = ActivityController.get();
 *     ActivityViewController viewController = controller.getViewController();
 *     ...// the more you want to do!
 * </pre>
 * @author heaven7
 * @see ActivityController
 * @see ActivityViewController
 * @see Transaction
 */
public final class ScrapHelper {

	/** jump to the target view with no data. */
	public static void jumpTo(BaseScrapView view){
		ActivityController.get().jumpTo(view);
	}

	/**
	 * jump to the target view with data. but not cache it or add to back stack.
	 * if you want more . please use {@link #beginTransaction()}
	 * @param view the view to jump
	 * @param data the data to carry.
	 * @see Transaction
	 */
	public static void jumpTo(BaseScrapView view ,Bundle data){
		ActivityController.get().jumpTo(view, data);
	}

	/**
	 * jump to the target view with data and animate executor.
	 * @param target  the target to jump
	 * @param data   the data to carry
	 * @param executor   the animate executor to perform this jump(only use once)
	 */
	public static void jumpTo(BaseScrapView target,Bundle data,AnimateExecutor executor){
		getActivityViewController().jumpTo(target,data,executor);
	}
	/**
	 * jump to the target view with animate executor.
	 * @param target  the target to jump
	 * @param executor   the animate executor to perform this jump(only use once)
	 */
	public static void jumpTo(BaseScrapView target,AnimateExecutor executor){
		getActivityViewController().jumpTo(target,null,executor);
	}

	/**
	 * open the transaction of {@link ActivityViewController}, this is very useful.
	 * here is the sample code: <p>
	 * <pre> Transaction transaction = ActivityController.get().beginTransaction();
     *       transaction.cache(view).changeBackStackMode(ExpandArrayList2.Mode.ReplacePreviousAndClearAfter)
     *           .addBack().jump().commit();
     *  </pre></p>
     *  the more to see {@link Transaction}
     *  @see Transaction
	 */
	public static Transaction beginTransaction(){
		return ActivityController.get().beginTransaction();
	}

	/**
	 * register Activity life cycle callback. which will be call in the Activity.
	 * also you can use {@link org.heaven7.scrap.core.lifecycle.ActivityLifeCycleAdapter} ,it implement IActivityLifeCycleCallback
	 * @param callbacks the callbacks
	 * @see  IActivityLifeCycleCallback
	 * @see  org.heaven7.scrap.core.lifecycle.ActivityLifeCycleAdapter
	 * @see  org.heaven7.scrap.core.lifecycle.ActivityLifeCycleDispatcher
	 */
	public static void registerActivityLifeCycleCallback(IActivityLifeCycleCallback... callbacks){
		ActivityController.get().getLifeCycleDispatcher().registerActivityLifeCycleCallback(callbacks);
	}

	/**
	 * unregister the activity life cycle callback. suach as: Activity_onResume(),Activity_onStop()
	 * @param callbacks the callbacks
	 * @see  #registerActivityLifeCycleCallback(IActivityLifeCycleCallback...)
	 */
	public static void unregisterActivityLifeCycleCallback(IActivityLifeCycleCallback...callbacks){
		ActivityController.get().getLifeCycleDispatcher().unregisterActivityLifeCycleCallback(callbacks);
	}

	/**
	 * register the event callback of Activity.such as: key event(down/up...etc).touch event.
	 * also use can use {@link org.heaven7.scrap.core.event.ActivityEventAdapter}
	 * @param callbacks the callbacks
	 * @see  IActivityEventCallback
	 * @see org.heaven7.scrap.core.event.ActivityEventAdapter
	 */
	public static void registerActivityEventCallback(IActivityEventCallback ...callbacks){
        ActivityController.get().getEventListenerGroup().registerActivityEventListener(callbacks);
	}

	/**
	 * unregister the event callback of activity.
	 * @param callbacks the callbacks
	 * @see  #registerActivityEventCallback(IActivityEventCallback...)
	 */
	public static void unregisterActivityEventCallback(IActivityEventCallback ...callbacks){
        ActivityController.get().getEventListenerGroup().unregisterActivityEventListener(callbacks);
	}

	/**
	 * whether or not the target scarp view is at the top of back stack.
	 * <li>head means it will be removed from back stack with the next first back event.</li>
	 * @param target  the target view
	 * @return  true  if the target is the head view of back stack.
	 */
	public static boolean isScrapViewAtTop(BaseScrapView target){
		if(target == null)
			return false;
       return ActivityController.get().getViewController().getCurrentView() == target;
	}
	/**
	 * whether or not the target scarp view is at the bottom of back stack.
	 * <li>bottom means it will be last removed from back stack </li>
	 * @param target  the target view
	 * @return  true  if the target at the bottom view of back stack.
	 *       false  if the target is null or it not add to the back stack.
	 * @see  #isScrapViewAtTop(BaseScrapView)
	 * @see  Transaction#addBackAsBottom()
	 */
	public static boolean isScrapViewAtBottom(BaseScrapView target){
       return ActivityController.get().getViewController().isScrapViewAtBottom(target);
	}

	/** finish the current activity which is attached to {@link ActivityController}  */
	public static void finishCurrentActivity(){
		ActivityController.get().finishActivity();
	}

	/**
	 * get the activity's view controller
	 */
	public static ActivityViewController getActivityViewController(){
		return ActivityController.get().getViewController();
	}

	/** set the visible of view .
	 * @param  visible true to visible,false to gone.
	 * */
	public static void setVisibility(boolean visible){
		getActivityViewController().setVisibility(visible);
	}

	/**
	 * toggle the visibility of view .
	 */
	public static void toggleVisibility() {
		getActivityViewController().toggleVisibility();
	}

	/**
	 * set the animate executor when jump from one ScrapView to another.
	 * @note this animate executor is used as  the global .
	 * @param animateExecutor the animate executor
	 * @see  AnimateExecutor
	 */
	public static void setAnimateExecutor(AnimateExecutor animateExecutor) {
		ActivityController.get().setAnimateExecutor(animateExecutor);
	}
}
