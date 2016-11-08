package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.request.UserLoginRequest;
import com.softdesign.devintensive.data.network.response.UserListResponse;
import com.softdesign.devintensive.data.network.response.UserModelResponse;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.AppConfig;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by OsIpoFF on 26.09.16.
 */

public class AuthActivity extends BaseActivity implements View.OnClickListener {
    public final String TAG = getClass().getSimpleName();
    private DataManager mDataManager;
    private RepositoryDao mRepositoryDao;
    private UserDao mUserDao;

    @BindView(R.id.btn_login)
    Button loginBtn;

    @BindView(R.id.remember_password)
    TextView mRememberPassword;

    @BindView(R.id.main_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.email_et)
    EditText mLogin;

    @BindView(R.id.password_et)
    EditText mPassword;

    private ButterKnife.Action<Button> setBtnOnClickListener = new ButterKnife.Action<Button>() {
        @Override
        public void apply(@NonNull Button view, int index) {
            view.setOnClickListener(AuthActivity.this);
        }
    };

    private ButterKnife.Action<TextView> setTextOnClickListener =
            new ButterKnife.Action<TextView>() {
        @Override
        public void apply(@NonNull TextView view, int index) {
            view.setOnClickListener(AuthActivity.this);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mDataManager = DataManager.getInstance();
        mUserDao = mDataManager.getDaoSession().getUserDao();
        mRepositoryDao = mDataManager.getDaoSession().getRepositoryDao();

        ButterKnife.bind(this);
        ButterKnife.apply(loginBtn, setBtnOnClickListener);
        ButterKnife.apply(mRememberPassword, setTextOnClickListener);

        // TODO: 08.11.16 добавить проверку, если токен уже есть, то сразу переходим к списку
        // TODO: 08.11.16 обработать нажатие кнопки "забыл пароль" 
//        mRememberPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                signIn();
                break;
            case R.id.remember_password:
                rememberPassword();
                break;
        }
    }

    private void rememberPassword() {
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    private void loginSuccess(UserModelResponse userModel) {
        showSnackbar(mCoordinatorLayout, userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getId());
        saveUserName(userModel.getData().getUser());
        saveUserPhoto(userModel.getData().getUser().getPublicInfo());
        saveUserValues(userModel.getData().getUser().getProfileValues());
        saveUserFields(userModel.getData().getUser());
        // TODO: 08.11.16 вынести запись в базу в отдельный поток
        saveUsersToDB();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(AuthActivity.this, UserListActivity.class);
                startActivity(loginIntent);
            }
        }, AppConfig.START_DELAY);
    }

    private void signIn() {
        if (!NetworkStatusChecker.isNetworkAvailable(this)) {
            showSnackbar(mCoordinatorLayout, "Network is not available, please try later.");
            return;
        }

        Call<UserModelResponse> call = mDataManager.loginUser(new UserLoginRequest(
                mLogin.getText().toString(), mPassword.getText().toString()));
        call.enqueue(new Callback<UserModelResponse>() {
            @Override
            public void onResponse(Call<UserModelResponse>  call,
                                   Response<UserModelResponse> response) {
                switch (response.code()) {
                    case 200:
                        loginSuccess(response.body());
                        break;
                    case 404:
                        showSnackbar(mCoordinatorLayout, "Incorrect email or password");
                        break;
                    default:
                        showSnackbar(mCoordinatorLayout, "WTF?!?!?!");
                }
            }

            @Override
            public void onFailure(Call<UserModelResponse> call, Throwable t) {
                // TODO: 05.11.16 обработать ошибки ретрофита
            }
        });
    }

    private void saveUserName(UserModelResponse.User user) {
        mDataManager.getPreferencesManager()
                .saveUserName(user.getFirstName() + " " + user.getSecondName());
    }

    private void saveUserPhoto(UserModelResponse.PublicInfo userPublicInfo) {
        mDataManager.getPreferencesManager().saveUserPhoto(Uri.parse(userPublicInfo.getPhoto()));
    }

    private void saveUserValues(UserModelResponse.ProfileValues profileValues) {
        int[] userValues = {
                profileValues.getRating(),
                profileValues.getLinesCode(),
                profileValues.getProjects()
        };

        mDataManager.getPreferencesManager().saveUserProfileValues(userValues);
    }

    private void saveUserFields(UserModelResponse.User user) {
        List<String> userFields = new ArrayList<String>();

        userFields.add(user.getContacts().getPhone());
        userFields.add(user.getContacts().getEmail());
        userFields.add(user.getContacts().getVk());
        userFields.add(user.getRepositories().getRepo().get(0).getGit());
        userFields.add(user.getPublicInfo().getBio());

        mDataManager.getPreferencesManager().saveUserProfileData(userFields);
    }

    private void saveUsersToDB() {
        Call<UserListResponse> call = mDataManager.getUserListFromNetwork();

        call.enqueue(new Callback<UserListResponse>() {

            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                try {
                    if (response.code() == 200) {
                        List<Repository> allRepositories = new ArrayList<>();
                        List<User> allUsers = new ArrayList<>();

                        for (UserListResponse.UserData userRes : response.body().getData()) {
                            allRepositories.addAll(getUserRepoList(userRes));
                            allUsers.add(new User(userRes));
                        }

                        mRepositoryDao.insertOrReplaceInTx(allRepositories);
                        mUserDao.insertOrReplaceInTx(allUsers);
                    } else {
                        showSnackbar(mCoordinatorLayout, "список пользователейй не может быть получен");
                        Log.d(TAG, "onResponse " + String.valueOf(response.errorBody().source()));
                    }
                } catch (NullPointerException e) {

                }
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {

            }
        });
    }

    private List<Repository> getUserRepoList(UserListResponse.UserData userData) {
        final String userId = userData.getId();

        List<Repository> repositories = new ArrayList<>();

        for (UserModelResponse.Repo repositoryRes : userData.getRepositories().getRepo()) {
            repositories.add(new Repository(repositoryRes, userId));
        }

        return repositories;
    }
}
