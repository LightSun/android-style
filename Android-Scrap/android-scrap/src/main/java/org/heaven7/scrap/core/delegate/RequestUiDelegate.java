package org.heaven7.scrap.core.delegate;

import com.heaven7.android.component.network.NetworkComponent;
import com.heaven7.android.component.network.NetworkContext;


public abstract class RequestUiDelegate<T> extends StyledUiDelegate{

    private final NetworkContext mNetworkContext;

    public RequestUiDelegate(NetworkContext mNetworkContext) {
        this.mNetworkContext = mNetworkContext;
    }

    public NetworkContext getNetworkContext() {
        return mNetworkContext;
    }

    public NetworkComponent getNetworkComponent() {
        return mNetworkContext.getNetworkComponent();
    }

    @Override
    public void onDestroy() {
        NetworkComponent com = getNetworkComponent();
        if(com != null){
            com.cancelAll();
        }
        super.onDestroy();
    }

    public void request(RequestCallback<T> callback){
        request(false, callback);
    }

    public abstract void request(boolean refresh, RequestCallback<T> callback);


    public interface RequestCallback<T>{

        default void onStart(){};
        default void onEnd(){};

        void onSuccess(T data);
        void onFailed(Throwable e);
    }
}
