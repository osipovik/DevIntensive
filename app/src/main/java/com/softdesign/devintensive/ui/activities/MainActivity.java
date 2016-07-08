package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.ui.custom.CircleImageView;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = ConstantManager.TAG_PREFIX  + getClass().getSimpleName();

    @BindView(R.id.main_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.navigation_drawer)
    DrawerLayout mNavigationDrawer;

    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    @BindView(R.id.profile_placeholder)
    RelativeLayout mProfilePlaceholder;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.user_photo_img)
    ImageView mProfileImage;

    @BindViews({R.id.phone_et, R.id.email_et, R.id.vk_id_et, R.id.repository_et, R.id.about_et})
    List<EditText> mUserInfoViews;

    @BindViews({R.id.dial_iv, R.id.send_iv, R.id.view_vk_iv, R.id.view_github_iv})
    List<ImageView> mImageViewList;

    private DataManager mDataManager;
    private int mCurrentEditMode = 0;

    private AppBarLayout.LayoutParams mAppBarParams = null;
    private File mPhotoFile = null;
    private Uri mSelectedImage = null;

    //Меняем состояние EditText(редактирование/просмотр) в зависимости от параметра value
    ButterKnife.Setter<View, Boolean> setEditTextValues = new ButterKnife.Setter<View, Boolean>() {
        @Override
        public void set(@NonNull View view, Boolean value, int index) {
            view.setEnabled(value);
            view.setFocusable(value);
            view.setFocusableInTouchMode(value);

            if (view.getId() == R.id.phone_et) {
                view.requestFocus();
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    };

    //Запрещаем клик по ImageView в режиме редактирования, и разрешаем в режиме просмотра
    ButterKnife.Setter<View, Boolean> setImageViewClickable =
            new ButterKnife.Setter<View, Boolean>() {
        @Override
        public void set(@NonNull View view, Boolean value, int index) {
            view.setClickable(value);
        }
    };

    //Устанавливаем обработку клика по элементам ImageView
    ButterKnife.Action<ImageView> setOnClickListeners = new ButterKnife.Action<ImageView>() {
        @Override
        public void apply(@NonNull ImageView view, int index) {
            view.setOnClickListener(MainActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        ButterKnife.apply(mImageViewList, setOnClickListeners);

        mDataManager = DataManager.getInstance();

        mFloatingActionButton.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);

        setupToolbar();
        setupDrawer();
        insertProfileImage(mDataManager.getPreferencesManager().loadUserPhoto(), true);

        if (savedInstanceState == null) {

        } else {
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeViewMode(mCurrentEditMode);
            loadUserInfoValue();
        }

        Log.d(TAG, "onCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.PERMISSION_REQUEST_CAMERA_CODE
                && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                loadPhotoFromCamera();
            }
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Устанавливает тулбар, получает параметры CollapsingToolbar, устанавливает параметры ActionBar
     */
    private void setupToolbar() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();

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
                    showSnackbar(item.getTitle().toString());
                    item.setChecked(true);
                    mNavigationDrawer.closeDrawer(GravityCompat.START);
                    return false;
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Клик по кнопке FloatActionButton
            case R.id.fab:
                if (mCurrentEditMode == 0) {
                    changeViewMode(1);
                } else {
                    changeViewMode(0);
                }

                break;
            //Клик по плейсхолдеру для установки фотографии профиля
            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
            //Клик по иконке вызова
            case R.id.dial_iv:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {
                    String telNumber = mUserInfoViews.get(0).getText().toString();
                    createActionIntent(Intent.ACTION_CALL, Uri.fromParts("tel", telNumber, null));
                } else {
                    requestAppPermissions(new String[]{
                            Manifest.permission.CALL_PHONE
                    }, ConstantManager.PERMISSION_REQUEST_CALL_CODE);
                }
                break;
            //Клик по иконке отправки email
            case R.id.send_iv:
                String email = mUserInfoViews.get(1).getText().toString();
                createActionIntent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
                break;
            //Клик по иконке просмотра профиля ВК
            case R.id.view_vk_iv:
                String vk_url = mUserInfoViews.get(2).getText().toString();
                createActionIntent(Intent.ACTION_VIEW, Uri.parse("https://" + vk_url));
                break;
            //Клик по иконке просмотра аккаунта GitHub
            case R.id.view_github_iv:
                String repo_url = mUserInfoViews.get(3).getText().toString();
                createActionIntent(Intent.ACTION_VIEW, Uri.parse("https://" + repo_url));
                break;
        }
    }

    /**
     * Показывает диалог запроса разрешений и списка permissions
     * @param permissions Перечень необходимых разрешений
     * @param requestCode Код запроса, для обработки возвращаемого результата
     */
    private void requestAppPermissions(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);

        Snackbar.make(mCoordinatorLayout, R.string.permission_request, Snackbar.LENGTH_LONG)
                .setAction(R.string.allow, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings();
                    }
                }).show();
    }


    /**
     * Генерирует неявный интент по входящим параметрам и запускает новую активность
     * @param action Строковое представление действия для интента
     * @param uri Подготовленный к передаче в интент Uri
     */
    private void createActionIntent(String action, Uri uri) {
        Intent actionIntent = new Intent(action, uri);

        try {
            startActivity(actionIntent);
        } catch (ActivityNotFoundException e) {
            showSnackbar(getString(R.string.app_for_action_not_fount_exception));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.user_profile_dialog_gallery),
                        getString(R.string.user_profile_dialog_camera),
                        getString(R.string.user_profile_dialog_cancel)};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title));
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choiseItem) {
                        switch (choiseItem) {
                            case 0:
                                loadPhotoFromGallery();
                                break;
                            case 1:
                                loadPhotoFromCamera();
                                break;
                            case 2:
                                dialog.cancel();
                                break;
                        }
                    }
                });
                return builder.create();

            default:
                return null;
        }
    }

    /**
     * Получает ответ от другой Activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage, false);
                }

                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage, false);
                }

                break;
        }
    }

    /**
     * Переключает view, в режим редактирования и обратно
     * @param mode - режим view, 1 - режим редактирования, 0 - режим просмотра
     */
    private void changeViewMode(int mode) {
        if (mode == 1) {
            ButterKnife.apply(mUserInfoViews, setEditTextValues, true);
            ButterKnife.apply(mImageViewList, setImageViewClickable, false);

            showProfilePlaceholder();
            lockToolbar();

            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
            mFloatingActionButton.setImageResource(R.drawable.ic_done_24dp);
        } else {
            ButterKnife.apply(mUserInfoViews, setEditTextValues, false);
            ButterKnife.apply(mImageViewList, setImageViewClickable, true);

            saveUserInfoValue();
            hideProfilePlaceholder();
            unlockToolbar();

            mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
            mFloatingActionButton.setImageResource(R.drawable.ic_edit_24dp);
        }

        mCurrentEditMode = mode;
    }

    private void loadUserInfoValue() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();

        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }
    }

    private void saveUserInfoValue() {
        List<String> userData = new ArrayList<>();

        for (EditText userFiledView : mUserInfoViews) {
            userData.add(userFiledView.getText().toString());
        }

        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }

    private boolean isNavDrawerOpen() {
        return mNavigationDrawer != null && mNavigationDrawer.isDrawerOpen(GravityCompat.START);
    }

    private void loadPhotoFromGallery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent,
                getString(R.string.user_profile_choose_message)),
                ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    private void loadPhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                showSnackbar(getString(R.string.image_create_error) + e.getLocalizedMessage());
            }

            if (mPhotoFile != null) {
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            requestAppPermissions(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.PERMISSION_REQUEST_CAMERA_CODE);
        }
    }

    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    private void lockToolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    private void unlockToolbar() {
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
                | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PHOTO_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;
    }

    /**
     * Обновляет изображение профиля во вьюхе.
     * Если вызван не из onCreate, сохраняет путь к изображению в Shared Preference
     * @param selectedImage ресурс изображения
     * @param init флаг вызова функции, true - при первом вызове из onCreate, иначе false
     */
    private void insertProfileImage(Uri selectedImage, boolean init) {
        Picasso.with(this)
                .load(selectedImage)
                .placeholder(R.drawable.user_bg)
                .transform(new Transformation() {
                    int maxHeight = 256;

                    @Override
                    public Bitmap transform(Bitmap source) {
                        int targetWidth, targetHeight;
                        double aspectRatio;

                        targetHeight = maxHeight;
                        aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                        targetWidth = (int) (targetHeight * aspectRatio);

                        Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);

                        if (result != source) {
                            source.recycle();
                        }

                        return result;
                    }

                    @Override
                    public String key() {
                        return "x" + maxHeight;
                    }
                })
                .into(mProfileImage);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        CircleImageView circlePhoto = (CircleImageView) navigationView.getHeaderView(0)
                .findViewById(R.id.nav_photo_circle);

        try {
            circlePhoto.setImageDrawable(Drawable.createFromStream(
                    getContentResolver().openInputStream(selectedImage), selectedImage.toString()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (!init) {
            mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);
        }
    }

    /**
     * Открывает настройки приложения, вызываем для установки прав
     */
    private void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));

        startActivity(appSettingsIntent);
    }
}
