package org.heaven7.scrap.sample.scrapview;

import android.content.Context;
import android.view.View;

import org.heaven7.scrap.core.oneac.ScrapHelper;
import org.heaven7.scrap.sample.R;

/**
 * this is sample to test visibility of view which is indicate by the ScrapPosition.
 * Created by heaven7 on 2015/8/4.
 */
public class TestVisivilityScrapView extends CommonView {

    public TestVisivilityScrapView(Context mContext) {
        super(mContext);
    }

    @Override
    protected void onPostInit() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.scrap_middle_test_visibile;
    }

    @Override

    protected void onAttach() {
        getView(R.id.bt_test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrapHelper.setVisibility(true);
            }
        });
        getView(R.id.bt_test2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrapHelper.setVisibility(false);
            }
        });
    }
}
