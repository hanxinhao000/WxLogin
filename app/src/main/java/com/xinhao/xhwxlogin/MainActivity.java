package com.xinhao.xhwxlogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.xinhao.xhwxlogin.wxapi.WxLogin;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.startWx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WxLogin.longWx();

            }
        });

    }
}
