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

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.view.View;
/**
 * the internal anmator listener
 * @author heaven7
 */
/* package */ class InternalAnimatorListener implements AnimatorListener {
	
	final View target;
	final AnimateExecutor.OnAnimateEndListener l;

	public InternalAnimatorListener(View target, AnimateExecutor.OnAnimateEndListener l) {
		super();
		this.target = target;
		this.l = l;
	}

	@Override
	public void onAnimationStart(Animator animation) {

	}

	@Override
	public void onAnimationEnd(Animator animation) {
         l.onAnimateEnd(target);
	}

	@Override
	public void onAnimationCancel(Animator animation) {

	}

	@Override
	public void onAnimationRepeat(Animator animation) {

	}

}
