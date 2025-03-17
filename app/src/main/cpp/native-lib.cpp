#include <jni.h>
#include <string>

extern "C" {
JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_getZeepLiveThimblesCoinIvKey(JNIEnv *env, jclass clazz) {
    std::string baseURL = "DSEBYE6LWE9FW7YE";
    return env->NewStringUTF(baseURL.c_str());
}
}
