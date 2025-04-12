#include <jni.h>
#include <string>
#include <sstream>
extern "C" {

JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_getretroUrL(JNIEnv *env, jclass clazz) {
std::string baseURL = "https://oxadmedia.in/";
return env->NewStringUTF(baseURL.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_getUserregUrl(JNIEnv *env, jclass clazz) {
    std::string baseURL = "registerusers";
    return env->NewStringUTF(baseURL.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_enterInUrl(JNIEnv *env, jclass clazz) {
    std::string baseURL = "getgymlist";
    return env->NewStringUTF(baseURL.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_lkeyFetch(JNIEnv *env, jclass clazz) {
    std::stringstream ss;
    ss << "ags" << "10109512" << "001AoX";  // Append using stream

    return env->NewStringUTF(ss.str().c_str());
}

JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_gDatamembUrl(JNIEnv *env, jclass clazz) {
    std::string baseURL = "getmemberdata";
    return env->NewStringUTF(baseURL.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_vcoachUrl(JNIEnv *env, jclass clazz) {
std::string baseURL = "getdumbellworkout";
return env->NewStringUTF(baseURL.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_getProductUrl(JNIEnv *env, jclass clazz) {
    std::string baseURL = "getmyproducts";
    return env->NewStringUTF(baseURL.c_str());
}
JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_transCheckUrl(JNIEnv *env, jclass clazz) {
std::string baseURL = "gettransachistory";
return env->NewStringUTF(baseURL.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_gtDataUrl(JNIEnv *env, jclass clazz) {
    std::string baseURL = "gettrainerdata";
    return env->NewStringUTF(baseURL.c_str());
}
JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_guTableUrl(JNIEnv *env, jclass clazz) {
    std::string baseURL = "updatetable";
    return env->NewStringUTF(baseURL.c_str());
}
JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_plusNewCUrl(JNIEnv *env, jclass clazz) {
    std::string baseURL = "addnewcustomer";
    return env->NewStringUTF(baseURL.c_str());
}
JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_plusNewGymUrl(JNIEnv *env, jclass clazz) {
    std::string baseURL = "addnewgym";
    return env->NewStringUTF(baseURL.c_str());
}
JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_gutrainerUrl(JNIEnv *env, jclass clazz) {
    std::string baseURL = "updatetrainer";
    return env->NewStringUTF(baseURL.c_str());
}
JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_gudueUrl(JNIEnv *env, jclass clazz) {
    std::string baseURL = "updatedueamt";
    return env->NewStringUTF(baseURL.c_str());
}
JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_minusCustUrl(JNIEnv *env, jclass clazz) {
    std::string baseURL = "deletemycustomer";
    return env->NewStringUTF(baseURL.c_str());
}
JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_minusTrainerUrl(JNIEnv *env, jclass clazz) {
    std::string baseURL = "removetrainer";
    return env->NewStringUTF(baseURL.c_str());
}
JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_plusTrainerUrl(JNIEnv *env, jclass clazz) {
    std::string baseURL = "addmytrainer";
    return env->NewStringUTF(baseURL.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_uCPassUrl(JNIEnv *env, jclass clazz) {
    std::string baseURL = "updategymownerpassword";
    return env->NewStringUTF(baseURL.c_str());
}
JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_addProductUrl(JNIEnv *env, jclass clazz) {
    std::string baseURL = "addmyproduct";
    return env->NewStringUTF(baseURL.c_str());
}
JNIEXPORT jstring JNICALL
Java_com_oxad_fitox_retrofit_ApiHelper_updateProductUrl(JNIEnv *env, jclass clazz) {
    std::string baseURL = "updateproduct";
    return env->NewStringUTF(baseURL.c_str());
}
}
