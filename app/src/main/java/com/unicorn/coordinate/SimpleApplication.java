package com.unicorn.coordinate;

import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDexApplication;

import com.unicorn.coordinate.task.TaskHelper;
import com.unicorn.coordinate.task.model.DaoMaster;
import com.unicorn.coordinate.task.model.DaoSession;
import com.unicorn.coordinate.task.model.PointDao;
import com.unicorn.coordinate.volley.SimpleVolley;

import net.danlew.android.joda.JodaTimeAndroid;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;


public class SimpleApplication extends MultiDexApplication {


    // ======================== instance =========================

    private static SimpleApplication instance;

    public static SimpleApplication getInstance() {
        return instance;
    }


    // ======================== onCreate =========================

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
        doSomeWork();
    }

    private void init() {
        CustomActivityOnCrash.install(this);
        initGreenDao();
        SimpleVolley.init(instance);
        JodaTimeAndroid.init(instance);
    }

    private void doSomeWork() {
        TaskHelper.getTask();
    }


    // ======================== GreenDao =========================

    private DaoSession daoSession;

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "coordinate-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public PointDao getPointDao() {
        return daoSession.getPointDao();
    }


}
