package org.heaven7.scrap.sample.scrapview;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import org.heaven7.scrap.core.ViewHelper;
import org.heaven7.scrap.sample.R;
import org.heaven7.scrap.sample.ScrapLog;
import org.heaven7.scrap.util.ArrayList2;
import org.heaven7.scrap.util.ScrapHelper;

/**
 * Created by heaven7 on 2015/8/3.
 */
public class ScrapView extends CommonView {

    public int id;
    private boolean mIsLastScrapView;

    public ScrapView(Context mContext) {
        super(mContext);
    }

    public void setIsLastScrapView(boolean last){
        this.mIsLastScrapView = last;
    }

    @Override
    protected int getMiddleLayoutId() {
        return R.layout.scrap_middle_common;
    }


    @Override
    protected void onAttach() {
        this.id = getBundle() == null ? 0 : getBundle().getInt("id");
        if(id == 4){
            setIsLastScrapView(true);
        }

        ScrapLog.i("ScrapView_onAttach","id = " + id);

        ViewHelper helper = getViewHelper();
        helper.setOnClickListener(R.id.iv_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }).setText(R.id.tv_title,"ScrapView"+id);

        if(mIsLastScrapView){
            helper.setText(R.id.tv_middle,getResources().getText(R.string.test_back_please_click_back))
                    .setVisibility(R.id.button, false);
        }else{
            helper.setText(R.id.tv_middle, getResources().getText(R.string.test_back_please_click_button))
                    .setVisibility(R.id.button,true);
        }
        helper.setOnClickListener(R.id.button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * if you use the same class ScrapView.class to create multi view  and  want add all of
                 * them to the default back stack, you must call changeBackStackMode(ArrayList2.
                 * ExpandArrayList2.Mode.Normal) first.
                 * Because default setting( contains mode ) of back stack only save
                 * different view of BaseScrapView. and after call Transaction.commit().
                 * the setting( contains mode )  will restore to the default.
                 */
                Bundle b = new Bundle();
                b.putInt("id",id+1);

                ScrapHelper.beginTransaction().changeBackStackMode(ArrayList2.ExpandArrayList2.Mode.Normal)
                        .addBackAsTop(new ScrapView(v.getContext()))
                        .withExtras(b).jump().commit();
            }
        });
    }

    @Override
    protected void onDetach() {
        showToast("ScrapView "+id+ " is detached!");
    }

    @Override
    public String toString() {
        return "ScrapView{" +
                "id=" + id +
                '}';
    }
}
