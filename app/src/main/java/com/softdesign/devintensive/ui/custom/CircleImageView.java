package com.softdesign.devintensive.ui.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.softdesign.devintensive.utils.RoundedDrawable;

/**
 * Created by OsIpoFF on 29.06.16.
 */
public class CircleImageView extends ImageView {
    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
//        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            RoundedDrawable roundedDrawable = new RoundedDrawable(bitmap);
            super.setImageDrawable(roundedDrawable);
//        } else {
//            super.setImageDrawable(drawable);
//        }
    }
}
