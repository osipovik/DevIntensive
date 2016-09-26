package com.softdesign.devintensive.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.softdesign.devintensive.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by OsIpoFF on 26.09.16.
 */

public class AuthActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.btn_login)
    Button loginBtn;

    ButterKnife.Action<Button> setOnClickListener = new ButterKnife.Action<Button>() {
        @Override
        public void apply(@NonNull Button view, int index) {
            view.setOnClickListener(AuthActivity.this);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ButterKnife.bind(this);
        ButterKnife.apply(loginBtn, setOnClickListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                finish();
                break;
        }
    }
}
