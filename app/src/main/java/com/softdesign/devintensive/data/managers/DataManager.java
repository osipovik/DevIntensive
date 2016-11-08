package com.softdesign.devintensive.data.managers;

import android.content.Context;
import android.net.Uri;

import com.softdesign.devintensive.data.network.PicassoCache;
import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.request.UserLoginRequest;
import com.softdesign.devintensive.data.network.response.UserListResponse;
import com.softdesign.devintensive.data.network.response.UserModelResponse;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    private Context mContext;
    private PreferencesManager mPreferencesManager;
    private RestService mRestService;
    private Picasso mPicasso;
    private DaoSession mDaoSession;

    private DataManager() {
        this.mContext = DevintensiveApplication.getContext();
        this.mPreferencesManager = new PreferencesManager();
        this.mRestService = ServiceGenerator.createService(RestService.class);
        this.mPicasso = new PicassoCache(mContext).getPicassoInstance();
        this.mDaoSession = DevintensiveApplication.getDaoSession();
    }

    public static DataManager getInstance() {
        if (sInstance == null) {
            sInstance = new DataManager();
        }

        return sInstance;
    }

    public Picasso getPicasso() {
        return mPicasso;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
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

    public Call<UserListResponse> getUserListFromNetwork() {
        return mRestService.getUserList();
    }

    //endregion

    //region ================== Database =====================

    public List<User> getUserListFromDb() {
        List<User> userList = new ArrayList<>();

        try {
            userList = mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.CodeLines.gt(0))
                    .orderDesc(UserDao.Properties.CodeLines)
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList;
    }

    public List<User> getUserListByName(String name) {
        List<User> userList = new ArrayList<>();

        try {
            userList = mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.Rating.gt(0),
                            UserDao.Properties.SearchName.like("%" + name.toUpperCase() + "%"))
                    .orderDesc(UserDao.Properties.CodeLines)
                    .build()
                    .list();
        } catch (Exception e) {

        }

        return userList;
    }


    //endregion
}
