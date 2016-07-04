package cn.ben.takephototest.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import java.io.File;

import cn.ben.takephototest.R;
import cn.ben.takephototest.utils.BitmapUtils;

public class PhotoPreviewActivity extends AppCompatActivity {

    private static final String EXTRA_PHOTO = "extra_photo";
    private static final float RATIO = 0.75f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);

        ImageView mPhotoPreview = (ImageView) findViewById(R.id.iv_preview_photo);
        File mPhotoFile = (File) getIntent().getSerializableExtra(EXTRA_PHOTO);
        if (mPhotoFile == null) return;

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int requestWidth = (int) (displayMetrics.widthPixels * RATIO);
        int requestHeight = (int) (displayMetrics.heightPixels * RATIO);
        Bitmap bitmap = BitmapUtils.decodeBitmapFromFile(mPhotoFile.getAbsolutePath(), requestWidth, requestHeight);//按照屏幕宽高的3/4比例进行缩放显示
        if (bitmap != null) {
            int degree = BitmapUtils.getBitmapDegree(mPhotoFile.getAbsolutePath());//检查是否有被旋转，并进行纠正
            if (degree != 0) {
                bitmap = BitmapUtils.rotateBitmapByDegree(bitmap, degree);
            }
            if (mPhotoPreview != null) {
                mPhotoPreview.setImageBitmap(bitmap);
            }
        }
    }

    public static void preview(Activity activity, File file) {
        Intent previewIntent = new Intent(activity, PhotoPreviewActivity.class);
        previewIntent.putExtra(EXTRA_PHOTO, file);
        activity.startActivity(previewIntent);
    }
}
