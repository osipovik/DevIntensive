package com.softdesign.devintensive.data.managers;

import android.net.Uri;

import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.request.UserLoginRequest;
import com.softdesign.devintensive.data.network.response.UserModelResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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

    public Call<ResponseBody> uploadImage(Uri fileUri) {
        File file = new File(fileUri.getPath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        return mRestService.uploadImage(getPreferencesManager().getUserId(), body);
    }

    //endregion

    //region ================== Database =====================

    //endregion
}
