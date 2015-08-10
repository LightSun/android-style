package org.heaven7.scrap.sample.scrapview;

import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by heaven7 on 2015/8/9.
 */
public class TestLoadingScrapView extends CommonView {

    private Timer timer;

    public TestLoadingScrapView(Context mContext) {
        super(mContext);
    }

    @Override
    protected void onAttach() {
        // if you want only show loading.( hide top and bottom, please use  setShowLoading(LoadingParam loadingParam) )
        setShowLoading(true);
        //simulate the time-consuming work ,after 5000 mills. show it
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(r);
            }
        },5000);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
    }

    private final Runnable r = new Runnable() {
        @Override
        public void run() {
            showGirl();
            setShowLoading(false);
        }
    };
}
