
package com.NationalPhotograpy.weishoot.activity.photograph;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.activity.shouye.UserInfoDetialActivity;
import com.NationalPhotograpy.weishoot.bean.ShareBean;
import com.NationalPhotograpy.weishoot.utils.DimensionPixelUtil;
import com.NationalPhotograpy.weishoot.view.ProgressiveDialog;
import com.NationalPhotograpy.weishoot.view.SharePopupWindow;
import com.NationalPhotograpy.weishoot.view.StyleAlertDialog;
import com.NationalPhotograpy.weishoot.view.WebViewPopupWindow;

/**
 * 网页
 */
public class WeiShootWebActiivty extends BaseActivity implements OnClickListener {

    public WebView mwebview;

    private ProgressiveDialog progressDialog;

    private LinearLayout layout_back, layoutForShare;

    private Button btn_right;

    private TextView tv_title;

    private final String APIUser = "weishoot://personal?u=";

    private final String APIShare = "weishoot://share?url=";

    private final String APIPhoto = "weishoot://showPhoto?tcode=";

    private final String APISendTopic = "weishoot://postweishoot?cid=";

    private final String APIAlert = "weishoot://alert?concent=";

    private WebViewPopupWindow webviewPopWindow;

    private SharePopupWindow showShareWindow;

    private StyleAlertDialog warnAlert;

    private String currentUrl = "";

    private String postData = "";

    private String str_url = "";

    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_weishoot_webview);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        layoutForShare = (LinearLayout) findViewById(R.id.layoutForShare);
        btn_right = (Button) findViewById(R.id.btn_right);
        tv_title = (TextView) findViewById(R.id.tv_title);
        mwebview = (WebView) findViewById(R.id.mwebview);
        progressDialog = new ProgressiveDialog(this);
        showShareWindow = new SharePopupWindow(this);
        webviewPopWindow = new WebViewPopupWindow(WeiShootWebActiivty.this, new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 刷新
                mwebview.reload();
                webviewPopWindow.dismiss();
            }
        }, new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 网页打开
                Uri uri = Uri.parse(mwebview.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                webviewPopWindow.dismiss();
            }
        }, new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 分享
                ShareBean shareBean = new ShareBean(tv_title.getText().toString(), tv_title
                        .getText().toString(), "http://weishoot.com/Content/Images/logo@2x.png",
                        currentUrl);
                showShareWindow.setShareData(shareBean);
                if (showShareWindow.isShowing()) {
                    showShareWindow.dismiss();
                } else {
                    showShareWindow.showAtLocation(layoutForShare, Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                webviewPopWindow.dismiss();
            }
        });
    }

    private void initData() {
        str_url = getIntent().getStringExtra("url");
        postData = getIntent().getStringExtra("postData");
        WebSettings webSettings = mwebview.getSettings();
        webSettings.setJavaScriptEnabled(true); // 支持javaScript
        webSettings.setSupportZoom(true); // 支持放大缩小
        // webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        mwebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (postData != null && postData.length() > 0) {
            mwebview.postUrl(str_url, postData.getBytes());
        } else {
            mwebview.loadUrl(str_url);
        }
        mwebview.setDownloadListener(new MyWebViewDownLoadListener());
        mwebview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                currentUrl = url;
                Intent it;
                boolean flag = false;// 当flag为true时，webView取消在家，跳转到指定Activity。
                if (url.contains(APIUser)) {
                    String u = url.substring(url.indexOf("=") + 1, url.length());
                    it = new Intent(WeiShootWebActiivty.this, UserInfoDetialActivity.class);
                    it.putExtra(UserInfoDetialActivity.ARG_UCODE, u);
                    startActivity(it);
                    flag = true;
                } else if (url.contains(APIShare)) {
                    String u = url.substring(url.indexOf("=") + 1, url.length());
                    ShareBean shareBean = new ShareBean(tv_title.getText().toString(), tv_title
                            .getText().toString(),
                            "http://weishoot.com/Content/Images/logo@2x.png", u);
                    showShareWindow.setShareData(shareBean);
                    if (showShareWindow.isShowing()) {
                        showShareWindow.dismiss();
                    } else {
                        showShareWindow.showAtLocation(layoutForShare, Gravity.BOTTOM
                                | Gravity.CENTER_HORIZONTAL, 0, 0);
                    }
                    flag = true;
                } else if (url.contains(APIPhoto)) {
                    String tcode = url.substring(url.indexOf("=") + 1, url.indexOf("&"));
                    String ucode = url.substring(url.indexOf("ucode") + 6, url.length());
                    flag = true;
                } else if (url.contains(APISendTopic)) {
                    String cid = url.substring(url.indexOf("=") + 1, url.length());
                    it = new Intent(WeiShootWebActiivty.this, SelectPicActivity.class);
                    it.putExtra("type", "upload");
                    it.putExtra("cid", cid);
                    startActivityForResult(it, 1);
                    flag = true;
                } else if (url.contains(APIAlert)) {
                    String content = url.substring(url.indexOf("=") + 1, url.length());
                    showWarnAlert(WeiShootWebActiivty.this, content);
                    flag = true;
                }
                if (flag) {
                    view.stopLoading();
                } else {
                    index++;
                }
                return false;
            }

            /**
             * 回调方法，当页面开始加载时执行
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                try {
                    progressDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mwebview.canGoBack()) {
                    layout_back.setVisibility(View.VISIBLE);
                } else {
                    layout_back.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void setListener() {
        layout_back.setOnClickListener(this);
        btn_right.setOnClickListener(this);

        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tv_title.setText(title);
            }

        };
        mwebview.setWebChromeClient(wvcc);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_back:
                // if (mwebview.canGoBack()) {

                if (index == 1) {
                    if (!TextUtils.isEmpty(postData)) {
                        mwebview.postUrl(str_url, postData.getBytes());
                    } else {
                        mwebview.loadUrl(str_url);
                    }
                    index--;

                } else if (index > 1) {
                    mwebview.goBack();
                    index--;
                } else {
                    finish();
                }

                // } else {
                // finish();
                // }
                break;
            case R.id.btn_right:
                if (webviewPopWindow.isShowing()) {
                    webviewPopWindow.dismiss();
                } else {
                    webviewPopWindow.showAtLocation(btn_right, Gravity.TOP | Gravity.RIGHT, 0,
                            (int) DimensionPixelUtil.getDimensionPixelSize(1, 77,
                                    WeiShootWebActiivty.this));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (index == 1) {
                if (!TextUtils.isEmpty(postData)) {
                    mwebview.postUrl(str_url, postData.getBytes());
                } else {
                    mwebview.loadUrl(str_url);
                }
                index--;
                return true;
            } else if (index > 1) {
                mwebview.goBack();
                index--;
                return true;
            } else {
                finish();
            }

        }

        return super.onKeyDown(keyCode, event);
    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition,
                String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    public void showWarnAlert(final Context con, String content) {

        if (warnAlert != null && warnAlert.isShowing()) {
            return;
        }
        warnAlert = new StyleAlertDialog(con, R.style.MyDialog);
        // 没有网络
        warnAlert.setTitleMsg("警告");
        warnAlert.setCancelable(true);
        warnAlert.setContent(content);
        warnAlert.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(View v) {
                warnAlert.dismiss();
            }
        });
        warnAlert.show();
    }
}
