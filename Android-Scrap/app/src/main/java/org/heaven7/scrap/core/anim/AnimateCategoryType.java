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
/**
 * indicate three kinds of animation : Animation  and Animator and ViewPropertyAnimator
 * @author heaven7
 *
 */
public enum AnimateCategoryType {

	/**
	 * this indicate you want to use Animation,such as: AlphaAnimation, TranslateAnimation...and etc 
	 */
	Animation,
	/**
	 * this indicate you want to use animator,such as: ObjectAnimator, AnimatorSet...and etc 
	 */
	Animator , 
	/**
	 * this indicate you want to use ViewPropertyAnimator, this can be return by view.animate(); 
	 */
	ViewPropertyAnimator;
}
