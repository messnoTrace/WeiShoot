package com.NationalPhotograpy.weishoot.utils;

import android.os.SystemClock;


public class ClickUtil {
    private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = SystemClock.elapsedRealtime();
        if (time - lastClickTime < 500) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }
}
