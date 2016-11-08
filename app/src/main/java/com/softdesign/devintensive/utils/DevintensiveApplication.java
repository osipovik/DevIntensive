package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.stetho.Stetho;
import com.softdesign.devintensive.data.storage.models.DaoMaster;
import com.softdesign.devintensive.data.storage.models.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by OsIpoFF on 29.06.16.
 */
public class DevintensiveApplication extends Application {
    private static SharedPreferences sSharedPreferences;
    private static Context sContext;
    private static DaoSession sDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sContext = getApplicationContext();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "devintensive-db");
        Database db = helper.getWritableDb();
        sDaoSession = new DaoMaster(db).newSession();

        Stetho.initializeWithDefaults(this);
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

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }
}
