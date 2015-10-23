
package com.NationalPhotograpy.weishoot.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

public class DimensionPixelUtil {
    public final static int PX = TypedValue.COMPLEX_UNIT_PX;

    public final static int DIP = TypedValue.COMPLEX_UNIT_DIP;

    public final static int SP = TypedValue.COMPLEX_UNIT_SP;

    public static float getDimensionPixelSize(int unit, float value, Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        switch (unit) {
            case PX:
                return value;
            case DIP:
            case SP:
                return TypedValue.applyDimension(unit, value, metrics);
            default:
                throw new IllegalArgumentException("unknow unix");
        }
    }
}
