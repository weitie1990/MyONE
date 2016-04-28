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

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.easeui.Conversion;
import com.easemob.easeui.widget.EaseAlertDialog;
import com.example.doapp.R;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.DoUser;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.untils.MyStatic;
import com.lanxiao.doapp.untils.util.MyQRCodeUtils;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddContactActivity extends BaseActivity implements View.OnClickListener {
    private EditText query;
    private ImageButton clearSearch;
    private TextView nameText, mTextView;
    private ImageView avatar;
    private InputMethodManager inputMethodManager;
    private String toAddUsername;

  /*  String userAver;
    String userNick;
    AlertDialog.Builder alertDialog;
    ImageView ivQcrUserpic;
    TextView ivQcrUserNick;
    ImageView imgQcr;
    AlertDialog myAlertDialog;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        ButterKnife.inject(this);
        init();
        mTextView = (TextView) findViewById(R.id.add_list_friends);
        query = (EditText) findViewById(R.id.query);
        clearSearch= (ImageButton) findViewById(R.id.search_clear);
        String strAdd = "查找&添加好友";
        mTextView.setText(strAdd);
        //nameText = (TextView) findViewById(R.id.name);
        //avatar = (ImageView) findViewById(R.id.avatar);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideKeyboard();
            }
        });
    }

    private void init() {
       /* View view = getLayoutInflater().inflate(R.layout.alert_qcr_show, null);
        ivQcrUserpic= (ImageView) view.findViewById(R.id.iv_qcr_userpic);
        imgQcr= (ImageView) view.findViewById(R.id.img_qcr);
        ivQcrUserNick= (TextView) view.findViewById(R.id.iv_qcr_userNick);
        Utils.setAver(Conversion.getInstance().getHeadUrl(), ivQcrUserpic);
        ivQcrUserNick.setText(Conversion.getInstance().getNickName());
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(view);
        myAlertDialog=alertDialog.create();*/

    }

    /**
     * 查找contact
     * @param
     */
    public void searchContact(String Type) {
        final String name = query.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            new EaseAlertDialog(this, "请输入查找内容").show();
            return;
        }
     /*   //判断是否为手机号查询
        if (Utils.isMobileNO(name)) {
            Type = "2";
        }
        LogUtils.i(Type);*/
        //服务器存在此用户，显示此用户和添加按钮
        searchContentServer(name, Type);
    }

    private String ToNickName = null;

    /**
     * 添加用户时查看用户是否存在
     */
    public void searchContentServer(String toAddUsername, final String type) {
        LogUtils.i(toAddUsername);
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("keyword", toAddUsername);
        params.addBodyParameter("type", type);
        HttpUtils httpUtils = new HttpUtils(5000);
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.SEARCH_FRIEND, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                LogUtils.i(arg0.getMessage() + ":" + arg1);
                Utils.showToast("请求数据失败", getApplicationContext());
                return;
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject js = new JSONObject(responseInfo.result);
                    if (js.optString("message").equals("成功")) {
                        List<DoUser> list = new ArrayList<DoUser>();
                        JSONArray ja = js.getJSONArray("userlist");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject js2 = ja.getJSONObject(i);
                            String userId = js2.optString("userid");
                            String heanUrl = js2.optString("userheadlogo");
                            String nicKName = js2.optString("nickname");
                            DoUser doUser = new DoUser();
                            doUser.setId(userId);
                            doUser.setNickName(nicKName);
                            doUser.setAver(heanUrl);
                            list.add(doUser);
                        }
                        Intent intent = new Intent(AddContactActivity.this, ShowFriendActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", (Serializable) list);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        if (type.equals("1") || type.equals("2")) {
                            Utils.showToast("没有此用户", getApplicationContext());
                        } else if (type.equals("3") || type.equals("4")) {
                            Utils.showToast("无此相关查找用户", getApplicationContext());
                        } else {
                            Utils.showToast("无此相关查找用户", getApplicationContext());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void back(View v) {
        finish();
    }


    @OnClick({R.id.contact_nichen_search, R.id.contact_id_search, R.id.contact_phone_search, R.id.contact_companyName_search, R.id.contact_nickname_search, R.id.contact_lication_search, R.id.contact_qcr_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contact_nichen_search:
                searchContact("5");
                break;
            case R.id.contact_id_search:
                searchContact("1");
                break;
            case R.id.contact_phone_search:
                searchContact("2");
                break;
            case R.id.contact_companyName_search:
                searchContact("6");
                break;
            case R.id.contact_nickname_search:
                searchContact("3");
                break;
            case R.id.contact_lication_search:
                searchContact("4");
                break;
            case R.id.contact_qcr_search:
                startActivity(new Intent(AddContactActivity.this,CaptureActivity.class));
                break;
        }
    }
   /* Bitmap bitmap = null;

    private void CareateQcr() {
        if(myAlertDialog.isShowing()){
            myAlertDialog.dismiss();
        }
        Boolean IsExistQcr = sp.getBoolean("qcr", false);
        String headUrl = Conversion.getInstance().getHeadUrl();

        if (TextUtils.isEmpty(headUrl)) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ease_default_avatar);
        } else {
            bitmap = ImageLoader.getInstance().loadImageSync(headUrl);
        }
        if (!IsExistQcr) {
            //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
            new Thread(new Runnable() {
                File file = MyStatic.createFile(MyStatic.FileCache + DemoHelper.getInstance().getCurrentUsernName() + ".jpg");

                @Override
                public void run() {
                    if(file.exists()){
                        file.delete();
                    }
                    boolean success = MyQRCodeUtils.createQRImage(DemoHelper.getInstance().getCurrentUsernName(), 600, 600,
                            bitmap,
                            file.getPath());

                    if (success) runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imgQcr.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                            myAlertDialog.show();
                        }
                    });
                }
            }).start();
        }
    }*/
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
