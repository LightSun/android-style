package org.heaven7.scrap.core.oneac;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * loading param , used for the loading view
 * Created by heaven7 on 2015/8/9.
 */
public class LoadingParam implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.showLoading ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showTop ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showBottom ? (byte) 1 : (byte) 0);
    }

    protected LoadingParam(Parcel in) {
        this.showLoading = in.readByte() != 0;
        this.showTop = in.readByte() != 0;
        this.showBottom = in.readByte() != 0;
    }

    public static final Creator<LoadingParam> CREATOR = new Creator<LoadingParam>() {
        @Override
        public LoadingParam createFromParcel(Parcel source) {
            return new LoadingParam(source);
        }

        @Override
        public LoadingParam[] newArray(int size) {
            return new LoadingParam[size];
        }
    };
}
