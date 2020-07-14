package org.heaven7.scrap.sample;

import android.content.Context;
import android.view.View;

import com.heaven7.core.util.Toaster;

import org.heaven7.scrap.core.oneac.BaseScrapView;
import org.heaven7.scrap.core.oneac.ScrapHelper;
import org.heaven7.scrap.sample.scrapview.TestTransitionScrapView_exit;

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

    public void showToast(String msg) {
        Toaster.show(getContext(), msg);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.scrap_main;
    }

    @Override
    protected boolean onBackPressed() {
        if (ScrapHelper.isScrapViewAtBottom(this)) {
            if(start == 0){
                start = System.currentTimeMillis();
                showToast("click again to exit MainScrapView!");
                return true;
            }
            long now = System.currentTimeMillis();
            if (now - start >= 500) {
                showToast("click again to exit MainScrapView!");
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

        getView(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrapHelper.beginTransaction()
                        .addBackAsTop(new TestTransitionScrapView_exit(v.getContext()))
                        .jump()
                        .commit();
            }
        });

        //String s = getResources().getDisplayMetrics().toString();
       // ScrapLog.i("getDisplayMetrics",s);
    }
}
