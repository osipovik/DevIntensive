package com.softdesign.devintensive.ui.behaviors;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.UiHelper;

/**
 * Created by OsIpoFF on 04.11.16.
 */

public class CustomNestedScrollBehavior extends AppBarLayout.ScrollingViewBehavior {

    private final String TAG = getClass().getSimpleName();

    private final int mMaxAppBarHeight;
    private final int mMinAppBarHeight;
    private final int mMaxUserInfoHeight;

    public CustomNestedScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        mMinAppBarHeight = UiHelper.getStatusBarHeight() + UiHelper.getActionBarHeight();
        mMaxAppBarHeight = context.getResources().getDimensionPixelSize(R.dimen.profile_image_height);
        mMaxUserInfoHeight = context.getResources().getDimensionPixelSize(R.dimen.user_info_height);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float currentFriction = UiHelper.currentFriction(
                mMinAppBarHeight, mMaxAppBarHeight, dependency.getBottom());
        int transY = UiHelper.lerp(mMaxUserInfoHeight/2, mMaxUserInfoHeight, currentFriction);

//        Log.d(TAG, "mMaxUserInfoHeight/2 = " + mMaxUserInfoHeight/2);
//        Log.d(TAG, "mMaxUserInfoHeight = " + mMaxUserInfoHeight);
//        Log.d(TAG, "currentFriction = " + currentFriction);
//        Log.d(TAG, "transY = " + transY);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.topMargin = transY;
        child.setLayoutParams(lp);

        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }
}
