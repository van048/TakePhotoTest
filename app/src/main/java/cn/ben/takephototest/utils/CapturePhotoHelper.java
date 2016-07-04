package cn.ben.takephototest.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.ben.takephototest.R;

public class CapturePhotoHelper {

    public static final int CAPTURE_PHOTO_REQUEST_CODE = 0;
    private static final String TIMESTAMP_FORMAT = "yyyy_MM_dd_HH_mm_ss";
    private static final String TAG = CapturePhotoHelper.class.getSimpleName();
    private final Activity mActivity;
    private File mPhotoFile;
    private final File mPhotoFolder;

    public CapturePhotoHelper(Activity activity, File photoFolder) {
        mActivity = activity;
        mPhotoFolder = photoFolder;
        Log.d(TAG, "mPhotoFolder: " + mPhotoFolder.getAbsolutePath());
    }

    public void capture() {
        if (hasCamera()) {
            createPhotoFile();

            if (mPhotoFile == null) {
                Toast.makeText(mActivity, R.string.file_create_error, Toast.LENGTH_SHORT).show();
                return;
            }

            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri fileUri = Uri.fromFile(mPhotoFile);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            mActivity.startActivityForResult(captureIntent, CAPTURE_PHOTO_REQUEST_CODE);

        } else {
            Toast.makeText(mActivity, R.string.no_camera_app_error, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasCamera() {
        PackageManager packageManager = mActivity.getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void createPhotoFile() {
        if (mPhotoFolder != null) {
            if (!mPhotoFolder.exists()) {//检查保存图片的目录存不存在
                boolean result = mPhotoFolder.mkdirs();
                if (!result) {
                    mPhotoFile = null;
                    Toast.makeText(mActivity, R.string.normal_error, Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // TODO: 2016/7/4
            @SuppressLint("SimpleDateFormat")
            String fileName = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
            mPhotoFile = new File(mPhotoFolder, fileName + BitmapUtils.JPG_SUFFIX);
            if (mPhotoFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                mPhotoFile.delete();
            }
            try {
                //noinspection ResultOfMethodCallIgnored
                mPhotoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                mPhotoFile = null;
            }
        } else {
            mPhotoFile = null;
            Toast.makeText(mActivity, R.string.not_specify_a_directory, Toast.LENGTH_SHORT).show();
        }
    }

    public File getPhoto() {
        return mPhotoFile;
    }

    public void setPhoto(File photo) {
        this.mPhotoFile = photo;
    }
}
