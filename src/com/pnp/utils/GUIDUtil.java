package com.pnp.utils;

import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

import android.annotation.SuppressLint;

public class GUIDUtil extends Object {

	public String valueBeforeMD5 = "";
	public String valueAfterMD5 = "";
	private static Random myRand;
	private static SecureRandom mySecureRand;

	private static String s_id;
	private static final int PAD_BELOW = 0x10;
	private static final int TWO_BYTES = 0xFF;

	static {
		mySecureRand = new SecureRandom();
		long secureInitializer = mySecureRand.nextLong();
		myRand = new Random(secureInitializer);
		try {
			s_id = NetWorkUtil.getRealIp();
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	public GUIDUtil() {
		getRandomGUID(false);
	}

	public GUIDUtil(boolean secure) {
		getRandomGUID(secure);
	}

	private void getRandomGUID(boolean secure) {
		MessageDigest md5 = null;
		StringBuffer sbValueBeforeMD5 = new StringBuffer(128);

		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.getMessage();
		}

		try {
			long time = System.currentTimeMillis();
			long rand = 0;

			if (secure) {
				rand = mySecureRand.nextLong();
			} else {
				rand = myRand.nextLong();
			}
			sbValueBeforeMD5.append(s_id);
			sbValueBeforeMD5.append(":");
			sbValueBeforeMD5.append(Long.toString(time));
			sbValueBeforeMD5.append(":");
			sbValueBeforeMD5.append(Long.toString(rand));

			valueBeforeMD5 = sbValueBeforeMD5.toString();
			md5.update(valueBeforeMD5.getBytes());

			byte[] array = md5.digest();
			StringBuffer sb = new StringBuffer(32);
			for (int j = 0; j < array.length; ++j) {
				int b = array[j] & TWO_BYTES;
				if (b < PAD_BELOW)
					sb.append('0');
				sb.append(Integer.toHexString(b));
			}

			valueAfterMD5 = sb.toString();

		} catch (Exception e) {
			e.getMessage();
		}
	}

	@SuppressLint("DefaultLocale")
	public String toString() {
		String raw = valueAfterMD5.toUpperCase();
		StringBuffer sb = new StringBuffer(64);
		sb.append(raw.substring(0, 8));
		sb.append("-");
		sb.append(raw.substring(8, 12));
		sb.append("-");
		sb.append(raw.substring(12, 16));
		sb.append("-");
		sb.append(raw.substring(16, 20));
		sb.append("-");
		sb.append(raw.substring(20));

		return sb.toString();
	}

	public static void main(String args[]) {

		for (int i = 0; i < 100; i++) {
			GUIDUtil myGUID = new GUIDUtil();
			System.out.println("Seeding String=" + myGUID.valueBeforeMD5);
			System.out.println("rawGUID=" + myGUID.valueAfterMD5);
			System.out.println("RandomGUID=" + myGUID.toString());
		}

		UUID uuid = UUID.randomUUID();
		System.out.println(uuid.toString());
	}

}