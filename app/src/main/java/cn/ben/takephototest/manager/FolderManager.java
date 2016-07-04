package cn.ben.takephototest.manager;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FolderManager {
    private final static String PHOTO_FOLDER_NAME = "photo";
    private final static String APP_FOLDER_NAME = "takePhotoTest";

    private static File getAppFolder(Context context) {
        // TODO: 2016/7/4
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return new File(context.getExternalCacheDir(), APP_FOLDER_NAME);
        } else {
            return null;
        }
    }

    public static File getPhotoFolder(Context context) {
        File appFolder = getAppFolder(context);
        if (appFolder != null) {
            return new File(appFolder, PHOTO_FOLDER_NAME);
        } else {
            return null;
        }
    }
}
