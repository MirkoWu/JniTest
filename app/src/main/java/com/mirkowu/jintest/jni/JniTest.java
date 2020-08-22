package com.mirkowu.jintest.jni;

/**
 * @author by DELL
 * @date on 2020/8/22
 * @describe
 */
public class JniTest {
    static {
        System.loadLibrary("jniTest");
    }

    public static native String stringFromJNI();
    public static native String stringFromJniWithStr(Object context,String value);
}
