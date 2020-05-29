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

public  class ActivityLifeCycleAdapter implements IActivityLifeCycleCallback {

	@Override
	public void onActivityCreate(Activity activity, Bundle savedInstanceState) {
		
	}

	@Override
	public void onActivityPostCreate(Activity activity,
			Bundle savedInstanceState) {
		
	}

	@Override
	public void onActivityStart(Activity activity) {
		
	}

	@Override
	public void onActivityResume(Activity activity) {
		
	}

	@Override
	public void onActivityPause(Activity activity) {
		
	}

	@Override
	public void onActivityStop(Activity activity) {
		
	}

	@Override
	public void onActivityDestroy(Activity activity) {
		
	}

    @Override
    public void onActivityConfigurationChanged(Activity activity,
    		Configuration newConfig) {
    	
    }

	@Override
	public void onActivityUserLeaveHint(Activity activity) {
		
	}

	@Override
	public void onActivityResult(Activity activity, int requestCode,
			int resultCode, Intent data) {
		
	}

	@Override
	public void onActivityLowMemory(Activity activity) {
		
	}

	@Override
	public void onActivityWindowFocusChanged(Activity activity, boolean hasFocus) {
		
	}

	@Override
	public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResult) {

	}

}
