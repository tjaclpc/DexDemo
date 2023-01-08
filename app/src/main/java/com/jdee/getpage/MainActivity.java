package com.jdee.getpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.jdee.getpage.classinfo.ClsManager;
import com.jdee.getpage.tool.VerifyTool;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ClassManager.getInstance().test_ClassLoader(this);
        findViewById(R.id.hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context appContext = MainActivity.this.getApplication();
                ClsManager.getInstance().testDexClassLoader(appContext,"/sdcard/Test.dex");
            }
        });
        VerifyTool.verifySignature(this);
    }
}