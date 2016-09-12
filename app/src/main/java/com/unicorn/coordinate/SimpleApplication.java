package com.unicorn.coordinate;

import android.app.Application;

import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.volley.SimpleVolley;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import net.danlew.android.joda.JodaTimeAndroid;


public class SimpleApplication extends Application {

    private static SimpleApplication instance;

    public static SimpleApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        doSomeInitJob();
    }

    private void doSomeInitJob() {
        SimpleVolley.init(instance);
        JodaTimeAndroid.init(instance);
        ZXingLibrary.initDisplayOpinion(instance);
    }

}
