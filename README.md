废话不多说，直接开始

[Demo传送门](https://github.com/MirkoWu/JinTest)

 **注意：无论是动静态注册还是动态注册，Java端代码都是一样的，所谓的静动态注册是指在C/C++里面的操作，下面会具体讲**


### 静态注册
java代码:
```java
public class JniTest {
    static {
        System.loadLibrary("jniTest");
    }

    public static native String stringFromJNI();
    public static native String stringFromJniWithStr(Object context,String value);
}
```
C/C++代码：
```cpp
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
```

### 动态注册

 动态注册基本流程

 1. 编写Java端的相关native方法
 2. 编写C/C++代码, 实现JNI_Onload()方法
 3. 将Java 方法和 C/C++方法通过签名信息一一对应起来
 4. 通过JavaVM获取JNIEnv, JNIEnv主要用于获取Java类和调用一些JNI提供的方法
 5. 使用类名和对应起来的方法作为参数, 调用JNI提供的函数RegisterNatives()注册方法

` 所谓的动态注册 是指，动态注册JAVA的Native方法，使得c/c++里面方法名 可以和 java 的Native方法名可以不同，
 动态注册是将将二者方法名关联起来，以后在修改Native方法名时，只需修改动态注册关联的方法名称即可。
System.loadLibrary("xxx"); 这个方法还是必须要调用的，不管动态还是静态。
 `

java代码：
```java
public class JniDynamicRegisterTest {

    static {
        System.loadLibrary("dynamic_register");
    }

    public static native String a();
    public static native String b(Object context,String value);
}
```

C/C++代码：
```cpp
#include <jni.h>
#include <string>
#include <assert.h>

// ------------------------------- 以下是动态注册 --------------------------------

jstring aaa(JNIEnv *env, jclass clazz) {
    std::string hello = "Hello from C++  by 动态注册";
    return env->NewStringUTF(hello.c_str());
}

jstring getStringWithDynamicReg(JNIEnv *env, jclass clazz, jobject context, jstring value) {
    const char *str = env->GetStringUTFChars(value, JNI_FALSE);
    return env->NewStringUTF(str);
}



/**
 * 所谓的动态注册 是指，动态注册JAVA的Native方法，使得c/c++里面方法名 可以和 java 的Native方法名可以不同，
 * 动态注册是将将二者方法名关联起来，以后在修改Native方法名时，只需修改动态注册关联的方法名称即可
 *  System.loadLibrary("xxx"); 这个方法还是必须要调用的，不管动态还是静态
 */
#define JNIREG_CLASS "com/mirkowu/jintest/jni/JniDynamicRegisterTest"  //Java类的路径：包名+类名
#define NUM_METHOES(x) ((int) (sizeof(x) / sizeof((x)[0]))) //获取方法的数量


static JNINativeMethod method_table[] = {
        // 第一个参数a 是java native方法名，
        // 第二个参数 是native方法参数,括号里面是传入参的类型，外边的是返回值类型，
        // 第三个参数 是c/c++方法参数,括号里面是返回值类型，
        {"a", "()Ljava/lang/String;",                                     (jstring *) aaa},
        {"b", "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/String;", (jstring *) getStringWithDynamicReg},

};

static int registerMethods(JNIEnv *env, const char *className,
                           JNINativeMethod *gMethods, int numMethods) {
    jclass clazz = env->FindClass(className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    //注册native方法
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        return JNI_FALSE;
    }
    return JNI_TRUE;
}


extern "C"
JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {

    JNIEnv *env = NULL;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    assert(env != NULL);

// 注册native方法
    if (!registerMethods(env, JNIREG_CLASS, method_table, NUM_METHOES(method_table))) {
        return JNI_ERR;
    }

    return JNI_VERSION_1_6;
}

```

### 签名
JNI 签名
动态注册中 JNINativeMethod 结构体中第二个参数需注意
括号内代表传入参数的签名符号，为空可以不写，括号外代表返回参数的签名符号,为空填写 V，对应关系入下表

签名符号 | C/C++ | java
-------     | ----- | -----
V | void | void
Z|jboolean|boolean
I|jint|int
J|jlong|long
D|jdouble|double
F|jfloat|float
B|jbyte|byte
C|jchar|char
S|jshort|short
[Z|jbooleanArray|boolean[]
[I|jintArray|int[]
[J|jlongArray|long[]
[D|jdoubleArray|double[]
[F|jfloatArray|float[]
[B|jbyteArray|byte[]
[C|jcharArray|char[]
[S|jshortArray|short[]
 特殊的String||
Ljava/lang/String;|jstring|String
L完整包名加类名;|jobject|class

举个例子:
传入的java参数有两个 分别是 int 和 long[] 函数返回值为 String 
即函数的定义为：String getString(int a ,long[] b)  
签名就应该是 :"(I[J)Ljava/lang/String;"(不要漏掉英文分号)  
如果有内部类 则用 $ 来分隔 如:Landroid/os/FileUtils$FileStatus;

### 总结
**静态注册**
- 优点: 理解和使用方式简单, 属于傻瓜式操作, 使用相关工具按流程操作就行, 出错率低
- 缺点: 当需要更改类名,包名或者方法时, 需要按照之前方法重新生成头文件, 灵活性不高

**动态注册**
- 优点: 灵活性高, 更改类名,包名或方法时, 只需对更改模块进行少量修改, 效率高
- 缺点: 对新手来说稍微有点难理解, 同时会由于搞错签名, 方法, 导致注册失败


 
### 参考
[输出多个so库参考这里](https://blog.csdn.net/b2259909/article/details/58591898)
