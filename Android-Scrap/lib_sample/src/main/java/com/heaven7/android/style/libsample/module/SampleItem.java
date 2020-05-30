package com.heaven7.android.style.libsample.module;

import android.os.Parcel;
import android.os.Parcelable;

import com.heaven7.adapter.BaseSelector;

public class SampleItem extends BaseSelector implements Parcelable {

    private String text;
    private int id;

    public SampleItem(String text, int id) {
        this.text = text;
        this.id = id;
    }

    public SampleItem(String text) {
       this(text, 0);
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeInt(this.id);
    }

    protected SampleItem(Parcel in) {
        this.text = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<SampleItem> CREATOR = new Parcelable.Creator<SampleItem>() {
        @Override
        public SampleItem createFromParcel(Parcel source) {
            return new SampleItem(source);
        }

        @Override
        public SampleItem[] newArray(int size) {
            return new SampleItem[size];
        }
    };
}
