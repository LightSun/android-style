package org.heaven7.scrap.core;

/**
 * there is some constants used to BroadcastReceive.
 * Created by heaven7 on 2015/8/3.
 */
public interface ScrapConstant {

    String KEY_BUNDLE                          = "bundle";

    String CATEGORY_SCRAP                      = "com.heaven7.scrap.category_life_cycle";
    String ACTION_ACTIVITY_ON_CREATE           = "com.heaven7.scrap.activity.action_onCreate";
    String ACTION_ACTIVITY_ON_POST_CREATE      = "com.heaven7.scrap.activity.action_onPostCreate";
    String ACTION_ACTIVITY_ON_START            = "com.heaven7.scrap.activity.action_onStart";
    String ACTION_ACTIVITY_ON_RESUME           = "com.heaven7.scrap.activity.action_onResume";
    String ACTION_ACTIVITY_ON_PAUSE            = "com.heaven7.scrap.activity.action_onPause";
    String ACTION_ACTIVITY_ON_STOP             = "com.heaven7.scrap.activity.action_onStop";
    String ACTION_ACTIVITY_ON_DESTROY          = "com.heaven7.scrap.activity.action_onDestroy";
}
