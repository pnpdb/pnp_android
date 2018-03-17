package com.pnp;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class PNPApplication extends Application {

	private static String currentAccount;
	private static String currentActivity;

	@Override
	public void onCreate() {
		super.onCreate();
//		SDKInitializer.initialize(getApplicationContext());
	}

	public static String getCurrentActivity() {
		return currentActivity;
	}

	public static void setCurrentActivity(String currentActivity) {
		PNPApplication.currentActivity = currentActivity;
	}

	public static String getCurrentAccount() {
		return currentAccount;
	}

	public static void setCurrentAccount(String currentAccount) {
		PNPApplication.currentAccount = currentAccount;
	}

}
