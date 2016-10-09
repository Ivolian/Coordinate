package com.unicorn.coordinate.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;

import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.TinyDB;
import com.unicorn.coordinate.task.PointHelper;
import com.unicorn.coordinate.user.LoginActivity;
import com.unicorn.coordinate.user.model.UserInfo;

import java.io.File;

public class ConfigUtils {

//    final static private String IP = "applink.chengshidingxiang.com";

//    final static private String PORT = "80";

    final static private String TEST_IP = "139.224.69.49";

    final static private String TEST_PORT = "9002";

    public static String getBaseUrl() {
        return "http://" + TEST_IP + ":" + TEST_PORT;
    }

    public static String getImageBaseUrl() {
        return getBaseUrl() + "/upfiles/";
    }


    //

    public static void saveUserInfo(UserInfo userInfo) {
        TinyDB tinyDB = TinyDB.getNewInstance();
        tinyDB.putObject(Constant.K_USER_INFO, userInfo);
    }

    public static UserInfo getUserInfo() {
        TinyDB tinyDB = TinyDB.getNewInstance();
        return (UserInfo) tinyDB.getObject(Constant.K_USER_INFO, UserInfo.class);
    }

    public static boolean notLogin() {
        return getUserInfo() == null;
    }

    public static boolean checkLogin(Activity activity) {
        if (notLogin()) {
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
            return false;
        }
        return true;
    }

    public static String getUserId() {
        if (notLogin()) {
            return "";
        }
        return getUserInfo().getUserid();
    }

    public static void logout() {
        TinyDB tinyDB = TinyDB.getNewInstance();
        tinyDB.remove(Constant.K_USER_INFO);

        PointHelper.clearPointInfo();
    }

    public static String getAccount() {
        return TinyDB.getNewInstance().getString(Constant.K_ACCOUNT);
    }

    public static void saveAccount(String account) {
        TinyDB.getNewInstance().putString(Constant.K_ACCOUNT, account);
    }

    public static String getBaseDirPath() {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Coordinate");
        if (!dir.exists()) {
            boolean result = dir.mkdir();
            if (!result) {
                ToastUtils.show("创建基础目录失败!");
            }
        }
        return dir.getAbsolutePath();
    }

}
