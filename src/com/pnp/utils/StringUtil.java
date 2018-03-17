package com.pnp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.pnp.message.XStreamUtil;

public class StringUtil {

	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\t|\r|\n| {2,}");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	public static boolean isXmlFull(String recv) {
		try {
			XStreamUtil.getCommandFromXML(recv);
		} catch (Exception e) {
			Log.i("ankl", "不是完整的XML");
			return false;
		}
		Log.i("ankl", "是完整的XML");
		return true;
	}

}
