package com.pnp.audio;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;

public class AudioPlayer implements Runnable {

	private static AudioPlayer player;
	private List<AudioData> dataList = null;
	private AudioData playData;
	private boolean isPlaying = false;

	private AudioTrack audioTrack;

	private AudioPlayer() {
		dataList = Collections.synchronizedList(new LinkedList<AudioData>());
	}

	public static AudioPlayer getInstance() {
		if (player == null) {
			player = new AudioPlayer();
		}
		return player;
	}

	public void addData(byte[] rawData, int size) {
		AudioData decodedData = new AudioData();
		decodedData.setSize(size);
		byte[] tempData = new byte[size];
		System.arraycopy(rawData, 0, tempData, 0, size);
		decodedData.setRealData(tempData);
		dataList.add(decodedData);
	}

	@SuppressWarnings("deprecation")
	private boolean initAudioTrack() {
		int bufferSize = AudioRecord.getMinBufferSize(AudioConfig.SAMPLERATE, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioConfig.AUDIO_FORMAT);
		// CHANNEL_OUT_MONO:0x00000004 CHANNEL_IN_MONO:0x00000010
		if (bufferSize < 0) {
			return false;
		}
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, AudioConfig.SAMPLERATE, AudioFormat.CHANNEL_OUT_MONO, AudioConfig.AUDIO_FORMAT,
				bufferSize, AudioTrack.MODE_STREAM);
		// set volume:ÉèÖÃ²¥·ÅÒôÁ¿
		audioTrack.setStereoVolume(1.0f, 1.0f);
		audioTrack.play();
		return true;
	}

	private void playFromList() throws IOException {
		while (isPlaying) {
			while (dataList.size() > 0) {
				playData = dataList.remove(0);
				audioTrack.write(playData.getRealData(), 0, playData.getSize());
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
		}
	}

	public void startPlaying() {
		if (isPlaying) {
			return;
		}
		new Thread(this).start();
	}

	public void run() {
		this.isPlaying = true;
		if (!initAudioTrack()) {
			return;
		}
		try {
			playFromList();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (this.audioTrack != null) {
			// audioTrack.getPlayState() == AudioTrack.PLAYSTATE_STOPPED;
			if (this.audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
				this.audioTrack.stop();
				this.audioTrack.release();
			}
		}
	}

	public void stopPlaying() {
		this.isPlaying = false;
	}
}
