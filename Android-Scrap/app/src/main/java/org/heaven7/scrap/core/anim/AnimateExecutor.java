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
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;

import org.heaven7.scrap.annotation.CalledByFramework;
import org.heaven7.scrap.core.BaseScrapView;
/**
 * used to perform the animation of cross the stage(eg: from one abstract view to another). but i don't know which you want to use. 
 * such as: Animation , Animator , ViewPropertyAnimator. so {@link AnimateCategoryType} indicator this three.
 * 
 * @see AnimateCategoryType#Animation
 * @see AnimateCategoryType#Animator
 * @see AnimateCategoryType#ViewPropertyAnimator
 * @author heaven7
 *
 */
public abstract class AnimateExecutor {

	/**
	 * @see #prepareAnmation(View, boolean, BaseScrapView, BaseScrapView)
	 * @return  a AnimateCategoryType , can be null.if you don't need animation
	 * */
	protected abstract AnimateCategoryType getType(boolean enter,BaseScrapView previous,
	           BaseScrapView current);
	
	/**
	 * prepare an Animation  which will be start by framework. it will be start only if
	 * {@link #getType(boolean, BaseScrapView, BaseScrapView)} return {@link AnimateCategoryType#Animation}
	 * @param target  the target to animate,offen is the root view of activity
	 * @param enter   true means is the enter animation,false is the exit animation!
	 * @param previous  the previous BaseScrapView which will perform exit animation!
	 * @param current  the current BaseScrapView which will perform enter animation!
	 * @return  an prepared animarion but not start.can be null.if you don't need this animation
	 */
	protected Animation prepareAnmation(View target,boolean enter,BaseScrapView previous,
	           BaseScrapView current){
		return null;
	}
	/**
	 * same as {@link #prepareAnmation(View, boolean, BaseScrapView, BaseScrapView)}
	 * @see  #prepareAnmation(View, boolean, BaseScrapView, BaseScrapView)
	 * @return an prepared animator but not start.can be null.if you don't need this animation
	 */
	protected Animator prepareAnimator(View target,boolean enter,BaseScrapView previous,
	           BaseScrapView current){
		return null;
	}
	/**
	 * same as {@link #prepareAnmation(View, boolean, BaseScrapView, BaseScrapView)}
	 * @see  #prepareAnmation(View, boolean, BaseScrapView, BaseScrapView)
	 * @return an prepared ViewPropertyAnimator but not start.can be null.if you don't need this animation
	 */
	protected ViewPropertyAnimator prepareViewPropertyAnimator(View target,boolean enter,
			BaseScrapView previous,BaseScrapView current){
		return null;
	}
	
	/***
	 * perform the concrete animation/animator/ViewPropertyAnimator when cross two 'BaseScrapView'.
	 * the is called by the frame work!
	 * @param target  the target to animate,offen is the root view of activity
	 * @param enter   true means is the enter animation,false is the exit animation!
	 * @param previous  the previous BaseScrapView which will perform exit animation!
	 * @param current  the current BaseScrapView which will perform enter animation!
	 * @param l the animation end listener which must be called when animation is the end in exit animation.
	 */
	@CalledByFramework
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void performAnimate(View target,boolean enter,BaseScrapView previous,
			           BaseScrapView current,OnAnimateEndListener l){
		
		switch (getType(enter,previous,current)) {
			
		case Animation:
			Animation animation = prepareAnmation(target,enter,previous,current);
			if(animation!=null){
				if(animation.getRepeatCount() == Animation.INFINITE){
					throw new IllegalStateException("In here Animation.getRepeatCount() can't be Animation.INFINITE!");
				}
				if(l!=null)
				     animation.setAnimationListener(new InternalAnimationListener(target, l));
				target.startAnimation(animation);
			}else{
				if(l!=null)
				   l.onAnimateEnd(target);
			}
			break;
			
		case Animator:
			Animator animator = prepareAnimator(target,enter,previous,current);
			if(animator != null){
				if(l!=null)
				    animator.addListener(new InternalAnimatorListener(target, l));
				animator.start();
			}else{
				if(l!=null)
					l.onAnimateEnd(target);
			}
			break;
			
		case ViewPropertyAnimator:
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
				ViewPropertyAnimator propAnimator = prepareViewPropertyAnimator(
						target,enter,previous,current);
				if(propAnimator!=null){
					if(l!=null)
					     propAnimator.setListener(new InternalAnimatorListener(target, l));
					propAnimator.start();
				}else{
					if(l!=null)
						l.onAnimateEnd(target);
				}
			}else{
				//unsupport not perform animate. call the end directly.
				if(l!=null)
					l.onAnimateEnd(target);
			}
			break;

		default://such as null
			if(l!=null)
				l.onAnimateEnd(target);
		}
	}
	/**
	 * the animation end listener.
	 * @author heaven7
	 */
	public interface OnAnimateEndListener{
		/**
		 * will be called at the end of animation.
		 * @param target  the view to animate.
		 */
		void onAnimateEnd(View target);
	}
}
