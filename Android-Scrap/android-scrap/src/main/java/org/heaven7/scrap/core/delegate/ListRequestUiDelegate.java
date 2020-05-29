package org.heaven7.scrap.core.delegate;


import android.content.Intent;
import android.os.Bundle;

import com.heaven7.android.component.network.NetworkContext;
import com.heaven7.android.component.network.RequestConfig;
import com.heaven7.android.component.network.list.ListHelper;
import com.heaven7.android.pullrefresh.FooterDelegate;
import com.heaven7.android.pullrefresh.PullToRefreshLayout;

import org.heaven7.scrap.R;

import java.util.Map;

public abstract class ListRequestUiDelegate<T> extends RequestUiDelegate<T> implements ListHelper.Callback {

    private final ListHelper<T> mListHelper;
    private PullToRefreshLayout mPullLayout;

    public ListRequestUiDelegate(NetworkContext context, ListHelper.Factory factory) {
        super(context);
        mListHelper = new ListHelper<T>(context, factory, this);
    }

    public ListHelper<T> getListHelper() {
        return mListHelper;
    }

    @Override
    public FooterDelegate getFooterDelegate() {
        return null;
    }
    @Override
    public PullToRefreshLayout getPullToRefreshLayout() {
        if(mPullLayout == null){
            mPullLayout = findViewById(R.id.lib_style_pullToRefresh);
        }
        return mPullLayout;
    }
    @Override
    public void addRequestParams(Map<String, Object> params) {

    }
    @Override
    public void request(boolean refresh, RequestCallback<T> callback) {
        throw new UnsupportedOperationException();
    }

    public void request(boolean refresh){
        mListHelper.requestData(refresh);
    }
    public void refresh(){
        mListHelper.refresh();
    }

    @Override
    public void onInitialize(Intent intent, Bundle savedInstanceState) {
        super.onInitialize(intent, savedInstanceState);
        mListHelper.onInitialize(getActivity(), savedInstanceState);

        refresh();
    }

    @Override
    public abstract RequestConfig onCreateRequestConfig();
}
