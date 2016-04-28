package com.lanxiao.doapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.example.doapp.R;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.DoUser;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RegisterPage2Activity extends BaseActivity {
    String phone = null;
    String password = null;
    @InjectView(R.id.et_register_nickname)
    EditText etRegisterNickname;
    @InjectView(R.id.et_register_util)
    EditText etRegisterUtil;
    ProgressDialog pd;
    public static final String TAG="RegisterPage2Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page2);
        ButterKnife.inject(this);
        phone = getIntent().getStringExtra("cellphonenumber");
        password = getIntent().getStringExtra("password");
        pd = new ProgressDialog(this);
    }

    public void isfinish(View view) {
        if(TextUtils.isEmpty(etRegisterNickname.getText().toString())){
            Utils.showToast("昵称不能为空", RegisterPage2Activity.this);
            return;
        }
        if(TextUtils.isEmpty(etRegisterUtil.getText().toString())){
            Utils.showToast("单位不能为空",RegisterPage2Activity.this);
            return;
        }
        pd.setMessage(getResources().getString(R.string.Is_the_registered));
        pd.show();
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("cellphonenumber",phone);
        params.addBodyParameter("password", password);
        params.addBodyParameter("nickname", etRegisterNickname.getText().toString());
        params.addBodyParameter("companyshortname", etRegisterUtil.getText().toString());
        params.addBodyParameter("appversion",Utils.getVersion(getApplicationContext()));
        LogUtils.i("cellphonenumber:"+phone+"password:"+password+"nickname:"+etRegisterNickname.getText().toString()
        +"companyshortname:"+etRegisterUtil.getText().toString()
        );
        HttpUtils httpUtils=new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST,Api.DEAUFULT_REGISTER,params,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                pd.dismiss();
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                    if (jb.optString("result").equals("1")) {
                        DemoApplication.getInstance().addDestoryActivity(RegisterPage2Activity.this, TAG);
                        Intent intent = new Intent(RegisterPage2Activity.this, RegisterPage3Activity.class);
                        intent.putExtra("userinfo",responseInfo.result);
                        intent.putExtra("cellphonenumber",phone);
                        intent.putExtra("password",password);
                        startActivity(intent);
                        finish();

                    }
                    else if(jb.optString("result").equals("-1")){
                        Utils.showToast(jb.optString("message"), RegisterPage2Activity.this);
                        pd.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {
                pd.dismiss();
                Utils.showToast("无网络，请检查网络设置", RegisterPage2Activity.this);
            }
        });

    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
