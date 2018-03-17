package com.pnp.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.pnp.utils.PreferenceConstants;

public class AudioReceiver implements Runnable {
	// boolean isRunning = false;

	private byte[] inOutb;

	public void startRecieving() {
		try {
			FileInputStream inStream = new FileInputStream(new File(
					PreferenceConstants.AUDIO_TEMP_PATH));
			inOutb = new byte[inStream.available()];
			inStream.read(inOutb);
			new Thread(this).start();
			inStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void stopRecieving() {
		// isRunning = false;
	}

	public void run() {
		// ������ǰ��������������
		AudioDecoder decoder = AudioDecoder.getInstance();
		decoder.startDecoding();

		// isRunning = true;
		// while (isRunning) {
		int inOutbLen = inOutb.length;
		int inOutbBlock = inOutbLen / 50;
		int inOutbMod = inOutbLen % 50;
		byte[] bt = new byte[50];
		for (int i = 0; i < inOutbBlock; i++) {
			System.arraycopy(inOutb, i * 50, bt, 0, 50);
			decoder.addData(bt, 50);
		}

		bt = new byte[inOutbMod];
		for (int j = 0; j < inOutbMod; j++) {
			bt[j] = inOutb[50 * inOutbBlock + j];
		}
		decoder.addData(bt, inOutbMod);

		// decoder.addData(inOutb, 50);
		// }
		// ������ϣ�ֹͣ���������ͷ���Դ
		// decoder.stopDecoding();
	}
}
