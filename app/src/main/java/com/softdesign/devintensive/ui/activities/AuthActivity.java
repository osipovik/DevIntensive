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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by OsIpoFF on 26.09.16.
 */

public class AuthActivity extends BaseActivity implements View.OnClickListener {
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

    ButterKnife.Action<Button> setBtnOnClickListener = new ButterKnife.Action<Button>() {
        @Override
        public void apply(@NonNull Button view, int index) {
            view.setOnClickListener(AuthActivity.this);
        }
    };

    ButterKnife.Action<TextView> setTextOnClickListener = new ButterKnife.Action<TextView>() {
        @Override
        public void apply(@NonNull TextView view, int index) {
            view.setOnClickListener(AuthActivity.this);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ButterKnife.bind(this);
        ButterKnife.apply(loginBtn, setBtnOnClickListener);
        ButterKnife.apply(mRememberPassword, setTextOnClickListener);
//        mRememberPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                showSnackbar(mCoordinatorLayout, "Login");
//                finish();
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
}
