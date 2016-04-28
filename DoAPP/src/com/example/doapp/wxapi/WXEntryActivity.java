package com.example.doapp.wxapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.example.doapp.R;
import com.lanxiao.doapp.activity.BaseActivity;
import com.lanxiao.doapp.activity.MainActivity;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.DoUser;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.http.Json;
import com.lanxiao.doapp.myView.CircleProgressBar;
import com.lanxiao.doapp.untils.MyStatic;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Thinkpad on 2015/11/18.
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    // IWXAPI 是第三方app和微信通信的openapi接口
    private String TAG="WXEntryActivity";
    private IWXAPI api;
    public static final String APP_ID = "wxfb89c1c93dcc556a";
    public static final String APP_SECRET = "d4624c36b6795d1d99dcf0547af5443d";
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    public static String ACCESS_TOKEN="access_token";
    public static String REFRESH_TOKEN="refresh_toke";
    public static String OPENID="openid";
    Map<String,String> map=new HashMap<>();
    Map<String,Object> WXUserInfo=new HashMap<>();
    String mopenid="";
    String nickname="";
    String headimgurl="";
    int sex=0;
    String province="";
    String city="";
    private static WXEntryActivity intance;
    private String unionid="";
    private SharedPreferences sp;
    public static WXEntryActivity getIntance() {
        return intance;
    }
    private String saveName;
    private CircleProgressBar McircleProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circleprogressbar);
        McircleProgressBar= (CircleProgressBar) findViewById(R.id.McircleProgressBar);
        intance=this;
        api=WXAPIFactory.createWXAPI(this,APP_ID);
        api.handleIntent(getIntent(), this);
        sp=getSharedPreferences("config",MODE_PRIVATE);
        Log.i("weitie","api");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }
    public WXEntryActivity(){

    }
    public interface IListen{
        void listen();
    }
    IListen iListen;
    public void setListen(IListen iListen){
        if(iListen!=null) {
            this.iListen = iListen;
            LogUtils.i("iListen");
        }
    }
    @Override
    public void onResp(final BaseResp resp) {
        if(resp!=null){
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    switch (resp.getType()){
                        case RETURN_MSG_TYPE_LOGIN:
                            McircleProgressBar.setVisibility(View.VISIBLE);
                            //拿到了微信返回的code,立马再去请求access_token
                            String code = ((SendAuth.Resp) resp).code;
                            LogUtils.i(code);
                            String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+APP_ID+"&secret="+APP_SECRET+"&code="+code+"&grant_type=authorization_code";
                            //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
                            HttpUtils httpUtils=new HttpUtils();
                            httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                    map= Json.getAccectToken(responseInfo.result);
                                    String accToken=map.get(ACCESS_TOKEN);
                                    String openId=map.get(OPENID);
                                    getUserInfo(accToken,openId);
                                    LogUtils.i(accToken);
                                }

                                @Override
                                public void onFailure(HttpException e, String s) {

                                }
                            });
                            break;
                            case RETURN_MSG_TYPE_SHARE:
                                Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
                                    aginSubmit(1);
                                    finish();
                                break;

                    }
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Toast.makeText(this, "操作已取消", Toast.LENGTH_LONG).show();
                    aginSubmit(0);
                    finish();
                    break;
                default:
                    Toast.makeText(this, "授权失败", Toast.LENGTH_LONG).show();
                    finish();
                    break;
            }

        }

    }

    private void aginSubmit(final int statas) {
        String bodevalue=DemoApplication.getInstance().getBodyvalue();
        HttpUtils httpUtils=new HttpUtils();
        RequestParams rp=new RequestParams();
        rp.addBodyParameter("status",statas+"");
        rp.addBodyParameter("docid",bodevalue);
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.WEIXIN_STATAUE_SUBMIT, rp, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException e, String s) {
                LogUtils.i(s);
            }
        });
    }

    /**
     * 获取微信用户个人信息
     * @param access_token 获取access_token时给的
     * @param openid 获取access_token时给的
     * @return URL
     */
    public void getUserInfo(String access_token,String openid){

        String url="https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid;
        HttpUtils httpUtils=new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    LogUtils.i(responseInfo.result);
                    mopenid = jsonObject.optString("openid");
                    nickname = jsonObject.optString("nickname");
                    headimgurl = jsonObject.optString("headimgurl");
                    LogUtils.i(headimgurl);
                    unionid = jsonObject.optString("unionid");
                    sex = jsonObject.optInt("sex");
                    province = jsonObject.optString("province");
                    city = jsonObject.optString("city");
                    WXUserInfo.put("nickname", nickname);
                    WXUserInfo.put("headimgurl", headimgurl);
                    WXUserInfo.put("unionid", unionid);
                    WXUserInfo.put("sex", sex);
                    WXUserInfo.put("province", province);
                    WXUserInfo.put("city", city);
                    LogUtils.i(nickname);
                    SharedPreferences.Editor et = sp.edit();
                    et.putBoolean("wxlogin", true);
                    et.commit();
                    saveName=sp.getString("saveName","");
                    tijaio(unionid);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(HttpException e, String s) {
                LogUtils.i(s);
            }
        });
    }

    /**
     * 微信用户登陆
     * @param unionid
     */
    public void tijaio(String unionid){
            RequestParams params = new RequestParams("UTF-8");
            params.addBodyParameter("wxunionid", unionid);
        params.addBodyParameter("wxuserhead",headimgurl);
        params.addBodyParameter("wxusername",nickname);
       params.addBodyParameter("appversion", Utils.getVersion(getApplicationContext()));
        Log.i(TAG, "appversion:" + Utils.getVersion(getApplicationContext()));
        HttpUtils httpUtils = new HttpUtils();
        String url= Api.UP_WEIXIN_INFO;
        DemoApplication.getInstance().setAver(headimgurl);
        httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                LogUtils.i(arg0.getMessage() + ":" + arg1);
                return;
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                    Login(responseInfo.result);
                    //mHandler.sendMessageDelayed(mHandler.obtainMessage(1, responseInfo.result), 1000 * 2);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void Login(String responseInfo){
        try {
            JSONObject jsonObject = new JSONObject(responseInfo);
            if(jsonObject.optString("result").equals("0")) {
                final String userid = jsonObject.optString("userid");
                final String name = jsonObject.optString("nickname");
                LogUtils.i(nickname);
                // 调用sdk登陆方法登陆聊天服务器
                EMChatManager.getInstance().login(userid, "a", new EMCallBack() {
                    @Override
                    public void onSuccess() {

                        // 登陆成功，保存用户名
                        DemoHelper.getInstance().setCurrentUserName(userid);
                        // 注册群组和联系人监听
                        DemoHelper.getInstance().registerGroupAndContactListener();

                        // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                        // ** manually load all local groups and
                        EMGroupManager.getInstance().loadAllGroups();
                        EMChatManager.getInstance().loadAllConversations();


                        // 进入主页面
                        DemoApplication.getInstance().destoryActivity("LoginActivity");
                        Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                        int sex = (int) WXUserInfo.get("sex");
                        String province = (String) WXUserInfo.get("province");
                        String city = (String) WXUserInfo.get("city");
                        LogUtils.i("sex:" + sex);
                        setAlias(userid);
                        setTag(sex, province, city);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(final int code, final String message) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                        Toast.LENGTH_SHORT).show();
                                LogUtils.i(R.string.Login_failed + message);
                            }
                        });
                    }
                });
            }else {
                Utils.showToast(jsonObject.optString("bodyvalue"),WXEntryActivity.this);
            }
        } catch (JSONException e) {
            LogUtils.i("JSon解析异常");
            e.printStackTrace();
        }

    }



}
