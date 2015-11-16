
package com.NationalPhotograpy.weishoot.activity.registered;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

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

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.storage.SharePreManager;
import com.NationalPhotograpy.weishoot.storage.StaticInfo;
import com.NationalPhotograpy.weishoot.utils.BitmapUtils1;
import com.NationalPhotograpy.weishoot.utils.StringsUtil;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.ImageUtil;
import com.NationalPhotograpy.weishoot.view.ProgressiveDialog;
import com.NationalPhotograpy.weishoot.view.ScaleMovieView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 裁剪头像
 */
@SuppressLint("FloatMath")
public class ProcessHeadActivity extends BaseActivity implements OnClickListener {

    private LinearLayout layout_back;

    private Button btn_right;

    private Uri picUri;

    private ScaleMovieView preview;

    private boolean hasSetPreviewRect = false;

    private Bitmap bitmap;

    private int exportedWidth;

    private int exportedHeight;

    private ProgressiveDialog progressDialog;

    private String activityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_process_head);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        progressDialog = new ProgressiveDialog(this);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        btn_right = (Button) findViewById(R.id.btn_right);
        preview = (ScaleMovieView) findViewById(R.id.crop_preview);
    }

    private void initData() {
        activityType = getIntent().getStringExtra("type");
        picUri = getIntent().getParcelableExtra("picUri");
        if (picUri == null) {
            this.finish();
        }
        if (StaticInfo.widthPixels == 0) {
            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
            StaticInfo.widthPixels = mDisplayMetrics.widthPixels;
        }
        exportedWidth = StaticInfo.widthPixels - 60;
        exportedHeight = StaticInfo.widthPixels - 60;
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
        progressDialog.show();
        File tempFile = BitmapUtils1.createCameraPath("/weishoot/Camera/",
                BitmapUtils1.getPhotoFileName());
        if (preview.saveImage(tempFile.getAbsolutePath(), exportedWidth)) {
            if ("head".equals(activityType)) {
                requestModifyAvatar(tempFile + "");
            } else if ("TopCover".equals(activityType)) {
                requestReceiveTopCover(tempFile + "");
            }
        } else {
            progressDialog.dismiss();
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

    /**
     * 上传头像
     */
    private void requestModifyAvatar(final String picUri) {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("picUri", new File(picUri));
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.ModifyAvatar, params,
                new RequestCallBack<Object>() {
                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        WeiShootToast.makeErrorText(ProcessHeadActivity.this,
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        String temp = (String) objectResponseInfo.result;
                        progressDialog.dismiss();
                        String resultCode = "-1";
                        String resultMsg = "";
                        if (temp != null) {
                            try {
                                JSONObject jsonObj = new JSONObject(temp);
                                JSONObject resultJson = jsonObj.getJSONObject("result");
                                resultCode = resultJson.optString("ResultCode");
                                resultMsg = resultJson.optString("ResultMsg");
                                if ("200".equals(resultCode)) {
                                    SharePreManager.getInstance(ProcessHeadActivity.this)
                                            .setUserHeadTimestamp(StringsUtil.getTimeFormat());
                                    Intent intent = new Intent();
                                    intent.putExtra("picUri", picUri);
                                    setResult(Constant.RESULT_HEAD, intent);
                                    finish();
                                } else {
                                    WeiShootToast.makeErrorText(ProcessHeadActivity.this,
                                            resultMsg, WeiShootToast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            WeiShootToast.makeErrorText(ProcessHeadActivity.this,
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }

    /**
     * 修改用户顶图
     */
    private void requestReceiveTopCover(final String picUri) {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("picUri", new File(picUri));
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.ReceiveTopCover, params,
                new RequestCallBack<Object>() {
                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        WeiShootToast.makeErrorText(ProcessHeadActivity.this,
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        String temp = (String) objectResponseInfo.result;
                        progressDialog.dismiss();
                        String resultCode = "-1";
                        String resultMsg = "";
                        if (temp != null) {
                            try {
                                JSONObject jsonObj = new JSONObject(temp);
                                JSONObject resultJson = jsonObj.getJSONObject("result");
                                resultCode = resultJson.optString("ResultCode");
                                resultMsg = resultJson.optString("ResultMsg");
                                if ("200".equals(resultCode)) {
                                    SharePreManager.getInstance(ProcessHeadActivity.this)
                                            .setUserHeadTimestamp(StringsUtil.getTimeFormat());
                                    Intent intent = new Intent();
                                    intent.putExtra("picUri", picUri);
                                    setResult(Constant.RESULT_TOPCOVER, intent);
                                    finish();
                                } else {
                                    WeiShootToast.makeErrorText(ProcessHeadActivity.this,
                                            resultMsg, WeiShootToast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            WeiShootToast.makeErrorText(ProcessHeadActivity.this,
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }
}
