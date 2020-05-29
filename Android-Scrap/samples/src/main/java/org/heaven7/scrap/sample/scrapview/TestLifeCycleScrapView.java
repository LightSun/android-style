package org.heaven7.scrap.sample.scrapview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.heaven7.scrap.core.lifecycle.ActivityLifeCycleAdapter;
import org.heaven7.scrap.core.lifecycle.IActivityLifeCycleCallback;
import org.heaven7.scrap.core.oneac.ScrapHelper;
import org.heaven7.scrap.sample.R;
import org.heaven7.scrap.sample.ScrapLog;
import org.heaven7.scrap.sample.util.DialogUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * test the stand life cycle of activity.
 * <li>Note: Because this view is attached to the Activity. so activity is created.
 * some methods will not called.</li>
 * if you want register global  life cycle callback (.please use
 * {@link ScrapHelper#registerActivityLifeCycleCallback(IActivityLifeCycleCallback...)}
 * and {@link ScrapHelper#unregisterActivityLifeCycleCallback(IActivityLifeCycleCallback...)}
 * it has more methods.
 * Created by heaven7 on 2015/8/4.
 */
public class TestLifeCycleScrapView extends CommonView {

    private IActivityLifeCycleCallback callback ;

    public TestLifeCycleScrapView(Context mContext) {
        super(mContext);
    }

    @Override
    protected void onPostInit() {
        // offen register it in onAttach. but here we want to see the life cycle . so register in init.
        callback = new ActivityLifeCycleAdapter() {
            @Override
            public void onActivityCreate(Activity activity, Bundle savedInstanceState) {
                ScrapLog.i("callback  onActivityCreate","");
            }

            @Override
            public void onActivityPostCreate(Activity activity, Bundle savedInstanceState) {
                ScrapLog.i("callback  onActivityPostCreate","");
            }

            @Override
            public void onActivityStart(Activity activity) {
                ScrapLog.i("callback  onActivityStart","");
            }

            @Override
            public void onActivityResume(Activity activity) {
                ScrapLog.i("callback  onActivityResume","");
            }

            @Override
            public void onActivityPause(Activity activity) {
                ScrapLog.i("callback  onActivityPause","");
            }

            @Override
            public void onActivityStop(Activity activity) {
                ScrapLog.i("callback  onActivityStop","");
            }

            @Override
            public void onActivityDestroy(Activity activity) {
                ScrapLog.i("callback  onActivityDestroy","");
            }
            @Override
            public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
                ScrapLog.i("callback  OnActivityResult","");
            }
            //....etc methods
        };
        // register global life cycle callback
        ScrapHelper.registerActivityLifeCycleCallback(callback);
    }

    @Override
    protected void onAttach() {
        // use dialog to help you test it, or else you will see nothing it this.
        TextView mBt1 = getView(R.id.bt_1);
        getView(R.id.bt_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testLifeCycle();
            }
        });

        getView(R.id.bt_2).setVisibility(View.GONE);
        getView(R.id.bt_3).setVisibility(View.GONE);
        mBt1.setText("Click this button to show auto Dialog and test life cycle");
    }

    @Override
    protected void onDetach() {
        if(callback!=null) {
            ScrapHelper.unregisterActivityLifeCycleCallback(callback);
            callback = null;
        }
    }

    private void testLifeCycle() {
        DialogUtil.showProgressDialog((Activity) getContext());
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                DialogUtil.dismiss();
            }
        }, 2000);
    }

    @Override
    protected void onActivityCreate(Bundle saveInstanceState) {
        ScrapLog.i("onActivityCreate",saveInstanceState.toString());
    }

    @Override
    protected void onActivityPostCreate(Bundle saveInstanceState) {
        ScrapLog.i("onActivityPostCreate",saveInstanceState.toString());
    }

    @Override
    protected void onActivityStart() {
        ScrapLog.i("onActivityStart","");
    }

    @Override
    protected void onActivityResume() {
        ScrapLog.i("onActivityResume","");
    }

    @Override
    protected void onActivityPause() {
        ScrapLog.i("onActivityPause","");
    }

    @Override
    protected void onActivityStop() {
        ScrapLog.i("onActivityStop","");
    }

    @Override
    protected void onActivityDestroy() {
        ScrapLog.i("onActivityDestroy","");
    }
}
