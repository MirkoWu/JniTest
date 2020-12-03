package com.mirkowu.jintest;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mirkowu.jintest.jni.JniDynamicRegisterTest;
import com.mirkowu.jintest.jni.JniTest;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        TextView tv2 = findViewById(R.id.sample_text2);
        TextView tv3= findViewById(R.id.sample_text3);
        TextView tv4 = findViewById(R.id.sample_text4);
        tv.setText(JniTest.stringFromJNI());
        tv2.setText(JniTest.stringFromJniWithStr(this,"我是通过静态注册返回的Java"));
        tv3.setText(JniDynamicRegisterTest.a( ));
        tv4.setText(JniDynamicRegisterTest.b(this,"我是通过动态注册返回的"));


        Log.d("MainActivity","SUPPORTED_ABIS = "+ Arrays.toString(Build.SUPPORTED_ABIS));
        Log.d("MainActivity","SUPPORTED_64_BIT_ABIS = "+ Arrays.toString(Build.SUPPORTED_64_BIT_ABIS));
        Log.d("MainActivity","SUPPORTED_32_BIT_ABIS = "+ Arrays.toString(Build.SUPPORTED_32_BIT_ABIS));
        Log.d("MainActivity","abi[0] = "+Build.SUPPORTED_ABIS[0]);
        Log.d("MainActivity","CPU_ABI = "+Build.CPU_ABI);
        Log.d("MainActivity","nativeLibraryDir = "+getApplication().getApplicationInfo().nativeLibraryDir);

//        this.getApplication().getApplicationInfo().nativeLibraryDir;

        //32位和64位 的so文件不能混用 ，但是32位之间可以混用（但不建议，最好使用对应的类型），64位之间可以混用（但不建议，最好使用对应的类型）
        //abiFilters 最好配置，如果支持64位，且文件夹里也有对应so文件，可以配上对应cpu类型，例arm64-v8a；如果没有64位so文件，不要配这个类型的



        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
