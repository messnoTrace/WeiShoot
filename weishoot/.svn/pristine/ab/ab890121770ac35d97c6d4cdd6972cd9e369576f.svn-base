
package com.NationalPhotograpy.weishoot.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Methods {
    public static int dip2px(Context con, float dpValue) {
        final float scale = con.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取屏幕的宽度
     */
    public static int getWindowWith(Context con) {
        return ((Activity) con).getWindowManager().getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕的高度
     */
    public static int getWindowHeight(Context con) {
        return ((Activity) con).getWindowManager().getDefaultDisplay().getHeight();
    }

    /**
     * 获取控件的宽度
     * 
     * @param view(要获取的宽度的控件)
     */
    public static int getWidth(View view) {
        int space_width = 0;
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int width = view.getMeasuredWidth();
        space_width = width;
        return space_width;
    }

    public static void showInput(Context con) {
        InputMethodManager imm = (InputMethodManager) con
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    
    /**
     * 检查用户输入的手机号的有效性
     *
     * @return 检查通过返回true，检查未通过返回false并提示错误
     */
    public static boolean validatePhone(String telephone) {
        // 检查手机号是否为1开头并满足11位数字
        String pn = telephone;

        if (null != pn && pn.length() == 11 && pn.charAt(0) == '1'
                && isNumric(pn)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 正则表达式判断是否全是数字：不全为数字或者为空则返回false
     *
     */
    public static boolean isNumric(String str) {
        if (!TextUtils.isEmpty(str)) {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(str);
            if (!isNum.matches()) {
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
    }

}
