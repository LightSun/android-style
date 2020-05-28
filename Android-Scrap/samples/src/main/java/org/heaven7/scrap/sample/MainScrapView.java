package org.heaven7.scrap.sample;

import android.content.Context;
import android.view.View;

import org.heaven7.scrap.core.oneac.BaseScrapView;
import org.heaven7.scrap.core.oneac.ScrapHelper;
import org.heaven7.scrap.sample.scrapview.EntryScrapView;

/**
 * similar as the Main Activity: so this is used as the Splash scrap view.
 * but you must similar set res/raw/scrap_config.properties.
 * Created by heaven7 on 2015/8/8.
 */
public class MainScrapView extends BaseScrapView {

    private long start;

    public MainScrapView(Context mContext) {
        super(mContext);
    }

    //return 0 indicate don't need top
    @Override
    protected int getTopLayoutId() {
        return 0;
    }

    @Override
    protected int getMiddleLayoutId() {
        return R.layout.scrap_middle_main;
    }
    //return 0 indicate don't need bottom
    @Override
    protected int getBottomLayoutId() {
        return 0;
    }

    @Override
    protected boolean onBackPressed() {
        if (ScrapHelper.isScrapViewAtBottom(this)) {
            long now = System.currentTimeMillis();
            if (now - start >= 500) {
                showToast("you must click twice in 500 ms to exit CommonView");
                start = now;
            } else {
                ScrapHelper.finishCurrentActivity();
            }
            return true;
        }
        return super.onBackPressed();
    }

    @Override
    protected void onAttach() {
        //after here i use volley to load image,so must init

        getViewHelper().setOnClickListener(R.id.bt, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrapHelper.beginTransaction().addBackAsTop(new EntryScrapView(v.getContext()))
                        .jump().commit();
            }
        });

        //String s = getResources().getDisplayMetrics().toString();
       // ScrapLog.i("getDisplayMetrics",s);
    }
}
