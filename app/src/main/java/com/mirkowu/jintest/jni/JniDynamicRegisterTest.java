package com.mirkowu.jintest.jni;

/**
 * @author by DELL
 * @date on 2020/8/22
 * @describe
 */
public class JniDynamicRegisterTest {

    static {
        System.loadLibrary("dynamic_register");
    }

    public static native String a();
    public static native String b(Object context,String value);
}
