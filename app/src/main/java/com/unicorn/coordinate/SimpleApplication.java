package com.unicorn.coordinate;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.unicorn.coordinate.task.TaskHelper;
import com.unicorn.coordinate.task.model.DaoMaster;
import com.unicorn.coordinate.task.model.DaoSession;
import com.unicorn.coordinate.task.model.PointDao;
import com.unicorn.coordinate.volley.SimpleVolley;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import net.danlew.android.joda.JodaTimeAndroid;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;


public class SimpleApplication extends Application {

    private static SimpleApplication instance;

    public static SimpleApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CustomActivityOnCrash.install(this);
        instance = this;
        doSomeInit();
        doSomeWork();
    }

    private void doSomeInit() {
        initGreenDao();
        SimpleVolley.init(instance);
        JodaTimeAndroid.init(instance);
        ZXingLibrary.initDisplayOpinion(instance);
    }

    private void doSomeWork() {
        TaskHelper.getTask();
    }

    //

    private DaoSession daoSession;

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "coordinate-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public PointDao getPointDao() {
        return daoSession.getPointDao();
    }


}
