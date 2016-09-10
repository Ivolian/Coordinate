package com.unicorn.coordinate.utils;

import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.TinyDB;
import com.unicorn.coordinate.user.model.UserInfo;

public class ConfigUtils {

    final static private String IP = "applink.chengshidingxiang.com";

    final static private String PORT = "80";

    public static String getBaseUrl() {
        return "http://" + IP + ":" + PORT;
    }

    public static String getImageBaseUrl() {
        return getBaseUrl() + "/upfiles/";
    }

    public static String getUserId() {
        return TinyDB.getNewInstance().getString(Constant.K_USER_ID);
    }

    public static boolean isLogin() {
        return getUserId() != null;
    }

    public static void removeUserInfo(){
        TinyDB.getNewInstance().remove(Constant.K_USER_ID);
    }

    public static void saveUserInfo(UserInfo userInfo){
        TinyDB tinyDB= TinyDB.getNewInstance();
        tinyDB.putString(Constant.K_USER_ID,userInfo.getUserid());
    }

//    public static String getBaseDirPath() {
//        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "hems");
//        if (!dir.exists()) {
//            boolean result = dir.mkdir();
//            if (!result) {
//                ToastUtils.show("创建基础目录失败!");
//            }
//        }
//        return dir.getAbsolutePath();
//    }

}
