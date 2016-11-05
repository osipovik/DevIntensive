package com.softdesign.devintensive.data.managers;

import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.request.UserLoginRequest;
import com.softdesign.devintensive.data.network.response.UserModelResponse;

import retrofit2.Call;

/**
 * Created by OsIpoFF on 29.06.16.
 */
public class DataManager {
    private static DataManager sInstance = null;

    private PreferencesManager mPreferencesManager;
    private RestService mRestService;

    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
        this.mRestService = ServiceGenerator.createService(RestService.class);
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

    //region ================== Network =====================

    public Call<UserModelResponse> loginUser(UserLoginRequest userLoginRequest) {
        return mRestService.loginUser(userLoginRequest);
    }

    //endregion

    //region ================== Database =====================

    //endregion
}
