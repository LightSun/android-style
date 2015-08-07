package org.heaven7.scrap.sample.scrapview;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;

import org.heaven7.scrap.core.event.ActivityEventAdapter;
import org.heaven7.scrap.core.event.IActivityEventCallback;
import org.heaven7.scrap.sample.ScrapLog;
import org.heaven7.scrap.util.ScrapHelper;

/**
 * Created by heaven7 on 2015/8/4.
 */
public class TestKeyEventScrapView extends CommonView {

    private IActivityEventCallback callback;

    public TestKeyEventScrapView(Context mContext) {
        super(mContext);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
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
        ScrapHelper.registerActivityEventCallback(callback);
    }

    @Override
    protected void onDetach() {
        if(callback!=null){
            ScrapHelper.unregisterActivityEventCallback(callback);
            callback = null;
        }
    }
}
