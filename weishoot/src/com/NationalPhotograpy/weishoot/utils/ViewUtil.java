package com.NationalPhotograpy.weishoot.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

public class ViewUtil {

	//private static int screen_height, screen_width;

	// 根据当前设备，将dp转为px
	public static int dip2px(Context context, float size) {
		Resources r = context.getResources();
		int px = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, r.getDisplayMetrics()) + 0.5); // 四舍五入

		return px;
	}

	
	// 获取屏幕密度
	public static float getScreenDensity(Activity context){
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.density;
	}

	// 获取屏幕宽度
	public static int getScreenWidth(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	// 获取屏幕高度
	public static int getScreenHeight(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	
	/**
	 * 设置view右边的图
	 */
	public static void setDrawableRight(Context mContext,TextView view,int srcId){
		Drawable drawable= mContext.getResources().getDrawable(srcId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		view.setCompoundDrawables(null,null,drawable,null);
	}
}
