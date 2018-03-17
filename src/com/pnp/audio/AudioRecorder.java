package com.pnp.audio;

import android.media.AudioRecord;

public class AudioRecorder implements Runnable {

	private boolean isRecording = false;
	private AudioRecord audioRecord;

	private static final int BUFFER_FRAME_SIZE = 480;
	private int audioBufSize = 0;

	private byte[] samples;// data
	private int bufferRead = 0;
	private int bufferSize = 0;

	public void startRecording() {
		bufferSize = BUFFER_FRAME_SIZE;

		audioBufSize = AudioRecord.getMinBufferSize(AudioConfig.SAMPLERATE,
				AudioConfig.RECORDER_CHANNEL_CONFIG, AudioConfig.AUDIO_FORMAT);
		if (audioBufSize == AudioRecord.ERROR_BAD_VALUE) {
			return;
		}
		samples = new byte[audioBufSize];
		if (null == audioRecord) {
			audioRecord = new AudioRecord(AudioConfig.AUDIO_RESOURCE, AudioConfig.SAMPLERATE,
					AudioConfig.RECORDER_CHANNEL_CONFIG, AudioConfig.AUDIO_FORMAT, audioBufSize);
		}
		new Thread(this).start();
	}

	public void stopRecording() {
		this.isRecording = false;
	}

	public boolean isRecording() {
		return isRecording;
	}

	public void run() {
		AudioEncoder encoder = AudioEncoder.getInstance();
		encoder.startEncoding();
		audioRecord.startRecording();

		this.isRecording = true;
		while (isRecording) {
			bufferRead = audioRecord.read(samples, 0, bufferSize);
			if (bufferRead > 0) {
				encoder.addData(samples, bufferRead);// 640 480
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		audioRecord.stop();
		encoder.stopEncoding();
	}
}
