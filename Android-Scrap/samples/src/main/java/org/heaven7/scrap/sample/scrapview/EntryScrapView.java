package org.heaven7.scrap.sample.scrapview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import org.heaven7.scrap.adapter.QuickAdapter;
import org.heaven7.scrap.core.BaseScrapView;
import org.heaven7.scrap.core.ViewHelper;
import org.heaven7.scrap.core.anim.AnimateCategoryType;
import org.heaven7.scrap.core.anim.AnimateExecutor;
import org.heaven7.scrap.sample.R;
import org.heaven7.scrap.util.ArrayList2;
import org.heaven7.scrap.util.ScrapHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2015/8/3.
 */
public class EntryScrapView extends CommonView {

    private long start;
    /**
     * @param mContext
     */
    public EntryScrapView(Context mContext) {
        super(mContext);
    }

    // here not need bottom.so return null.
    @Override
    public View getBottomView() {
        return null;
    }

    @Override
    protected boolean onBackPressed() {
       return super.onBackPressed();
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        showToast("EntryScrapView is detached!");
    }

    @Override
    protected void onAttach() {

       final ViewHelper helper = getViewHelper();
        helper.setText(R.id.tv_title,"Scrap_Demos").setOnClickListener(R.id.iv_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        List<ActionData> datas =  getDatas();
        helper.setAdapter(R.id.lv, new QuickAdapter<ActionData>(R.layout.item_demo,datas) {

            @Override
            protected void convert(Context context, int position, ViewHelper viewHelper, ActionData item) {
                viewHelper.setText(R.id.bt_action,item.title)
                         .setText(R.id.tv_desc,item.desc)
                        .setTag(R.id.bt_action,item.id)
                        .setOnClickListener(R.id.bt_action,mClickListener);
            }
        });
    }
    final View.OnClickListener mClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = (int) v.getTag();
            Context context = v.getContext();
            switch (id){
                case 1:
                    // use transaction to cache? , addback,and jump.
            /**
             * if you use the same class ScrapView.class to create multi view  and  want add all of
             * them to the default back stack, you must call changeBackStackMode(ArrayList2.
             * ExpandArrayList2.Mode.Normal) first.
             * Because default setting( contains mode ) of back stack only save
             * different view of BaseScrapView. and after call Transaction.commit().
             * the setting( contains mode )  will restore to the default.
             */
                    Bundle b = new Bundle();
                    b.putInt("id",1);
                    ScrapHelper.beginTransaction().changeBackStackMode(ArrayList2.ExpandArrayList2.Mode.Normal)
                            .addBackAsTop(new ScrapView(context))
                            .withExtras(b).jump().commit();
                 //   ScrapHelper.beginTransaction().cache() //if you want cache the view.
                    break;
                case 2:
                   ScrapHelper.jumpTo(new TestVisivilityScrapView(context));
                    break;
                case 3:
                    ScrapHelper.jumpTo(new TestLifeCycleScrapView(context));
                    break;
                case 4:

                    /**
                     * if you want to set global animate executor,  please use ScrapHelper.setAnimateExecutor(animateExecutor);
                     */
                    Bundle b2 = new Bundle();
                    b2.putInt("id",1);
                    //also use can use #setBundle to carray data
                   // view2.setBundle(data);
                    ScrapHelper.beginTransaction().changeBackStackMode(ArrayList2.ExpandArrayList2.Mode.Normal)
                            .addBackAsTop(new ScrapView(context))
                            .animateExecutor(animateExecutor).withExtras(b2)
                            .jump().commit();
                    break;
                case 5:
                    ScrapHelper.jumpTo(new TestKeyEventScrapView(context));
                    break;

                case 6:
                    ScrapHelper.jumpTo(new TestLoadingScrapView(context));
            }
        }
    };
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

    private List<ActionData> getDatas() {
        List<ActionData> datas = new ArrayList<>();
        String desc = "this is a sample tell you how to listen default back event. how to use Transaction" +
                "to cache, add back,jump to it.";
        datas.add(new ActionData("Back Stack and Transaction",desc,1));

        desc = "this is a sample tell you how to hide or show or toogle visible of top/bottom/middle";
        datas.add(new ActionData("Visibility of ScrapPosition",desc,2));

        desc = "this is a sample tell you how to use the Activity's lifecycle callback ";
        datas.add(new ActionData("Activity's life cycle",desc,3));

        desc = "this is a sample tell you how to use the animation from one BaseScrapView to another. ";
        datas.add(new ActionData("ScrapView's Animation",desc,4));

        desc = "this is a sample tell you how to register/unregiser the other key event of activity.";
        datas.add(new ActionData("Activity's other Key event",desc,5));

        desc = "this is a sample tell you how to use the loading view...";
        datas.add(new ActionData("Loading View",desc,6));
        return datas;
    }
    static class ActionData{
        public ActionData(String title, String desc, int id) {
            this.title = title;
            this.desc = desc;
            this.id = id;
        }

        String title;
        String desc;
        int id;
    }
}
