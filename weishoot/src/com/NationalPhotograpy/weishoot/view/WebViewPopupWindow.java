
package com.NationalPhotograpy.weishoot.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.Dailyfood.meirishejian.R;

public class WebViewPopupWindow extends PopupWindow {

    private LinearLayout layout_onrefresh, layout_webview, layout_share;

    private OnClickListener refreshListener, webviewListener, shareListener;

    public WebViewPopupWindow(Activity context, OnClickListener refreshListener,
            OnClickListener webviewListener, OnClickListener shareListener) {
        super(context);
        this.refreshListener = refreshListener;
        this.webviewListener = webviewListener;
        this.shareListener = shareListener;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popwindow_webview, null);
        initView(view);
        initData();
        setListener();
        setView(view);
    }

    private void initView(View view) {
        layout_onrefresh = (LinearLayout) view.findViewById(R.id.layout_onrefresh);
        layout_webview = (LinearLayout) view.findViewById(R.id.layout_webview);
        layout_share = (LinearLayout) view.findViewById(R.id.layout_share);
    }

    private void initData() {

    }

    private void setListener() {
        layout_onrefresh.setOnClickListener(refreshListener);
        layout_webview.setOnClickListener(webviewListener);
        layout_share.setOnClickListener(shareListener);
    }

    private void setView(View view) {
        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }
}
