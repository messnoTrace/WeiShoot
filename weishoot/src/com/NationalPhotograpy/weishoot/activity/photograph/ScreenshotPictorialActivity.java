
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.adapter.photograph.ScreenshotPictorialAdapter;
import com.NationalPhotograpy.weishoot.bean.PicTemplateChildBean;
import com.NationalPhotograpy.weishoot.bean.PicTemplateChildBean.PicTemplateChild;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.storage.StaticInfo;
import com.NationalPhotograpy.weishoot.utils.BitmapUtils1;
import com.NationalPhotograpy.weishoot.utils.DimensionPixelUtil;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.view.HorizontalListView;
import com.NationalPhotograpy.weishoot.view.ImageUtil;
import com.NationalPhotograpy.weishoot.view.ScalePictorialView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 裁剪封面
 */
@SuppressLint("FloatMath")
public class ScreenshotPictorialActivity extends BaseActivity implements OnClickListener {

    private LinearLayout layout_back;

    private Button btn_right;

    private Uri picUri;

    private ScalePictorialView preview;

    private boolean hasSetPreviewRect = false;

    private Bitmap bitmap;

    private int exportedWidth;

    private int exportedHeight;

    private HorizontalListView listview_temp;

    private String Cid;

    private PicTemplateChildBean picTempBean;

    private PicTemplateChild picTemplateChild;

    private ImageLoader imageLoader;

    private DisplayImageOptions mOptionsPic;

    private RelativeLayout layout_screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_screenshot_pictorial);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        layout_screen = (RelativeLayout) findViewById(R.id.layout_screen);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        btn_right = (Button) findViewById(R.id.btn_right);
        preview = (ScalePictorialView) findViewById(R.id.crop_preview);
        listview_temp = (HorizontalListView) findViewById(R.id.listview_temp);
        this.imageLoader = ImageLoader.getInstance();
        mOptionsPic = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .showImageForEmptyUri(R.drawable.ic_stub).build();
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
        int tempWidth = (int) DimensionPixelUtil.getDimensionPixelSize(1, 25, this);
        exportedWidth = StaticInfo.widthPixels - tempWidth * 2;
        exportedHeight = (int) (exportedWidth * Constant.PICTORIAL_SIZE);
        Cid = getIntent().getStringExtra("Cid");
        requestGetCoverPicByClass();
    }

    private void setListener() {
        layout_back.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        listview_temp.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                picTemplateChild = picTempBean.data.get(position);
                imageLoader.displayImage(picTemplateChild.PicUrlpng, preview, mOptionsPic);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.btn_right:
                if (picTemplateChild == null) {
                    WeiShootToast.makeErrorText(ScreenshotPictorialActivity.this, "请选择一种画报风格",
                            WeiShootToast.LENGTH_SHORT).show();
                    return;
                }
                cropImage();
                break;

            default:
                break;
        }
    }

    private void cropImage() {
        Bitmap clipLayoutBitmap = getLayouyClipBitmap();
        String picLayoutUri = BitmapUtils1.saveBitmap(clipLayoutBitmap);
        clipLayoutBitmap.recycle();
        File tempFile = BitmapUtils1.createCameraPath("/weishoot/Camera/",
                BitmapUtils1.getPhotoFileName());
        if (preview.saveImage(tempFile.getAbsolutePath(), exportedWidth)) {
            Intent it = new Intent(ScreenshotPictorialActivity.this, CreatePictorialActivity.class);
            it.putExtra("picUri", Uri.fromFile(tempFile));
            it.putExtra("picLayoutUri", picLayoutUri);
            it.putExtra("CPId", picTemplateChild.CPId);
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
            preview.setImageBitmap(bitmap, true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageUtil.recycleBitmap(bitmap);
    }

    /* 获取矩形区域内的截图 */
    private Bitmap getLayouyClipBitmap() {
        layout_screen.setDrawingCacheEnabled(true);
        layout_screen.buildDrawingCache();
        int tempHeigntTop = (int) DimensionPixelUtil.getDimensionPixelSize(1, 50, this);
        int tempWidth = (int) DimensionPixelUtil.getDimensionPixelSize(1, 25, this);
        int rectWidth = layout_screen.getWidth() - tempWidth * 2;
        int rectHeight = (int) (rectWidth * Constant.PICTORIAL_SIZE);
        Bitmap finalBitmap = Bitmap.createBitmap(layout_screen.getDrawingCache(), tempWidth,
                tempHeigntTop, rectWidth, rectHeight);
        layout_screen.setDrawingCacheEnabled(false);
        return finalBitmap;
    }

    /**
     * 根据分类编号获取封面
     */
    private void requestGetCoverPicByClass() {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("Cid", Cid);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetCoverPicByClass, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        WeiShootToast.makeErrorText(ScreenshotPictorialActivity.this,
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        String temp = (String) objectResponseInfo.result;
                        picTempBean = new Gson().fromJson(temp,
                                new TypeToken<PicTemplateChildBean>() {
                                }.getType());
                        if (picTempBean != null && picTempBean.result != null) {
                            if ("200".equals(picTempBean.result.ResultCode)) {
                                int tempHeigntTop = (int) DimensionPixelUtil.getDimensionPixelSize(
                                        1, 53, ScreenshotPictorialActivity.this);
                                int tempWidth = (int) DimensionPixelUtil.getDimensionPixelSize(1,
                                        20, ScreenshotPictorialActivity.this);
                                int rectWidth = preview.getWidth() - tempWidth * 2;
                                int rectHeight = (int) (rectWidth * Constant.PICTORIAL_SIZE);
                                int picTempLayoutHeight = preview.getHeight() - rectHeight
                                        - tempHeigntTop;
                                RelativeLayout.LayoutParams lyp = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.FILL_PARENT,
                                        picTempLayoutHeight);
                                lyp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                                listview_temp.setLayoutParams(lyp);
                                ScreenshotPictorialAdapter adapter = new ScreenshotPictorialAdapter(
                                        ScreenshotPictorialActivity.this, picTempBean.data,
                                        picTempLayoutHeight);
                                listview_temp.setAdapter(adapter);
                            } else {
                                WeiShootToast.makeErrorText(ScreenshotPictorialActivity.this,
                                        picTempBean.result.ResultMsg, WeiShootToast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            WeiShootToast.makeErrorText(ScreenshotPictorialActivity.this,
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

    }
}
