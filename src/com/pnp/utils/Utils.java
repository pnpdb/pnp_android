package com.pnp.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class Utils {

	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	public static float getScreenDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	public static int dip2px(Context context, float px) {
		final float scale = getScreenDensity(context);
		return (int) (px * scale + 0.5);
	}

	private static DisplayMetrics dm;

	public static int getShareItemSize(Activity activity) {
		dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return Math.round(dm.widthPixels * 150 / 1080);
	}

}
