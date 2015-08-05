# android-scrap
this is one Activity Framework named android-scrap, a little like fragment.  
it helps you to reduce number of activities  and avoid some problems of fragment.
but current you you must declare another activity as the entry activity.

## Features
- One Activity
- Support animation between two children of the 'BaseScrapView'.
- Support cache BaseScrapView and add it to the back stack.
- Support listen to Activity's Lifecycle and event.
- Support fast set properties of view ,named 'ViewHlper'.
- Integrated QuickAdapter and multi item Adapter. based on [JoanZapata/base-adapter-helper](https://github.com/JoanZapata/base-adapter-helper) and thanks for him.
- the more to see in demo or source code

## Changelog
  1.0  this is first import project.

## Demo
in model android-scrap/sample.

``` java
//[1], want jump to target ScrapView ( child of BaseScrapView)
 ScrapHelper.jumpTo(new EntryScrapView(MainActivity.this));
 
//[2], want cache ,add back stack, and jump 
 ScrapHelper.beginTransaction().cache(new EntryScrapView(MainActivity.this)).addBackAsBottom()
                        .jump().commit();
                        
//[3], animation between two children of the 'BaseScrapView'. 
 // here use animator to perform animation between two ScrapViews.
    private AnimateExecutor animateExecutor = new AnimateExecutor() {
        @Override//use animator
        protected AnimateCategoryType getType(boolean enter, BaseScrapView previous, BaseScrapView current) {
            return AnimateCategoryType.Animator;
        }

        @Override
        protected Animator prepareAnimator(View target, boolean enter, BaseScrapView previous, BaseScrapView current) {
            if(!enter){
                //exit
                return ObjectAnimator.ofFloat(target, "translationX", 0, 200)
                        .setDuration(2000);
            }else{
                // if it is the first BaseScrapView,return null to make it not to animate.
                if(previous == null)
                    return null;
                //enter
                return ObjectAnimator.ofFloat(target,"translationX", 200, 0)
                        .setDuration(2000);
            }
            // AnimatorInflater.loadAnimator(context, id)
        }
    };
    
     //set animate executor
     ScrapHelper.setAnimateExecutor(animateExecutor);
     
     //[4], event of activity 
      callback = new ActivityEventAdapter() {
            @Override
            public boolean onKeyDown(int keyCode, KeyEvent event) {
                ScrapLog.i("callback onKeyDown","");
                return super.onKeyDown(keyCode, event);
            }

            @Override
            public boolean onBackPressed() {
                ScrapLog.i("callback onBackPressed","");
                return super.onBackPressed(); //if return true. the all BaseScrapView can't receive back event.
            }

            @Override
            public boolean onTouchEvent(MotionEvent event) {
                ScrapLog.i("callback onTouchEvent","");
                return super.onTouchEvent(event);
            }
            //...etc methods
        };
        // the callback is regist as the global. so if the ScrapView is detached and you don't need, don't forget to 
        // unregister it.
        ScrapHelper.registerActivityEventCallback(callback);
        
        //[5], ....etc in demos

```

## License

    Copyright 2011, 2012 Chris Banes

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
