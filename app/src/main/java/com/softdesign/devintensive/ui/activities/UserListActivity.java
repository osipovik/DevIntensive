package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.response.UserListResponse;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends BaseActivity {
    @BindView(R.id.main_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.navigation_drawer)
    DrawerLayout mNavigationDrawer;

    @BindView(R.id.user_list)
    RecyclerView mRecyclerView;

    DataManager mDataManager;
    UsersAdapter mUsersAdapter;
    List<UserListResponse.UserData> mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        ButterKnife.bind(this);

        mDataManager = DataManager.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        setupToolbar();
        setupDrawer();
        loadUsers();
    }

    /**
     *
     */
    private void loadUsers() {
        Call<UserListResponse> call = mDataManager.getUserList();
        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                if (response.code() == 200) {
                    mUsers = response.body().getData();
                    mUsersAdapter = new UsersAdapter(mUsers,
                            new UsersAdapter.UserViewHolder.CustomClickListener() {
                        @Override
                        public void onItemClickListener(int position) {
                            UserDTO userDTO = new UserDTO(mUsers.get(position));
                            Intent profileIntent =
                                    new Intent(getBaseContext(), ProfileUserActivity.class);
                            profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);

                            startActivity(profileIntent);
                        }
                    });
                    mRecyclerView.setAdapter(mUsersAdapter);
                } else {
                    showSnackbar(mCoordinatorLayout, "");
                }
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {

            }
        });
    }

    /**
     * Устанавливает тулбар, получает параметры CollapsingToolbar, устанавливает параметры ActionBar
     */
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Устанавливает обоработчик клика по элементам списка в NavigationView
     */
    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView
                    .OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.user_profile_menu:
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.team_menu:
                            break;
                    }

                    showSnackbar(mCoordinatorLayout, item.getTitle().toString());
                    item.setChecked(true);
                    mNavigationDrawer.closeDrawer(GravityCompat.START);
                    return false;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }
}
