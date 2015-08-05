package org.heaven7.scrap.sample.scrapview;

import android.content.Context;
import android.view.View;

import org.heaven7.scrap.core.ScrapPosition;
import org.heaven7.scrap.sample.R;
import org.heaven7.scrap.util.ScrapHelper;

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
    protected int getMiddleLayoutId() {
        return R.layout.scrap_middle_test_visibile;
    }

    @Override
    protected void onAttach() {
       getViewHelper().setOnClickListener(R.id.bt_test1, new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ScrapHelper.setVisibility(ScrapPosition.Bottom, true);
           }
       }).setOnClickListener(R.id.bt_test2, new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ScrapHelper.setVisibility(ScrapPosition.Bottom, false);
           }
       }).setOnClickListener(R.id.bt_test3, new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ScrapHelper.toogleVisibility(ScrapPosition.Bottom);
           }
       });
    }
}
