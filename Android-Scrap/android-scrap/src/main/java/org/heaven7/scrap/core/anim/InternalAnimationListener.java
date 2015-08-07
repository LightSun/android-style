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
package org.heaven7.scrap.core.anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;


 /* package */ class InternalAnimationListener implements AnimationListener{
		
		final View target;
		final AnimateExecutor.OnAnimateEndListener l;

		public InternalAnimationListener(View target, AnimateExecutor.OnAnimateEndListener l) {
			super();
			this.target = target;
			this.l = l;
		}
		@Override
		public void onAnimationStart(Animation animation) {
		}
		@Override
		public void onAnimationEnd(Animation animation) {
			l.onAnimateEnd(target);
		}
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
		
	}
