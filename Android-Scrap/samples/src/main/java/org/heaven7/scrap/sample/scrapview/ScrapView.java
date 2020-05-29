package org.heaven7.scrap.sample.scrapview;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.heaven7.core.util.ViewHelper;

import org.heaven7.scrap.core.oneac.ScrapHelper;
import org.heaven7.scrap.core.oneac.StackMode;
import org.heaven7.scrap.sample.R;
import org.heaven7.scrap.sample.ScrapLog;

import butterknife.BindView;

/**
 * Created by heaven7 on 2015/8/3.
 */
public class ScrapView extends CommonView {

    @BindView(R.id.tv_title)
    TextView mTv_title;
    @BindView(R.id.tv_middle)
    TextView mTv_middle;

    @BindView(R.id.button)
    View mBtn;

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

        mTv_title.setText("ScrapView"+id);

        if(mIsLastScrapView){
            mTv_middle.setText(getResources().getText(R.string.test_back_please_click_back));
            mBtn.setVisibility(View.GONE);
        }else{
            mTv_middle.setText(getResources().getText(R.string.test_back_please_click_button));
            mBtn.setVisibility(View.VISIBLE);
        }
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * if you use the same class ScrapView.class to create multi view  and  want add all of
                 * them to the default back stack, you must call stackMode(StackMode) first.
                 * Because default setting( contains mode ) of back stack only save
                 * different view of BaseScrapView. and after call Transaction.commit().
                 * the setting( contains mode )  will restore to the default.
                 */
                Bundle b = new Bundle();
                b.putInt("id",id+1);

                ScrapHelper.beginTransaction().stackMode(StackMode.Normal)
                        .addBackAsTop(new ScrapView(v.getContext()))
                        .arguments(b).jump().commit();
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
