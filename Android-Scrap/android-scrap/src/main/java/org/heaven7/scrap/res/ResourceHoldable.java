package org.heaven7.scrap.res;

import android.content.Context;
import android.graphics.Bitmap;

public interface ResourceHoldable {

    Context getContext();

    CharSequence getText(String resName);

    Bitmap getBitmap(String resName);

    Bitmap getBitmap(String resName, int width, int height);

    int getId(String resName);

    int getStringId(String resName);

    int getLayoutId(String resName);

    int getDrawableId(String resName);

    int getStyleId(String resName);

    int getAnimId(String resName);

    int getColorId(String resName);

    int getDimenId(String resName);

    int getRawId(String resName);

    int getStringArrayId(String resName);

}
