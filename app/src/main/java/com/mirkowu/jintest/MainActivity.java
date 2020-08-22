package com.mirkowu.jintest;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mirkowu.jintest.jni.JniDynamicRegisterTest;
import com.mirkowu.jintest.jni.JniTest;

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
    }


}
