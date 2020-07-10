package org.heaven7.scrap.sample.scrapview;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.heaven7.adapter.BaseSelector;
import com.heaven7.adapter.QuickRecycleViewAdapter;
import com.heaven7.adapter.util.ViewHelper2;
import com.heaven7.core.util.Toaster;

import org.heaven7.scrap.core.oneac.BaseScrapView;
import org.heaven7.scrap.sample.R;
import org.heaven7.scrap.util.ArrayList2;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by heaven7 on 2015/8/3.
 */
public class CommonView extends BaseScrapView {


    public CommonView(Context mContext) {
        super(mContext);
    }

    @Override
    protected int getLayoutId() {
        //TODO test KeyEvent. LifeCycle,
        return 0;
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        ButterKnife.bind(this, getView());
        showToast("CommonView is attached");
        //set the list view's data
        //use QuickAdapter to fast set adapter.
        showGirl();
    }
    public void showToast(String msg) {
        Toaster.show(getContext(), msg);
    }
    @Optional
    @OnClick(R.id.iv_back)
    public void onClickBack(View view){
        onBackPressed();
    }

    protected void showGirl() {
        addGirlDatas();

        RecyclerView view = null; //getView(R.id.lv);
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        view.setAdapter(new QuickRecycleViewAdapter<GirlData>(R.layout.item_girl,mGirlData) {
            @Override
            protected void onBindData(Context context, int position, GirlData item, int layoutId, ViewHelper2 viewHelper) {
                viewHelper.setText(R.id.tv,item.name);
                ImageView view = viewHelper.getView(R.id.eniv);
                Glide.with(view).load(item.imageUrl).into(view);
            }

        });
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

    public static class GirlData extends BaseSelector {
        public String imageUrl;
        public String name;

        public GirlData(String imageUrl, String name) {
            this.imageUrl = imageUrl;
            this.name = name;
        }
    }
}
