
package com.NationalPhotograpy.weishoot.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Dailyfood.meirishejian.R;

public class WeiShootToast {

    private WindowManager mWdm;

    private View mToastView;

    private WindowManager.LayoutParams mParams;

    private Timer mTimer;

    private boolean mShowTime;

    private boolean mIsShow;

    public static final boolean LENGTH_LONG = true;

    public static final boolean LENGTH_SHORT = false;

    private TextView tv_content_sucess, tv_content_error;

    private LinearLayout layout_sucess, layout_error;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mWdm.removeView(mToastView);
            mIsShow = false;
        };
    };

    private WeiShootToast(Context context, String text, boolean showTime, boolean flag) {
        mShowTime = showTime;
        mIsShow = false;
        mWdm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mToastView = LayoutInflater.from(context).inflate(R.layout.toast_warning, null);
        tv_content_sucess = (TextView) mToastView.findViewById(R.id.tv_content_sucess);
        tv_content_error = (TextView) mToastView.findViewById(R.id.tv_content_error);
        layout_error = (LinearLayout) mToastView.findViewById(R.id.layout_error);
        layout_sucess = (LinearLayout) mToastView.findViewById(R.id.layout_sucess);
        if (flag) {
            layout_error.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(text)) {
                tv_content_sucess.setText(text);
            }
        } else {
            layout_sucess.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(text)) {
                tv_content_error.setText(text);
            }
        }

        mTimer = new Timer();
        setParams(context);
    }

    private void setParams(Context context) {
        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.windowAnimations = R.style.anim_view;
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mParams.gravity = Gravity.CENTER_HORIZONTAL;
        mParams.y = -Methods.getWindowHeight(context);
    }

    public static WeiShootToast makeText(Context context, String text, boolean showTime) {
        WeiShootToast result = new WeiShootToast(context, text, showTime, true);
        return result;
    }

    public static WeiShootToast makeErrorText(Context context, String text, boolean showTime) {
        WeiShootToast result = new WeiShootToast(context, text, showTime, false);
        return result;
    }

    // public static

    public void show() {
        if (!mIsShow) {
            mIsShow = true;
            mWdm.addView(mToastView, mParams);
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);

                }
            }, (long) (mShowTime ? 3500 : 2000));
        }
    }

    public void cancel() {
        if (mTimer == null) {
            mWdm.removeView(mToastView);
            mTimer.cancel();
        }
        mIsShow = false;
    }
}
