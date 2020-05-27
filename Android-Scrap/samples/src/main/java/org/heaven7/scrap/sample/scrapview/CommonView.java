package org.heaven7.scrap.sample.scrapview;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.heaven7.scrap.adapter.QuickAdapter;
import org.heaven7.scrap.core.BaseScrapView;
import org.heaven7.scrap.core.ViewHelper;
import org.heaven7.scrap.sample.R;
import org.heaven7.scrap.util.ArrayList2;

import java.util.List;

/**
 * Created by heaven7 on 2015/8/3.
 */
public class CommonView extends BaseScrapView {

    public CommonView(Context mContext) {
        super(mContext);
    }

    @Override
    protected int getTopLayoutId() {
        return R.layout.scrap_page_1_top;
    }

    @Override
    protected int getMiddleLayoutId() {
        return R.layout.scrap_page_1_middle;
    }

    @Override
    protected int getBottomLayoutId() {
        return R.layout.scrap_page_1_bottom;
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        showToast("CommonView is attached");
        getViewHelper().setOnClickListener(R.id.iv_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }).setOnClickListener(R.id.bt_1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("button1 was clicked");
            }
        }).setOnClickListener(R.id.bt_2, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("button2 was clicked");
            }
        }).setOnClickListener(R.id.bt_3, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("button3 was clicked");
            }
        });
        //set the list view's data
        //use QuickAdapter to fast set adapter.
        showGirl();
    }

    protected void showGirl() {
        addGirlDatas();
        getViewHelper().setAdapter(R.id.lv, new QuickAdapter<GirlData>(R.layout.item_girl,mGirlData) {
            @Override
            protected void convert(Context context, int position, ViewHelper viewHelper, GirlData item) {
                   viewHelper.setText(R.id.tv,item.name);
                ImageView view = viewHelper.getView(R.id.eniv);
                Glide.with(view).load(item.imageUrl).into(view);
            }
        });
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        mGirlData.clear();
    }

    private void addGirlDatas() {
        String url = "http://images.ali213.net/picfile/pic/2012-06-18/927_h1ali213-page-34.jpg";
        mGirlData.add(new GirlData(url,"girl_1"));
        url = "http://www.2cto.com/uploadfile/2013/0407/20130407080828809.jpg";
        mGirlData.add(new GirlData(url,"girl_2"));
        url = "http://bagua.40407.com/uploads/allimg/130712/5-130G2141257.jpg";
        mGirlData.add(new GirlData(url,"girl_3"));
        url = "http://pic2.52pk.com/files/131213/1283314_094919_1430.jpg";
        mGirlData.add(new GirlData(url,"girl_4"));
    }

    private final List<GirlData> mGirlData = new ArrayList2<>();

    public static class GirlData{
        public String imageUrl;
        public String name;

        public GirlData(String imageUrl, String name) {
            this.imageUrl = imageUrl;
            this.name = name;
        }
    }
}
