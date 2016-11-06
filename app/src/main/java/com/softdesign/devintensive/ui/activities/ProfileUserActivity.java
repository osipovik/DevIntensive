package com.softdesign.devintensive.ui.activities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class ProfileUserActivity extends BaseActivity {

    @BindView(R.id.main_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.about_et)
    EditText mUserBio;

    @BindView(R.id.user_photo_img)
    ImageView mProfileImage;

    @BindViews({R.id.user_rating_tv, R.id.user_code_lines_tv, R.id.user_projects_tv})
    List<TextView> mUserValueViews;

    @BindView(R.id.repositories_list)
    ListView mRepoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        ButterKnife.bind(this);

        setupToolbar();
        initProfileData();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initProfileData() {
        UserDTO userDTO = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);

        final List<String> repositories = userDTO.getRepositories();
        final RepositoriesAdapter repositoriesAdapter = new RepositoriesAdapter(this, repositories);
        mRepoListView.setAdapter(repositoriesAdapter);

        mRepoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 06.11.16 реализовать переход к просмотру репозитория
                showSnackbar(mCoordinatorLayout, "Repo "  + repositories.get(position));
            }
        });

        mUserBio.setText(userDTO.getBio());
        mCollapsingToolbar.setTitle(userDTO.getFullName());

        for (TextView userValueView : mUserValueViews) {
            switch (userValueView.getId()) {
                case R.id.user_rating_tv:
                    userValueView.setText(userDTO.getRating());
                    break;
                case R.id.user_code_lines_tv:
                    userValueView.setText(userDTO.getCodeLines());
                    break;
                case R.id.user_projects_tv:
                    userValueView.setText(userDTO.getProjects());
                    break;
            }
        }

        Picasso.with(this)
                .load(userDTO.getPhoto())
                .placeholder(getResources().getDrawable(R.drawable.user_bg))
                .error(getResources().getDrawable(R.drawable.user_bg))
                .into(mProfileImage);
    }
}
