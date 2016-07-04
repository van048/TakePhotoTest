package cn.ben.takephototest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;

import cn.ben.takephototest.R;
import cn.ben.takephototest.manager.FolderManager;
import cn.ben.takephototest.utils.CapturePhotoHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String EXTRA_RESTORE_PHOTO = "extra_restore_photo";
    private CapturePhotoHelper mCapturePhotoHelper;
    private File mRestorePhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCapturePhotoHelper = new CapturePhotoHelper(this, FolderManager.getPhotoFolder(this));
        Button buttonCapturePhoto = (Button) findViewById(R.id.bt_capture_photo);
        if (buttonCapturePhoto != null) {
            buttonCapturePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCapturePhotoHelper != null) mCapturePhotoHelper.capture();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "requestCode: " + requestCode + " resultCode: " + resultCode + " data: " + data);
        if (requestCode == CapturePhotoHelper.CAPTURE_PHOTO_REQUEST_CODE) {
            File photoFile = mCapturePhotoHelper.getPhoto();
            if (photoFile != null) {
                if (resultCode == RESULT_OK) {
                    PhotoPreviewActivity.preview(this, photoFile);
                    finish();
                } else {
                    if (photoFile.exists()) {
                        //noinspection ResultOfMethodCallIgnored
                        photoFile.delete();
                    }
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        if (mCapturePhotoHelper != null) {
            mRestorePhotoFile = mCapturePhotoHelper.getPhoto();
            Log.i(TAG, "onSaveInstanceState , mRestorePhotoFile: " + mRestorePhotoFile);
            if (mRestorePhotoFile != null) {
                outState.putSerializable(EXTRA_RESTORE_PHOTO, mRestorePhotoFile);
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
        if (mCapturePhotoHelper != null) {
            mRestorePhotoFile = (File) savedInstanceState.getSerializable(EXTRA_RESTORE_PHOTO);
            Log.i(TAG, "onRestoreInstanceState , mRestorePhotoFile: " + mRestorePhotoFile);
            mCapturePhotoHelper.setPhoto(mRestorePhotoFile);
        }
    }
}