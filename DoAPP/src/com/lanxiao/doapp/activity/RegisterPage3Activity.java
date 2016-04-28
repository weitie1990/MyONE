package com.lanxiao.doapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.doapp.R;
import com.lanxiao.doapp.adapter.CompanyAdapter;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.PersonInfo;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterPage3Activity extends BaseActivity implements View.OnClickListener {
    RelativeLayout rl_register_createunit;
    List<Map<String, String>> list;
    ListView mlist;
    CompanyAdapter commentAdapter;
    String userid = null;
    String phone=null;
    String password=null;
    private EditText query;
    TextView tv_comment_serch;
    String type=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page3);
        init();
    }

    private void init() {
        type=getIntent().getStringExtra("type");
        tv_comment_serch= (TextView) findViewById(R.id.tv_comment_serch);
        list = new ArrayList<>();
        query= (EditText) findViewById(R.id.query);
        phone = getIntent().getStringExtra("cellphonenumber");
        password = getIntent().getStringExtra("password");
        mlist = (ListView) findViewById(R.id.list);
        View header = getLayoutInflater().inflate(R.layout.do_unit_header, null);
        ButterKnife.inject(this,header);
        mlist.addHeaderView(header);
        if(type==null) {
            String userInfo = getIntent().getStringExtra("userinfo");
            paseJson(userInfo);
        }
        saveToDb();
        commentAdapter = new CompanyAdapter(this, list,phone,password,type);
        mlist.setAdapter(commentAdapter);
        mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // search(s.toString());
                if (s.length() > 0) {
                    tv_comment_serch.setVisibility(View.VISIBLE);
                } else {
                    tv_comment_serch.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        tv_comment_serch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SerchComPanyFromServer(query.getText().toString().trim());
            }
        });
    }

    /**
     * 第一次进来保存个人详细信息.获取好友列表 本地DB
     */
    private void saveToDb() {
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("userid", userid);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.USER_PERSON_INFO, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                    PersonInfo pi = new PersonInfo();
                    pi.setUserid(userid);
                    pi.setNickname(jb.optString("nickname"));
                    pi.setLastName(jb.optString("LastName"));
                    pi.setBornDate(jb.optString("BornDate"));
                    pi.setSex(jb.optString("Sex"));
                    pi.setCertificateID(jb.optString("CertificateID"));
                    pi.setTag1(jb.optString("tag1"));
                    pi.setJobTitle(jb.optString("JobTitle"));
                    pi.setCellPhoneNumber(jb.optString("CellPhoneNumber"));
                    pi.setMailAddress(jb.optString("MailAddress"));
                    pi.setPhoneNumber(jb.optString("PhoneNumber"));
                    pi.setCountry(jb.optString("Country"));
                    pi.setCity(jb.optString("City"));
                    pi.setCompanyName(jb.optString("CompanyName"));
                    pi.setCompanyID(jb.optString("CompanyID"));
                    pi.setDepartment(jb.optString("Department"));
                    pi.setDepartmentID(jb.optString("DepartmentID"));
                    pi.setState(jb.optString("State"));
                    try {
                        DemoApplication.getInstance().getDb().save(pi);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Utils.showToast("无网络，请检查网络设置", getApplicationContext());
            }
        });

    }
    private void SerchComPanyFromServer(String s){
        HttpUtils httpUtils=new HttpUtils(5000);
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("companyshortname",s);
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.SEARCH_COMPANY, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                paseJson2(responseInfo.result);
                commentAdapter.resh(list);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                Utils.showToast("请求数据失败",RegisterPage3Activity.this);
                LogUtils.i(s);
            }
        });
    }
    /**
     * 解析JSON数据
     *
     * @param userInfo
     */
    private void paseJson(String userInfo) {
        try {
            JSONObject jb = new JSONObject(userInfo);
            userid=jb.optString("userid");
            DemoHelper.getInstance().setCurrentUserName(userid);
            JSONArray ja = jb.getJSONArray("companylist");
            if (ja != null||ja.length()==0) {
                for (int i = 0; i < ja.length(); i++) {
                    Map<String, String> map = new HashMap<>();
                    JSONObject jb2 = ja.getJSONObject(i);
                    map.put("companyname", jb2.optString("companyname"));
                    map.put("companyid", jb2.optString("companyid"));
                    map.put("userid", userid);
                    list.add(map);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 解析JSON数据
     *
     * @param userInfo
     */
    private void paseJson2(String userInfo) {
        list.clear();
        try {
            JSONObject jb = new JSONObject(userInfo);
            JSONArray ja = jb.getJSONArray("companylist");
            if (ja != null||ja.length()==0) {
                for (int i = 0; i < ja.length(); i++) {
                    Map<String, String> map = new HashMap<>();
                    JSONObject jb2 = ja.getJSONObject(i);
                    map.put("companyname", jb2.optString("companyname"));
                    map.put("companyid", jb2.optString("companyid"));
                    list.add(map);
                }
            }else {
                Utils.showToast("未搜索到类似公司！",RegisterPage3Activity.this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.rl_register_createunit, R.id.rl_register_nocreate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_register_createunit:
                Intent intent1=new Intent(RegisterPage3Activity.this, CreateUnitActivity.class);
                if(type!=null&&type.equals("1")) {
                    intent1.putExtra("type","1");
                }else {
                    intent1.putExtra("cellphonenumber", phone);
                    intent1.putExtra("password", password);
                }
                startActivity(intent1);
                finish();
                break;
            case R.id.rl_register_nocreate:
                if(type!=null&&type.equals("1")) {
                    Intent intent = new Intent(RegisterPage3Activity.this, SetActivity_set.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(RegisterPage3Activity.this, LoginActivity.class);
                    intent.putExtra("cellphonenumber", phone);
                    intent.putExtra("password", password);
                    startActivity(intent);
                }
                finish();
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
