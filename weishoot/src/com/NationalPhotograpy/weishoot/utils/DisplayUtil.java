package com.NationalPhotograpy.weishoot.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * @author Quan
 * @version 1.0
 * @date 2015-08-13 11:31
 * @since 尺寸转换
 * --------------------------------------------------------------------
 */
public class DisplayUtil {

    /**
     * 获取屏幕的宽度
     *
     * @param activity
     *
     * @return
     */
    public static int screenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param activity
     *
     * @return
     */
    public static int screenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     *
     * @return
     */
    public static float px2dip(Context context, float pxValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pxValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     *
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, context.getResources().getDisplayMetrics());

    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param spValue
     *
     * @return
     */
    public static float sp2px(Context context, float spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());

    }
}