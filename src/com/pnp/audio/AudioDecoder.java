package com.pnp.audio;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AudioDecoder implements Runnable {

	private static AudioDecoder decoder;

	private static final int MAX_BUFFER_SIZE = 2048;

	private byte[] decodedData = new byte[1024];
	private boolean isDecoding = false;
	private List<AudioData> dataList = null;

	public static AudioDecoder getInstance() {
		if (decoder == null) {
			decoder = new AudioDecoder();
		}
		return decoder;
	}

	private AudioDecoder() {
		this.dataList = Collections.synchronizedList(new LinkedList<AudioData>());
	}

	public void addData(byte[] data, int size) {
		AudioData adata = new AudioData();
		adata.setSize(size);
		byte[] tempData = new byte[size];
		System.arraycopy(data, 0, tempData, 0, size);
		adata.setRealData(tempData);
		dataList.add(adata);
	}

	public void startDecoding() {

		if (isDecoding) {
			return;
		}
		new Thread(this).start();
	}

	public void run() {
		AudioPlayer player = AudioPlayer.getInstance();
		player.startPlaying();
		this.isDecoding = true;
		// init ILBC parameter:30 ,20, 15
		AudioCodec.audio_codec_init(30);

		int decodeSize = 0;
		while (isDecoding) {
			while (dataList.size() > 0) {
				AudioData encodedData = dataList.remove(0);
				decodedData = new byte[MAX_BUFFER_SIZE];
				byte[] data = encodedData.getRealData();
				decodeSize = AudioCodec.audio_decode(data, 0, encodedData.getSize(), decodedData, 0);
				if (decodeSize > 0) {
					player.addData(decodedData, decodeSize);
					decodedData = new byte[decodedData.length];
				}
			}
		}
		player.stopPlaying();
	}

	public void stopDecoding() {
		this.isDecoding = false;
	}
}