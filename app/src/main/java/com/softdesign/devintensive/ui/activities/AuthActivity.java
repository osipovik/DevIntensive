package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.request.UserLoginRequest;
import com.softdesign.devintensive.data.network.response.UserModelResponse;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by OsIpoFF on 26.09.16.
 */

public class AuthActivity extends BaseActivity implements View.OnClickListener {
    private DataManager mDataManager;

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

        ButterKnife.bind(this);
        ButterKnife.apply(loginBtn, setBtnOnClickListener);
        ButterKnife.apply(mRememberPassword, setTextOnClickListener);
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
        saveUserValues(userModel);

        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
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

    private void saveUserValues(UserModelResponse userModel) {
        int[] userValues = {
                userModel.getData().getUser().getProfileValues().getRaiting(),
                userModel.getData().getUser().getProfileValues().getLinesCode(),
                userModel.getData().getUser().getProfileValues().getProjects()
        };

        mDataManager.getPreferencesManager().saveUserProfileValues(userValues);
    }
}
