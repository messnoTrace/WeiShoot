
package com.NationalPhotograpy.weishoot.activity.photograph;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.storage.StaticInfo;
import com.NationalPhotograpy.weishoot.utils.BitmapUtils1;
import com.NationalPhotograpy.weishoot.view.ImageUtil;
import com.NationalPhotograpy.weishoot.view.ScaleMovieView;

/**
 * 裁剪头像
 */
@SuppressLint("FloatMath")
public class ScreenshotMovieActivivty extends BaseActivity implements OnClickListener {

    private LinearLayout layout_back;

    private Button btn_right;

    private Uri picUri;

    private ScaleMovieView preview;

    private boolean hasSetPreviewRect = false;

    private Bitmap bitmap;

    private int exportedWidth;

    private int exportedHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_screenshot_movie);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        btn_right = (Button) findViewById(R.id.btn_right);
        preview = (ScaleMovieView) findViewById(R.id.crop_preview);
    }

    private void initData() {
        picUri = getIntent().getParcelableExtra("picUri");
        if (picUri == null) {
            this.finish();
        }
        if (StaticInfo.widthPixels == 0) {
            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
            StaticInfo.widthPixels = mDisplayMetrics.widthPixels;
        }

        exportedWidth = StaticInfo.widthPixels;
        exportedHeight = (int) (exportedWidth * Constant.MOVIE_SIZE);
    }

    private void setListener() {
        layout_back.setOnClickListener(this);
        btn_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.btn_right:
                cropImage();
                break;

            default:
                break;
        }
    }

    private void cropImage() {
        File tempFile = BitmapUtils1.createCameraPath("/weishoot/Camera/",
                BitmapUtils1.getPhotoFileName());
        if (preview.saveImage(tempFile.getAbsolutePath(), exportedWidth)) {
            Intent it = new Intent(ScreenshotMovieActivivty.this, CreateMovieActivity.class);
            it.putExtra("picUri", Uri.fromFile(tempFile));
            startActivityForResult(it, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.RESULT_UPLOAD_SUCCESS) {
            setResult(resultCode, data);
            finish();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasSetPreviewRect) {
            bitmap = BitmapUtils1.decodebitmap(this, picUri);
            hasSetPreviewRect = true;
            preview.setRect(exportedWidth - 1, exportedHeight - 1);
            preview.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageUtil.recycleBitmap(bitmap);
    }
}
