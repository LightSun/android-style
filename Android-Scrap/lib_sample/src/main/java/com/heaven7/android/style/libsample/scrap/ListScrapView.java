package com.heaven7.android.style.libsample.scrap;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heaven7.adapter.QuickRecycleViewAdapter;
import com.heaven7.adapter.util.ViewHelper2;
import com.heaven7.android.style.libsample.OnClickSampleItemListener;
import com.heaven7.android.style.libsample.R;
import com.heaven7.android.style.libsample.SampleConstants;
import com.heaven7.android.style.libsample.module.SampleItem;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.Reflector;

import org.heaven7.scrap.core.oneac.BaseScrapView;

import java.util.ArrayList;

/**
 * need parameters
 * @see SampleConstants#KEY_LIST
 * @see SampleConstants#KEY_ITEM_LISTENER
 * @author heaven7
 */
public class ListScrapView extends BaseScrapView {

    RecyclerView mRv;

    public ListScrapView(Context mContext) {
        super(mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lib_sample_list;
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        ArrayList<SampleItem> items = getArguments().getParcelableArrayList(SampleConstants.KEY_LIST);
        String cn = getArguments().getString(SampleConstants.KEY_ITEM_LISTENER);
        String title = getArguments().getString(SampleConstants.KEY_TITLE);
        if(Predicates.isEmpty(items)){
            throw new IllegalStateException("must set sample items");
        }
        final OnClickSampleItemListener l;
        try {
             l = Reflector.from(Class.forName(cn)).newInstance();
        } catch (ClassNotFoundException e) {
           throw new RuntimeException(e);
        }
        TextView tv = getView(R.id.tv_title);
        tv.setText(title);
        if(title == null){
            tv.setVisibility(View.GONE);
        }

        mRv = getView(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mRv.setAdapter(new QuickRecycleViewAdapter<SampleItem>(R.layout.lib_sample_item_button, items) {
            @Override
            protected void onBindData(Context context, int position, final SampleItem item, int itemLayoutId, ViewHelper2 helper) {
                helper.setText(R.id.bt, item.getText())
                        .setRootOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                l.onClickSample(getContext(), item);
                            }
                        });
            }
        });
    }

}
