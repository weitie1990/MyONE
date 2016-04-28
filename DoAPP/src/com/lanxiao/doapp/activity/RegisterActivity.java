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

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.doapp.R;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.untils.util.Utils;
import com.umeng.analytics.MobclickAgent;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 注册页
 *
 */
public class RegisterActivity extends BaseActivity {
    @InjectView(R.id.et_register_phone)
    EditText etRegisterPhone;
    @InjectView(R.id.et_register_yanzhengma)
    EditText etRegisterYanzhengma;
    @InjectView(R.id.et_register_get)
    ImageView etRegisterGet;
    @InjectView(R.id.password)
    EditText password;
    @InjectView(R.id.btn_register_next)
    Button btnRegisterNext;
    public static final String TAG="RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);

    }


    public void next(View v) {
        if(!Utils.isMobileNO(etRegisterPhone.getText().toString())){
            Utils.showToast("请输入正确的手机号",RegisterActivity.this);
            return;
        }
        if(TextUtils.isEmpty(etRegisterPhone.getText().toString())){
            Utils.showToast("手机号不能为空",RegisterActivity.this);
            return;
        }
        if(TextUtils.isEmpty(password.getText().toString())){
            Utils.showToast("密码不能为空不能为空",RegisterActivity.this);
            return;
        }
        DemoApplication.getInstance().addDestoryActivity(this,TAG);
        Intent intent = new Intent(RegisterActivity.this, RegisterPage2Activity.class);
        intent.putExtra("cellphonenumber",etRegisterPhone.getText().toString());
        intent.putExtra("password",password.getText().toString());
        startActivity(intent);
    }

    @OnClick(R.id.et_register_get)
    public void onClick() {
        StringBuffer sb=new StringBuffer();
        Random random=new Random();
        for (int i=0;i<4;i++){
            sb.append(random.nextInt(10));
        }
        etRegisterYanzhengma.setText(sb.toString());
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
