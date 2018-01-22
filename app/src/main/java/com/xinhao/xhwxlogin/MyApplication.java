package com.xinhao.xhwxlogin;

import android.app.Application;

import com.xinhao.xhwxlogin.wxapi.WxLogin;


/**
 * Created by 14178 on 2018/1/19.
 */

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        WxLogin.initWx(this);
    }
}
