package com.xinhao.xhwxlogin.wxapi;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 14178 on 2018/1/19.
 */

public class UIUtils {

    private static Context mContext;

    public static void initContext(Context context) {

        mContext = context;
    }

    public static void runOnUIToast(String str){
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();

    }
}
