package com.example.ashishpanjwani.timeperfectagain.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class TimePerfectUtil {

    private Context mContext=null;

    //Public Constructor that takes mContext for later use
    public TimePerfectUtil(Context con){
        mContext=con;
    }

    /**
     * Encode user email to use it as a firebase key (Firebase does not allow "." in the key name)
     * Encoded email is also used as "userEmail",list and item "owner" values
     */

    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".",",");
    }

    //This is a method to check if the device internet connection is currently on
    public boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo=connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
