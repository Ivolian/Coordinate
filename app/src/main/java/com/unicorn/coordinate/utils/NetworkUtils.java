package com.unicorn.coordinate.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.unicorn.coordinate.SimpleApplication;


public class NetworkUtils {

    public static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) SimpleApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

}
