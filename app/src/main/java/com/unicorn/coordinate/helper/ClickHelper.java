package com.unicorn.coordinate.helper;


public class ClickHelper {

    private static long lastClickTime;

    public synchronized static boolean isSafe() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime < 500) {
            return false;
        }
        lastClickTime = currentTime;
        return true;
    }

}
