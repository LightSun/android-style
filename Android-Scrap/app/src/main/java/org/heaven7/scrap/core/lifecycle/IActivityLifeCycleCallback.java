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

/**
 * Activity life cycle callback. if you want track the method of them.please call {@link org.heaven7.scrap.util.ScrapHelper#registerActivityLifeCycleCallback(IActivityLifeCycleCallback...)}
 * and {@link org.heaven7.scrap.util.ScrapHelper#unregisterActivityLifeCycleCallback(IActivityLifeCycleCallback...)}
 * @author  heaven7
 */
public interface IActivityLifeCycleCallback {

	void onActivityCreate(Activity activity, Bundle savedInstanceState);
	void onActivityPostCreate(Activity activity, Bundle savedInstanceState);
	
	void onActivityStart(Activity activity);
	void onActivityResume(Activity activity);
	
	void onActivityPause(Activity activity);
	void onActivityStop(Activity activity);
	void onActivityDestroy(Activity activity);
	
	void onActivitySaveInstanceState(Activity activity, Bundle outState);
	void onActivityRestoreInstanceState(Activity activity,
										Bundle savedInstanceState);
	
	void onActivityConfigurationChanged(Activity activity, Configuration newConfig);
	
	void onActivityUserLeaveHint(Activity activity);
	
	void onActivityResult(Activity activity, int requestCode, int resultCode,
						  Intent data);
	
	void onActivityLowMemory(Activity activity);
	
	void onActivityWindowFocusChanged(Activity activity, boolean hasFocus);
	
}
