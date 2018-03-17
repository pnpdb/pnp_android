package com.pnp.audio;


public class AudioCodec {

	static {
		System.loadLibrary("audiowrapper");
	}

	public static native int audio_codec_init(int mode);

	public static native int audio_encode(byte[] sample, int sampleOffset, int sampleLength, byte[] data, int dataOffset);

	public static native int audio_decode(byte[] data, int dataOffset, int dataLength, byte[] sample, int sampleLength);
}
