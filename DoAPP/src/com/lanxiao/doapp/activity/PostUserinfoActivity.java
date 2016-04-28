package com.lanxiao.doapp.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.doapp.R;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.widget.AutoRadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PostUserinfoActivity extends BaseActivity {

    @InjectView(R.id.et_sign_content)
    EditText etSignContent;
    @InjectView(R.id.radioGroup_sign_xinqing)
    AutoRadioGroup radioGroupSignXinqing;
    int mood = 0;
    @InjectView(R.id.radio0)
    RadioButton radio0;
    @InjectView(R.id.radio1)
    RadioButton radio1;
    @InjectView(R.id.radio2)
    RadioButton radio2;
    @InjectView(R.id.radio3)
    RadioButton radio3;
    @InjectView(R.id.radio4)
    RadioButton radio4;
    @InjectView(R.id.btn_register_next)
    Button btnRegisterNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_info);
        ButterKnife.inject(this);
        int type=getIntent().getIntExtra("type", 0);
        if(type==1){
            showMode();
        }else {
            String content = getIntent().getStringExtra("sign");
            mood = getIntent().getIntExtra("mood", 0);
            etSignContent.setText(content);
            setChecked();
        }
        radioGroupSignXinqing.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setmood(checkedId);
            }
        });
        etSignContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSignContent.setCursorVisible(true);
            }
        });
    }

    public void back(View v) {
        setResult(0x17);
        finish();
    }

    public void next(View v) {
        SerchComPanyFromServer();
    }

    private void SerchComPanyFromServer() {
        if (TextUtils.isEmpty(etSignContent.getText().toString())) {
            Utils.showToast("签名不能为空", PostUserinfoActivity.this);
            return;
        }
        pd.setMessage("正在提交...");
        pd.show();
        HttpUtils httpUtils = new HttpUtils(5000);
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("fieldname","sign"+","+"mood");
        params.addBodyParameter("fieldvalue",etSignContent.getText().toString()+","+mood);
        LogUtils.i("sign:" + etSignContent.getText().toString() + ",mood:" + mood);
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.UPDATAUSER, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                pd.dismiss();
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                    if (jb.optString("result").equals("1")) {
                        Utils.showToast("提交成功", getApplicationContext());
                        setResult(0x17);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                pd.dismiss();
                e.printStackTrace();
                Utils.showToast("提交数据失败", PostUserinfoActivity.this);
                LogUtils.i(s);
            }
        });
    }
    private void showMode() {
        HttpUtils httpUtils = new HttpUtils(5000);
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.USERMOOD, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                    etSignContent.setText(jb.optString("sign"));
                    mood = jb.optInt("mood");
                    setChecked();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                Utils.showToast("数据获取失败", PostUserinfoActivity.this);
                LogUtils.i(s);
            }
        });
    }
    private void setChecked(){
        switch (mood){
            case 0:
                radio0.setChecked(true);
                break;
            case 1:
                radio1.setChecked(true);
                break;
            case 2:
                radio2.setChecked(true);
                break;
            case 3:
                radio3.setChecked(true);
                break;
            case 4:
                radio4.setChecked(true);
                break;

        }
    }
    private void setmood(int radioId){
        switch (radioId){
            case R.id.radio0:
                mood=0;
                break;
            case R.id.radio1:
                mood=1;
                break;
            case R.id.radio2:
                mood=2;
                break;
            case R.id.radio3:
                mood=3;
                break;
            case R.id.radio4:
                mood=4;
                break;

        }
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
