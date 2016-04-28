package com.lanxiao.doapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.doapp.R;
import com.lanxiao.doapp.adapter.TeamAdapter;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.ApplyItem;
import com.lanxiao.doapp.entity.PersonInfo;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.myView.MainUnittemView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TearmUserinfoActivity extends BaseActivity {
    TeamAdapter teamAdapter;
    List<ApplyItem> list;
    ListView listView;
    String commentid = null;
    String DepartmentName = null;
    @InjectView(R.id.ll_team_setting_addteam)
    MainUnittemView llTeamSettingAddteam;
    @InjectView(R.id.ll_team_setting_commpay)
    MainUnittemView ll_team_setting_commpay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);
        listView = (ListView) findViewById(R.id.list_view);
        initData();

    }

    private void initData() {
        View header = getLayoutInflater().inflate(R.layout.do_team_header, null);
        ButterKnife.inject(this, header);
        listView.addHeaderView(header);
        try {
            PersonInfo Parent = DemoApplication.getInstance().getDb().findFirst(Selector.from(PersonInfo.class).where("userid", "=", DemoHelper.getInstance().getCurrentUsernName()));
            ll_team_setting_commpay.settext(Parent.getCompanyName());
            commentid = Parent.getCompanyID();
            DepartmentName = Parent.getDepartment();
            getTeamInfo();
        } catch (DbException e) {
            e.printStackTrace();
        }

        list = new ArrayList<>();
    }

    public void back(View v) {
        setResult(0x17);
        finish();
    }

    @OnClick({R.id.ll_team_setting_addteam})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_team_setting_addteam:
                addTeam();
                break;
        }
    }

    /**
     * 增加下属部门
     */
    private void addTeam() {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this).setTitle("增加下属部门").setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                .setPositiveButton(R.string.dl_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nickString = editText.getText().toString();
                        if (TextUtils.isEmpty(nickString)) {
                            Toast.makeText(getApplicationContext(), "输入不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        pd.setTitle("更新中....");
                        pd.show();
                        updatainfo(nickString);
                    }
                }).setNegativeButton(R.string.dl_cancel, null).show();
    }

    /**
     * 增加下属部门
     */
    private void updatainfo(final String inputname) {
        RequestParams params = new RequestParams("UTF-8");
        LogUtils.i(DemoHelper.getInstance().getCurrentUsernName());
        try {
            final PersonInfo Parent = DemoApplication.getInstance().getDb().findFirst(Selector.from(PersonInfo.class).where("userid", "=", DemoHelper.getInstance().getCurrentUsernName()));
            params.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
            params.addBodyParameter("companyname", Parent.getCompanyName());
            params.addBodyParameter("companyid", Parent.getCompanyID());
            params.addBodyParameter("deptname", inputname);
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.send(HttpRequest.HttpMethod.POST, Api.CREATE_PARTMENT, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    pd.dismiss();
                    LogUtils.i(responseInfo.result);
                    try {
                        JSONObject jb = new JSONObject(responseInfo.result);
                        if (jb.optString("result").equals("1")) {
                            Utils.showToast("添加成功", getApplicationContext());
                            ApplyItem item = new ApplyItem(inputname, false);
                            item.setInfo(jb.optString("deptid"));
                            list.add(item);
                            teamAdapter.DateChange(list);//刷新UI
                        } else {
                            Utils.showToast("添加失败", getApplicationContext());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Utils.showToast("无网络，请检查网络设置", getApplicationContext());
                    pd.dismiss();
                }
            });
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取部门
     */
    private void getTeamInfo() {
        RequestParams params = new RequestParams("UTF-8");
        LogUtils.i(commentid);
        params.addBodyParameter("companyid", commentid);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.Team_INFO, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                    if (jb.optString("result").equals("1")) {
                        JSONArray ja = jb.getJSONArray("companylist");
                        for (int i = 0; i < ja.length(); i++) {
                            ApplyItem item = new ApplyItem();
                            JSONObject jb2 = ja.getJSONObject(i);
                            item.setInfo(jb2.optString("deptid"));
                            item.setName(jb2.optString("deptname"));
                            LogUtils.i(DepartmentName);
                            if (jb2.optString("deptname").equals(DepartmentName)) {
                                item.setAdd(true);
                            } else {
                                item.setAdd(false);
                            }
                            list.add(item);
                        }
                        teamAdapter = new TeamAdapter(TearmUserinfoActivity.this, list);
                        listView.setAdapter(teamAdapter);

                    } else {
                        teamAdapter = new TeamAdapter(TearmUserinfoActivity.this, list);
                        listView.setAdapter(teamAdapter);
                        Utils.showToast("数据获取失败", getApplicationContext());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                LogUtils.i(s);
                teamAdapter = new TeamAdapter(TearmUserinfoActivity.this, list);
                listView.setAdapter(teamAdapter);
                Utils.showToast("无网络，请检查网络设置", getApplicationContext());
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
