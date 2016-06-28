package com.softdesign.devintensive.data.managers;

/**
 * Created by OsIpoFF on 29.06.16.
 */
public class DataManager {
    private static DataManager sInstance = null;

    private PreferencesManager mPreferencesManager;

    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
    }

    public static DataManager getInstance() {
        if (sInstance == null) {
            sInstance = new DataManager();
        }

        return sInstance;
    }

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }
}
