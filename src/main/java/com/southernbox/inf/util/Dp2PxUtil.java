package com.southernbox.inf.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class Dp2PxUtil {

	public static int getPx(Context context, int dp) {
		//获取屏幕密度
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		//屏幕密度的比例值
		float density = displayMetrics.density;
		//将dp转换为px
		return (int)(density*dp);
	}

}
