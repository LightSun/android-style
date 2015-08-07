package org.heaven7.scrap.core;

import android.view.View;

/**
 * the processor of the activity's view
 * Created by heaven7 on 2015/8/3.
 */
public interface IActivityViewProcessor {
    /**
     * replace the position's view of activity.
     * @param view  the view to replace
     * @param position the position to replace.
     */
    void replaceView(View view,ScrapPosition position);
}
