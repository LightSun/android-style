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
/**
 * event listener: contains key event.and touchevent
 * this comes from Activity. the more you can see in Activity.
 * @author heaven7
 *
 */
public interface IActivityEventCallback {

	/** return true to prevent the event to continue spread */
	boolean onKeyDown(int keyCode, KeyEvent event);

	/** return true to prevent the event to continue spread */
	 boolean onKeyUp(int keyCode, KeyEvent event);

	/** return true to prevent the event to continue spread */
	 boolean onKeyShortcut(int keyCode, KeyEvent event);

	/** return true to prevent the event to continue spread */
	 boolean onKeyLongPress(int keyCode, KeyEvent event);

	/** return true to prevent the event to continue spread */
	 boolean onKeyMultiple(int keyCode, KeyEvent event);

	/** return true to prevent the event to continue spread */
	 boolean onBackPressed();

	/** return true to prevent the event to continue spread */
	 boolean onTouchEvent(MotionEvent event);

}
