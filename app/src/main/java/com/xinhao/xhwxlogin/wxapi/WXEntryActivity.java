package com.xinhao.xhwxlogin.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xinhao.xhwxlogin.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    private IWXAPI mWeixinAPI;

    private TextView id_tv;
    private TextView nickname_tv;
    private TextView sex_tv;
    private ImageView imageView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        id_tv = findViewById(R.id.id_tv);
        nickname_tv = findViewById(R.id.nickname_tv);
        sex_tv = findViewById(R.id.sex_tv);
        imageView = findViewById(R.id.imageView);
        handler = new Handler();

        mWeixinAPI = WXAPIFactory.createWXAPI(this, WxData.WEIXIN_APP_ID, true);
        mWeixinAPI.handleIntent(this.getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWeixinAPI.handleIntent(intent, this);//必须调用此句话
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.e("-----", "onReq: " + baseReq);


        finish();
    }


    @Override
    public void onResp(BaseResp baseResp) {
        Log.e("-----", "errStr: " + baseResp.errStr);
        Log.e("-----", "openId: " + baseResp.openId);
        Log.e("-----", "transaction: " + baseResp.transaction);
        Log.e("-----", "errCode: " + baseResp.errCode);
        Log.e("-----", "getType: " + baseResp.getType());
        Log.e("-----", "checkArgs: " + baseResp.checkArgs());


        switch (baseResp.errCode) {

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (RETURN_MSG_TYPE_SHARE == baseResp.getType()) UIUtils.runOnUIToast("分享失败");
                else UIUtils.runOnUIToast("登录失败");
                break;
            case BaseResp.ErrCode.ERR_OK:
                switch (baseResp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        //拿到了微信返回的code,立马再去请求access_token
                        String code = ((SendAuth.Resp) baseResp).code;
                        getAccess_token(code);
                        //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求

                        Log.e("--------", "code: " + code);
                        id_tv.setText("code:       " + code);
                        break;

                    case RETURN_MSG_TYPE_SHARE:
                        UIUtils.runOnUIToast("微信分享成功");
                        finish();
                        break;
                }
                break;
        }
    }


    /**
     * 获取openid accessToken值用于后期操作
     *
     * @param code 请求码
     */
    private void getAccess_token(final String code) {
        String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + WxData.WEIXIN_APP_ID
                + "&secret="
                + WxData.APP_SECRET
                + "&code="
                + code
                + "&grant_type=authorization_code";
        OkHttpUtils.get().url(path).build().execute(new StringCallback() {

            @Override
            public void onError(okhttp3.Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {

                Log.e("-----", "onResponse: " + response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String openid = jsonObject.getString("openid").toString().trim();
                    String access_token = jsonObject.getString("access_token").toString().trim();
                    getUserMesg(access_token, openid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    /**
     * 获取微信的个人信息
     *
     * @param access_token
     * @param openid
     */
    private void getUserMesg(final String access_token, final String openid) {
        String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + access_token
                + "&openid="
                + openid;


        OkHttpUtils.get().url(path).build().execute(new StringCallback() {


            @Override
            public void onError(okhttp3.Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                Log.e("------", "全部数据: " + response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String nickname = jsonObject.getString("nickname");
                    int sex = Integer.parseInt(jsonObject.get("sex").toString());
                    String headimgurl = jsonObject.getString("headimgurl");
                    String openid1 = jsonObject.getString("openid");
                    Log.e("---", "用户基本信息:");
                    Log.e("---", "nickname:" + nickname);
                    nickname_tv.setText("nickname:       " +nickname);
                    Log.e("---", "sex:       " + sex);
                    sex_tv.setText("sex:       " + sex + "");
                    Log.e("---", "headimgurl:" + headimgurl);

                    imageUrl(headimgurl);
                    //    startLoca(nickname, openid1);
                } catch (JSONException e) {
                    e.printStackTrace();
                    UIUtils.runOnUIToast("登陆错误,请重新再试");

                }
                // finish();
            }
        });

    }


    //请求显示图像

    private void imageUrl(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url1 = new URL(url);
                    HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setReadTimeout(2000);
                    int responseCode = urlConnection.getResponseCode();

                    if (responseCode == 200) {
                        InputStream inputStream = urlConnection.getInputStream();

                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
