package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.AppConfig;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListActivity extends BaseActivity {
    @BindView(R.id.main_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.navigation_drawer)
    DrawerLayout mNavigationDrawer;

    @BindView(R.id.user_list)
    RecyclerView mRecyclerView;

    private MenuItem mSearchItem;

    DataManager mDataManager;
    UsersAdapter mUsersAdapter;
    List<User> mUsers;
    String mQuery;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: 08.11.16 сохранять состояние активности для восстановления при перевороте экрана
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        ButterKnife.bind(this);

        mDataManager = DataManager.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mHandler = new Handler();

        setupToolbar();
        setupDrawer();
        // TODO: 08.11.16 добавить отображение прогресса загрузки, до того как данные будут готовы
        // TODO: 08.11.16 вынести чтение из базы в отдельный поток
        loadUsersFromDb();
    }

    /**
     *
     */
    private void loadUsersFromDb() {
        mUsers = mDataManager.getUserListFromDb();

        if (mUsers.size() == 0) {
            showSnackbar(mCoordinatorLayout, "Список пользователей не может быть загружен");
        } else {
            showUsers(mUsers);
        }
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        mSearchItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        searchView.setQueryHint("Введите имя пользователя");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                showUsersByQuery(newText);
                return false;
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    private void showUsers(List<User> users) {
        mUsers = users;

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

        mRecyclerView.swapAdapter(mUsersAdapter, false);
    }

    private void showUsersByQuery(String query) {
        mQuery = query;
        // TODO: 08.11.16 при отмене ввода сразу запускать отображение всех данных 1:22:00
        Runnable searchUsers = new Runnable() {
            @Override
            public void run() {
                showUsers(mDataManager.getUserListByName(mQuery));
            }
        };

        mHandler.removeCallbacks(searchUsers);
        mHandler.postDelayed(searchUsers, AppConfig.SEARCH_DELAY);
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
