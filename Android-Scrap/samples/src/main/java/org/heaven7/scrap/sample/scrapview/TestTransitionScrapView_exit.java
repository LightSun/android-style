package org.heaven7.scrap.sample.scrapview;

import android.content.Context;

import com.heaven7.core.util.MainWorker;

import org.heaven7.scrap.core.anim.TransitionAnimateExecutor;
import org.heaven7.scrap.core.oneac.BaseScrapView;
import org.heaven7.scrap.core.oneac.ScrapHelper;
import org.heaven7.scrap.sample.R;

public class TestTransitionScrapView_exit extends BaseScrapView {

    public TestTransitionScrapView_exit(Context mContext) {
        super(mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.scrap_test_transition_exit;
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        MainWorker.postDelay(1000, new Runnable() {
            @Override
            public void run() {
                ScrapHelper.beginTransaction()
                        .addBackAsTop(new TestTransitionScrapView_enter(getContext()))
                        .animateExecutor(new TransitionAnimateExecutor(R.id.iv_trans))
                        .jump()
                        .commit();
            }
        });
    }
}
