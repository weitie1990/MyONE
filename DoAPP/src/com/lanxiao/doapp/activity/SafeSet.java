package com.lanxiao.doapp.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.doapp.R;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.http.Api;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SafeSet extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_set);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.message_back, R.id.ll_setting_updatePassword})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.message_back:
                finish();
                break;
            case R.id.ll_setting_updatePassword:
                initView();
                break;
        }
    }
    private void initView(){
        View v=getLayoutInflater().inflate(R.layout.dialog_set_pwd,null);
        final TextView password= (TextView) v.findViewById(R.id.et_pwd);
        final TextView passwordConfirm= (TextView) v.findViewById(R.id.et_pwd_confirm);
        new AlertDialog.Builder(this).setTitle("设置密码").setView(v).setNeutralButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(password.getText().toString()) || TextUtils.isEmpty(passwordConfirm.getText().toString())) {
                    com.lanxiao.doapp.untils.util.Utils.showToast("输入不能为空", SafeSet.this);
                    show(dialog);
                    return;
                } else if (password.getText().toString().equals(passwordConfirm.getText().toString())) {
                    //修改密码
                    updatainfo(password.getText().toString());
                    diss(dialog);
                    return;
                } else {
                    com.lanxiao.doapp.untils.util.Utils.showToast("再次密码不一致,请重新输入", SafeSet.this);
                    show(dialog);
                    return;
                }
            }
        }).setNegativeButton("取消", null).show();
    }
    /**
     * 修改密码
     */
    public void updatainfo(String password){
        RequestParams params = new RequestParams("UTF-8");
        LogUtils.i(DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("fieldname","password");
        params.addBodyParameter("fieldvalue",password);
        HttpUtils httpUtils=new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.UPDATAUSER, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                    if (jb.optString("result").equals("1")) {
                        com.lanxiao.doapp.untils.util.Utils.showToast("修改成功", getApplicationContext());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                com.lanxiao.doapp.untils.util.Utils.showToast("请求失败", SafeSet.this);
            }
        });
    }
    private void show(DialogInterface dialog){
        try
        {
            Field field = dialog.getClass()
                    .getSuperclass().getDeclaredField(
                            "mShowing" );
            field.setAccessible( true );
            // 将mShowing变量设为false，表示对话框已关闭
            field.set(dialog, false );
            //dialog.dismiss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void diss(DialogInterface dialog){
        try
        {
            Field field = dialog.getClass()
                    .getSuperclass().getDeclaredField(
                            "mShowing" );
            field.setAccessible( true );
            // 将mShowing变量设为false，表示对话框已关闭
            field.set(dialog, true );
            //dialog.dismiss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
