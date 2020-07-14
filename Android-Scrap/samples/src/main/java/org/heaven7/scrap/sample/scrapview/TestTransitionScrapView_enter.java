package org.heaven7.scrap.sample.scrapview;

import android.content.Context;

import org.heaven7.scrap.core.oneac.BaseScrapView;
import org.heaven7.scrap.sample.R;

public class TestTransitionScrapView_enter extends BaseScrapView {

    public TestTransitionScrapView_enter(Context mContext) {
        super(mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.scrap_test_transition_enter;
    }
}
