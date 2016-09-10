package com.unicorn.coordinate.utils;

public class ConfigUtils {

    final static private String IP = "applink.chengshidingxiang.com";

    final static private String PORT = "80";

    public static String getBaseUrl() {
        return "http://" + IP + ":" + PORT;
    }

    public static String getImageBaseUrl() {
        return getBaseUrl() + "/upfiles/";
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
