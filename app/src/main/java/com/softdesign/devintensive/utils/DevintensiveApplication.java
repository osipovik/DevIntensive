package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by OsIpoFF on 29.06.16.
 */
public class DevintensiveApplication extends Application {
    public static SharedPreferences sSharedPreferences;
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sContext = getApplicationContext();
    }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    /**
     * Return application context
     * @return Context
     */
    public static Context getContext() {
        return sContext;
    }
}
