package com.softdesign.devintensive.utils;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by OsIpoFF on 05.11.16.
 */

public class FileUtils {

    public static String getPath(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.Images.Media.DATA};

        Cursor cursor = DevintensiveApplication.getContext().getContentResolver()
                .query(contentUri, proj, null, null, null);

        if (cursor == null) {
            path = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            try {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

            cursor.close();
        }

        return path;
    }
}
