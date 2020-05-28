package org.heaven7.scrap.core.oneac;

/**
 * loading param , used for the loading view
 * Created by heaven7 on 2015/8/9.
 */
public class LoadingParam {

    public boolean showLoading;
    public boolean showTop;
    public boolean showBottom;

    // public boolean showMiddle;
    //content/middle is not show ,if showLoading = true

    /**
     * @param showLoading   true to show loading
     * @param showTop       true to show top while showLoading = true,
     * @param showBottom    true to show bottom while showLoading = true;
     */
    public LoadingParam(boolean showLoading, boolean showTop, boolean showBottom) {
       set(showLoading,showTop,showBottom);
    }

    /**
     * @param showLoading   true to show loading
     * @param showTop       true to show top while showLoading = true,
     * @param showBottom    true to show bottom while showLoading = true;
     */
    public void set(boolean showLoading, boolean showTop, boolean showBottom){
        this.showLoading = showLoading;
        this.showTop = showTop;
        this.showBottom = showBottom;
    }

    @Override
    public String toString() {
        return "LoadingParam{" +
                "showLoading=" + showLoading +
                ", showTop=" + showTop +
                ", showBottom=" + showBottom +
                '}';
    }
}
