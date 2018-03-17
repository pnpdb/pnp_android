package com.pnp.audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.os.Environment;

import com.pnp.utils.GUIDUtil;
import com.pnp.utils.PreferenceConstants;
import com.pnp.utils.PreferenceUtil;

public class AudioEncoder implements Runnable {

	private FileOutputStream outStream;

	private static AudioEncoder encoder;

	private boolean isEncoding = false;

	private List<AudioData> dataList = null;

	public static AudioEncoder getInstance() {
		if (encoder == null) {
			encoder = new AudioEncoder();
		}
		return encoder;
	}

	private AudioEncoder() {
		dataList = Collections.synchronizedList(new LinkedList<AudioData>());
	}

	public void addData(byte[] data, int size) {
		AudioData rawData = new AudioData();
		rawData.setSize(size);
		byte[] tempData = new byte[size];
		System.arraycopy(data, 0, tempData, 0, size);
		rawData.setRealData(tempData);
		dataList.add(rawData); // 480
	}

	public void startEncoding() {
		if (isEncoding) {
			return;
		}
		// ////////////////////////////////////////////////
		String audioFileName = new GUIDUtil().toString();
		PreferenceConstants.AUDIO_TEMP_PATH = Environment
				.getExternalStorageDirectory().getPath()
				+ PreferenceConstants.AUDIO_BASE_PATH + audioFileName;

		File audioFile = new File(PreferenceConstants.AUDIO_TEMP_PATH);
		try {
			outStream = new FileOutputStream(audioFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		new Thread(this).start();
	}

	public void stopEncoding() {
		this.isEncoding = false;
		try {
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		int encodeSize = 0;
		byte[] encodedData = new byte[256];

		AudioCodec.audio_codec_init(30);

		isEncoding = true;
		while (isEncoding) {
			if (dataList.size() == 0) {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			if (isEncoding) {
				AudioData rawData = dataList.remove(0);
				encodedData = new byte[rawData.getSize()];
				//
				encodeSize = AudioCodec.audio_encode(rawData.getRealData(), 0,
						rawData.getSize(), encodedData, 0);
				if (encodeSize > 0) {
					try {
						byte[] bb = new byte[50];
						System.arraycopy(encodedData, 0, bb, 0, 50);
						outStream.write(bb);
					} catch (Exception e) {
						e.printStackTrace();
					}
					encodedData = new byte[encodedData.length];
				}
			}
		}
	}
}