
package com.NationalPhotograpy.weishoot.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.storage.SharePreManager;

public class BaseActivity extends FragmentActivity {
    public SharePreManager sp;

    private DisplayMetrics metrics = new DisplayMetrics();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = SharePreManager.getInstance(this);
        getWidthPixels();
        getHeightPixels();
    }
    
    public int getWidthPixels() {
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Constant.SCREEN_WIDTH=metrics.widthPixels;
        return metrics.widthPixels;
    }

    public int getHeightPixels() {
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Constant.SCREEN_HEIGHT=metrics.heightPixels;
        return metrics.heightPixels;
    }
}
