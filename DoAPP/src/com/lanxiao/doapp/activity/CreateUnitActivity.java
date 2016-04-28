package com.lanxiao.doapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

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
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CreateUnitActivity extends BaseActivity {
    String type=null;
    @InjectView(R.id.et_register_short)
    EditText etRegisterShort;
    @InjectView(R.id.et_register_full)
    EditText etRegisterFull;
    String phone=null;
    String password=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_createunit);
        ButterKnife.inject(this);
        type=getIntent().getStringExtra("type");
        phone = getIntent().getStringExtra("cellphonenumber");
        password = getIntent().getStringExtra("password");
    }

    public void save(View v) {
        if(TextUtils.isEmpty(etRegisterShort.getText().toString())){
            Utils.showToast("简称不能为空", CreateUnitActivity.this);
            return;
        }
        if(TextUtils.isEmpty(etRegisterFull.getText().toString())){
            Utils.showToast("全称不能为空",CreateUnitActivity.this);
            return;
        }
        pd.setMessage("正在创建....");
        pd.show();
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("companyshortname", etRegisterShort.getText().toString());
        params.addBodyParameter("companyname", etRegisterFull.getText().toString());
        HttpUtils httpUtils=new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.CREATE_COMPANY, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                    if (jb.optString("result").equals("1")) {
                            Utils.showToast("创建成功", CreateUnitActivity.this);
                        pd.dismiss();
                        if(type!=null&&type.equals("1")){
                                PersonInfo pf = DemoApplication.getInstance().getDb().findFirst(Selector.from(PersonInfo.class).where("userid", "=", DemoHelper.getInstance().getCurrentUsernName()));
                              pf.setCompanyID(jb.optString("companyid"));
                              pf.setCompanyName(etRegisterFull.getText().toString());
                              DemoApplication.getInstance().getDb().update(pf);
                            Intent intent = new Intent(CreateUnitActivity.this, SetActivity_set.class);
                            startActivity(intent);
                            finish();
                        }else {
                            PersonInfo pf = DemoApplication.getInstance().getDb().findFirst(Selector.from(PersonInfo.class).where("userid", "=", DemoHelper.getInstance().getCurrentUsernName()));
                            pf.setCompanyID(jb.optString("companyid"));
                            pf.setCompanyName(etRegisterFull.getText().toString());
                            DemoApplication.getInstance().getDb().update(pf);
                            Intent intent = new Intent(CreateUnitActivity.this, LoginActivity.class);
                            intent.putExtra("cellphonenumber",phone);
                            intent.putExtra("password",password);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        Utils.showToast("创建失败", CreateUnitActivity.this);
                        pd.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Utils.showToast("无网络，请检查网络设置", CreateUnitActivity.this);
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
