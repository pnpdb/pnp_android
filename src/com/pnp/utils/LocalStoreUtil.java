package com.pnp.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

public class LocalStoreUtil {
	public static String getStoragePath(Context context, String path) {
		if (isHaveSDCard()) {
			return Environment.getExternalStorageDirectory().getPath() + path;
		} else {
			return context.getFilesDir().getPath() + path;
		}
	}

	public static boolean isHaveSDCard() {
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist)
			return true;
		return false;
	}

	// 删除SDCard文件
	public static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			}
			// 若是一个目录
			else if (file.isDirectory()) {
				// 遍历目录下的所有文件files[];
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]); // 迭代（回调）删除
				}
			}
			file.delete();
		}
	}

	// 获取SDCard可用空间
	@SuppressWarnings("deprecation")
	public static long getSDFreeSize(Context context) {
		File path = null;
		if (isHaveSDCard()) {
			path = Environment.getExternalStorageDirectory();
		} else {
			path = new File(context.getFilesDir().getPath());
		}
		StatFs sf = new StatFs(path.getPath());
		long blockSize = sf.getBlockSize();
		long freeBlocks = sf.getAvailableBlocks();
		return (freeBlocks * blockSize) / 1024 / 1024;
	}

	// 获取SDCard总空间
	@SuppressWarnings("deprecation")
	public static long getSDAllSize(Context context) {
		File path = null;
		if (isHaveSDCard()) {
			path = Environment.getExternalStorageDirectory();
		} else {
			path = new File(context.getFilesDir().getPath());
		}
		StatFs sf = new StatFs(path.getPath());
		long blockSize = sf.getBlockSize();
		long allBlocks = sf.getBlockCount();
		return (allBlocks * blockSize) / 1024 / 1024;
	}

	// 获取文件大小
	public static long getFileSizes(String path) {
		long s = 0;
		File f;
		FileInputStream fis = null;
		try {
			f = new File(path);
			if (f.exists()) {
				fis = new FileInputStream(f);
				s = fis.available();
				fis.close();
			}
		} catch (IOException e) {
			e.getMessage();
		}
		return s;
	}

	public static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public static String getFileCreateDate(String path) {
		File file = new File(path);
		try {
			Process ls_proc = Runtime.getRuntime().exec("cmd.exe /c dir " + file.getAbsolutePath() + " /tc");
			BufferedReader br = new BufferedReader(new InputStreamReader(ls_proc.getInputStream()));
			for (int i = 0; i < 5; i++) {
				br.readLine();
			}
			String stuff = br.readLine();
			StringTokenizer st = new StringTokenizer(stuff);
			String dateC = st.nextToken();
			String time = st.nextToken();
			String datetime = dateC.concat(time);
			br.close();
			return datetime;
		} catch (Exception e) {
			return null;
		}
	}

	public static void DirsCreate(Context context) {
		File f0, f1, f2, f3;
		String rootPath = "";
		if (isHaveSDCard()) {
			rootPath = Environment.getExternalStorageDirectory().getPath();

		} else {
			rootPath = context.getFilesDir().getPath();
		}
		f0 = new File(rootPath + "/pnp/cache/audio/");
		f1 = new File(rootPath + "/pnp/cache/images/");
		f2 = new File(rootPath + "/pnp/save/images/");
		f3 = new File(rootPath + "/pnp/save/files/");
		if (!f0.exists())
			f0.mkdirs();
		if (!f1.exists())
			f1.mkdirs();
		if (!f2.exists())
			f2.mkdirs();
		if (!f3.exists())
			f3.mkdirs();
	}
}
