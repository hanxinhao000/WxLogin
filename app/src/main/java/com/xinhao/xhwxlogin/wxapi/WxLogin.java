package com.xinhao.xhwxlogin.wxapi;

import android.content.Context;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by 14178 on 2018/1/19.
 */

public class WxLogin {

    public static IWXAPI api;
    public static Context mContext;

    /**
     * 急的初始化
     *
     * @param context
     */
    public static void initWx(Context context) {
        UIUtils.initContext(context);
        mContext = context;
        api = WXAPIFactory.createWXAPI(context, WxData.WEIXIN_APP_ID, true);
        api.registerApp(WxData.WEIXIN_APP_ID);

        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
        // 将该app注册到微信
        msgApi.registerApp(WxData.WEIXIN_APP_ID);
    }

    public static void longWx() {
        if (mContext == null) {
            Toast.makeText(mContext, "你没有初始化,请在Application中做初始化动作,请调用 initWx(context)方法", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!api.isWXAppInstalled()) {
            Toast.makeText(mContext, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = WxData.SCOPE;
        req.state = WxData.STATE;
        api.sendReq(req);

    }


}
