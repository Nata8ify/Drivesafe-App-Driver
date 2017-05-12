package com.senior.g40.drivesafe.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by PNattawut on 23-Apr-17.
 */

public class SettingVerify {
    public static boolean isNetworkConnected(Context context){
        return ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
