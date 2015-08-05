# android-scrap
this is one Activity Framework named android-scrap, a little like fragment.  
it helps you to reduce number of activities  and avoid some problems of fragment.
but current you you must declare another activity as the entry activity.
The module 'app' is the library of scrap. and 'sample' is the demo.

## Features
- One Activity
- Support animation between two children of the 'BaseScrapView'.
- Support cache BaseScrapView and add it to the back stack.
- Support listen to Activity's Lifecycle and event.
- Support fast set properties of view ,named 'ViewHelper'.
- Integreted google-Volley and expand it to support  file upload and circle/round image. and you can see it in demo.
- Integrated QuickAdapter and multi item Adapter. based on [JoanZapata/base-adapter-helper](https://github.com/JoanZapata/base-adapter-helper) and thanks for him.
- the more to see in demo or source code

## Changelog
  1.0  this is first import project.
  
## About me
   * heaven7 
   * email: donshine723@gmail.com or 978136772@qq.com   
   
## issue
   * if you have some problems while using this framework or have bug.please tell me!

### How to use 
   in gradle:  wait...
   
## Demo
in model [android-scrap/sample](https://github.com/LightSun/android-scrap/tree/master/Android-Scrap/samples).

<img src="1.png"/>

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
    
     //set animate executor (it used as the global)
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
        
        //[5],QuickAdapter and ViewHelper. in a child of 'BaseScrapView'
         @Override
    protected void onAttach() {
        super.onAttach();
        showToast("CommonView is attached");
        getViewHelper().setOnClickListener(R.id.iv_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }).setOnClickListener(R.id.bt_1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("button1 was clicked");
            }
        }).setOnClickListener(R.id.bt_2, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("button2 was clicked");
            }
        }).setOnClickListener(R.id.bt_3, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("button3 was clicked");
            }
        });
        //set the list view's data
        //use QuickAdapter to fast set adapter.
        addGirlDatas();
        getViewHelper().setAdapter(R.id.lv, new QuickAdapter<GirlData>(R.layout.item_girl,mGirlData) {
            @Override
            protected void convert(Context context, int position, ViewHelper viewHelper, GirlData item) {
                   viewHelper.setText(R.id.tv,item.name);
                   viewHelper.setImageUrl(R.id.eniv, item.imageUrl, new ImageParam.Builder()
                           .placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).circle().create());
            }
        });
    }
        
        //[6], ....etc in demos

```

## License

    Copyright 2015   
                    heaven7(donshine723@gmail.com)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
