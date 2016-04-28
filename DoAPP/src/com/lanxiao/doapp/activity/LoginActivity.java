/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lanxiao.doapp.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.easeui.utils.EaseCommonUtils;
import com.example.doapp.R;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.PersonInfo;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * 登陆页面
 *
 */
public class LoginActivity extends BaseActivity {
    public static final String TAG = "LoginActivity";
    public static final int REQUEST_CODE_SETNICK = 1;
    @InjectView(R.id.tv_register)
    TextView tvRegister;
    private EditText usernameEditText;
    private EditText passwordEditText;

    private boolean progressShow;
    private boolean autoLogin = false;

    private String currentUsername;
    private String currentPassword;
    private TextView tv_logoin_weixinlogin;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 如果登录成功过，直接进入主页面
        if (DemoHelper.getInstance().isLoggedIn()) {
            autoLogin = true;
            startActivity(new Intent(LoginActivity.this, MainActivity.class));

            return;
        }
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        pd = new ProgressDialog(LoginActivity.this);
        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        tv_logoin_weixinlogin = (TextView) findViewById(R.id.tv_logoin_weixinlogin);
        //实现微信登陆功能
        tv_logoin_weixinlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WXLogin();

            }
        });
        // 如果用户名改变，清空密码
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordEditText.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (DemoHelper.getInstance().getCurrentUsernName() != null) {
            try {
                PersonInfo Parent = DemoApplication.getInstance().getDb().findFirst(Selector.from(PersonInfo.class).where("userid", "=", DemoHelper.getInstance().getCurrentUsernName()));

                if(Parent!=null) {
                    usernameEditText.setText(Parent.getCellPhoneNumber());
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        pd.dismiss();
    }

    /**
     * 登录
     *
     * @param view
     */
    public void login(View view) {
        if (!EaseCommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
        currentUsername = usernameEditText.getText().toString().trim();
        currentPassword = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        progressShow = true;
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        pd.show();
        deauftLogin();
        final long start = System.currentTimeMillis();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        currentUsername = intent.getStringExtra("cellphonenumber");
        currentPassword = intent.getStringExtra("password");
        LogUtils.i("currusername:"+currentUsername+",password:"+currentPassword);
        progressShow = true;
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        pd.show();
        deauftLogin();

    }

    /**
     * 注册
     *
     * @param view
     */
    public void mregister(View view) {
        startActivityForResult(new Intent(this, RegisterActivity.class), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (autoLogin) {
            return;
        }
    }

    /**
     * 微信登陆
     */
    private void WXLogin() {
        if (!DemoApplication.getInstance().getApi().isWXAppInstalled()) {
            Toast.makeText(getApplicationContext(), "您还未安装微信客户端", Toast.LENGTH_LONG).show();
            return;
        }
        pd.setMessage("正在获取微信信息");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        DemoApplication.getInstance().getApi().sendReq(req);
        DemoApplication.getInstance().addDestoryActivity(this, TAG);
    }

    @OnClick(R.id.tv_register)
    public void onClick() {
        DemoApplication.getInstance().addDestoryActivity(this, TAG);
        startActivityForResult(new Intent(this, RegisterActivity.class), 0);
    }

    /**
     * 本地服务器登陆
     */
    public void deauftLogin(){
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("username",currentUsername);
        params.addBodyParameter("password", currentPassword);
        params.addBodyParameter("appversion",Utils.getVersion(getApplicationContext()));
        HttpUtils httpUtils=new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.DEAUFULT_LOGIN, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                    if (jb.optString("result").equals("1")) {
                        final String userid = jb.optString("userid");
                        //Utils.showToast("登陆成功", getApplicationContext());
                        // 调用sdk登陆方法登陆聊天服务器
                        LogUtils.i(userid);
                        EMChatManager.getInstance().login(userid, "a", new EMCallBack() {

                            @Override
                            public void onSuccess() {
                                if (!progressShow) {
                                    return;
                                }
                                // 登陆成功，保存用户名
                                DemoHelper.getInstance().setCurrentUserName(userid);
                                // 注册群组和联系人监听
                                DemoHelper.getInstance().registerGroupAndContactListener();
                                // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                                // ** manually load all local groups and
                                EMGroupManager.getInstance().loadAllGroups();
                                EMChatManager.getInstance().loadAllConversations();

                                if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                                    pd.dismiss();
                                }
                                setAlias(userid);
                                // 进入主页面
                                Intent intent = new Intent(LoginActivity.this,
                                        MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onProgress(int progress, String status) {
                            }

                            @Override
                            public void onError(final int code, final String message) {
                                if (!progressShow) {
                                    return;
                                }
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    } else {
                        Utils.showToast("登陆失败", getApplicationContext());
                        pd.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                pd.dismiss();
                Utils.showToast("登陆失败,请尝试重新登陆！", getApplicationContext());
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


}
