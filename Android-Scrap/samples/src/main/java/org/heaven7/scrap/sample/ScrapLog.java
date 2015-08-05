package org.heaven7.scrap.sample;

import android.util.Log;

/**
 * Created by heaven7 on 2015/8/4.
 */
public class ScrapLog {

    private static final String TAG =  "ScrapLog";

    public static void i(String method,String msg){
        Log.i(TAG,"called [ "+method+" ]: "+msg);
    }
}
