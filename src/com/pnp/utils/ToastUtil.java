package com.pnp.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	public static void ToastShow(Context context, String text, int duration) {
		Toast.makeText(context, text, duration).show();
	}

}
