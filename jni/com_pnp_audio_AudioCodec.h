#include <jni.h>

#ifndef _Included_com_pnp_audio_AudioCodec
#define _Included_com_pnp_audio_AudioCodec
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint JNICALL Java_com_pnp_audio_AudioCodec_audio_1codec_1init
  (JNIEnv *, jclass, jint);


JNIEXPORT jint JNICALL Java_com_pnp_audio_AudioCodec_audio_1encode
  (JNIEnv *, jclass, jbyteArray, jint, jint, jbyteArray, jint);

JNIEXPORT jint JNICALL Java_com_pnp_audio_AudioCodec_audio_1decode
  (JNIEnv *, jclass, jbyteArray, jint, jint, jbyteArray, jint);

#ifdef __cplusplus
}
#endif
#endif
