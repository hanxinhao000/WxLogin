package com.xinhao.xhwxlogin.wxapi;

/**
 * XINHAO_HAN存储信息
 */

public class WxData {


    /**
     * 你的AppID
     *
     * 在此特别注意,本Demo只适用微信的登陆功能,并不能用到微信支付功能
     *
     * 在此注意,必须要和微信官网签名一致,否则调用不起来微信APP,
     *
     * 如果在你调用出错的情况下(微信APP死活不出来的情况下),请参阅作者简书网址 : https://www.jianshu.com/p/04ed0b65f3df
     *
     * 微信APP掉不出来:有以下原因:
     *
     * 1.签名不正确(APK所使用签名的MD5码) 签名MD5码不要有 : 如A0:5B:12:63.... ,要全部是小写(推荐)a05b1263...,这种形式的
     *
     * 2.APP_ID不正确
     *
     * 3.密匙不正确
     *
     * 4.包名不正确
     *
     * 不走回调WXEntryActivity
     *
     * 1.AndroidManifest.xml里没有配置
     *
     * <activity
     *       android:name=".wxapi.WXEntryActivity"
     *       android:exported="true"
     *       android:label="WXEntryActivity" />
     *
     *
     *
     *
     *
     *
     *
     */
    public static final String WEIXIN_APP_ID = "wxd806f2b48efb3eb3";
    public static final String APP_SECRET = "61b3537ebf50a93a242a1535782dbe97";

    /**
     * 固定的
     *
     */

    public static final String SCOPE = "snsapi_userinfo";
    public static final String STATE = "wechat_sdk_demo_test_neng";


}
