#include <jni.h>
#include <string>



extern "C" JNIEXPORT jstring JNICALL
Java_com_mirkowu_jintest_jni_JniTest_stringFromJNI(JNIEnv *env, jclass clazz) {
    std::string hello = "Hello from C++  by 静态注册";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_mirkowu_jintest_jni_JniTest_stringFromJniWithStr(JNIEnv *env, jclass clazz,
                                                          jobject context, jstring value) {

    const char *str = env->GetStringUTFChars(value, JNI_FALSE);
    return env->NewStringUTF(str);
}


