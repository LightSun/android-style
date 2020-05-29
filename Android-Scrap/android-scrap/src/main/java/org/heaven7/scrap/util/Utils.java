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
package org.heaven7.scrap.util;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.RestrictTo;

import com.heaven7.core.util.ConfigUtil;
import com.heaven7.core.util.DimenUtil;
import com.heaven7.core.util.ResourceUtil;

import java.util.Properties;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class Utils {

	private static int sSystemUIHeight;

	/** load properties which is under the raw (exclude extension )*/
	public static Properties loadRawConfig(Context context,String rawResName) {
		int resId = ResourceUtil.getResId(context, rawResName, ResourceUtil.ResourceType.Raw);
		return ConfigUtil.loadRawConfig(context, resId);
	}

	public static int getSystemUIHeight(Context context) {
		if (0 == sSystemUIHeight) {
			Resources resources = context.getResources();
			int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
			if (resourceId > 0) {
				sSystemUIHeight = resources.getDimensionPixelSize(resourceId);
			} else {
				sSystemUIHeight = DimenUtil.dip2px(context, 56);
			}
		}
		return sSystemUIHeight;
	}
}
