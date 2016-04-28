package com.lanxiao.doapp.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.easeui.Conversion;
import com.example.doapp.R;
import com.lanxiao.doapp.MultiImageSelector.MultiImageSelectorActivity;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.PersonInfo;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.myView.CircularImage;
import com.lanxiao.doapp.myView.MainSetItemView;
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

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SetActivity_set extends BaseActivity {
    @InjectView(R.id.message_title)
    TextView messageTitle;
    @InjectView(R.id.iv_userPic)
    CircularImage ivUserPic;
    @InjectView(R.id.iv_userinfo_name)
    TextView ivUserinfoName;
    @InjectView(R.id.iv_userinfo_id)
    TextView ivUserinfoId;
    @InjectView(R.id.iv_setting_twocode)
    ImageView ivSettingTwocode;
    @InjectView(R.id.tv_userset_dosum)
    TextView tvUsersetDosum;
    @InjectView(R.id.tv_userset_friendsum)
    TextView tvUsersetFriendsum;
    @InjectView(R.id.tv_userset_attentionsum)
    TextView tvUsersetAttentionsum;
    @InjectView(R.id.tv_userset_funs)
    TextView tvUsersetFuns;
    @InjectView(R.id.ll_setting_personuserinfo)
    MainSetItemView llSettingPersonuserinfo;

    @InjectView(R.id.ll_setting_unit)
    MainSetItemView llSettingUnit;

    @InjectView(R.id.ll_setting_team)
    MainSetItemView llSettingTeam;

    @InjectView(R.id.ll_setting_post)
    MainSetItemView llSettingPost;
    @InjectView(R.id.ll_setting_general_setting)
    MainSetItemView llSettingGeneralSetting;
    @InjectView(R.id.ll_setting_safe_setting)
    MainSetItemView llSettingSafeSetting;
    @InjectView(R.id.ll_setting_help)
    MainSetItemView llSettingHelp;

    String mood=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frament_setting);
       ButterKnife.inject(this);
        init();
    }

    private void init() {
        ivUserinfoName.setText(Conversion.getInstance().getNickName());
        ivUserinfoId.setText("ID号：" + DemoHelper.getInstance().getCurrentUsernName());
        Utils.setAver(Conversion.getInstance().getHeadUrl(), ivUserPic);
        try {
            PersonInfo Parent = DemoApplication.getInstance().getDb().findFirst(Selector.from(PersonInfo.class).where("userid", "=", DemoHelper.getInstance().getCurrentUsernName()));
            if (Parent != null) {
                llSettingUnit.setMiddleText(Parent.getCompanyName());
                llSettingTeam.setMiddleText(Parent.getDepartment());
                llSettingPersonuserinfo.setMiddleText(Parent.getLastName());
                companyName = Parent.getCompanyName();
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        getUserInfofromServer();

    }

    /**
     * 从服务器上面获取用户信息
     */
    private void getUserInfofromServer() {
        RequestParams params = new RequestParams("UTF-8");
        LogUtils.i(DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.USER_INFO, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                    Utils.setAver(Conversion.getInstance().getHeadUrl(), ivUserPic);
                    ivUserinfoName.setText(Conversion.getInstance().getNickName());
                    tvUsersetDosum.setText(jb.optString("todo"));
                    tvUsersetFriendsum.setText(jb.optString("friends"));
                    tvUsersetAttentionsum.setText(jb.optString("follow"));
                    tvUsersetFuns.setText(jb.optString("fans"));
                    llSettingPersonuserinfo.setMiddleText(jb.optString("LastName"));
                    llSettingUnit.setMiddleText(jb.optString("CompanyName"));
                    companyName = jb.optString("CompanyName");
                    llSettingTeam.setMiddleText(jb.optString("Department"));
                    llSettingPost.setMiddleText(jb.optString("sign"));
                    mood = jb.optString("mood");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Utils.showToast("请求数据失败", SetActivity_set.this);
            }
        });
    }

    /**
     * 检查当前用户是否被删除
     */
    public void back(View v) {
        finish();
    }

    public void logout(View v) {
        final ProgressDialog pd = new ProgressDialog(SetActivity_set.this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        DemoHelper.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // 重新显示登陆页面
                        startActivity(new Intent(SetActivity_set.this, LoginActivity.class));
                        finish();
                        DemoApplication.getInstance().destoryActivity("MainActivity");
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(SetActivity_set.this, "unbind devicetokens failed", Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x16 && resultCode == 0x17) {
            resh();
            return;
        }
        //系统相机返回信息
        if(requestCode==0x00&&resultCode==RESULT_OK){
            List<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            for(int i=0;i<mSelectPath.size();i++){
                String imagePath=mSelectPath.get(i);
                Utils.updateAver(this, imagePath, pd, ivUserPic);
            }
        }
    }

    private void resh() {
        getUserInfofromServer();
    }

    @OnClick({R.id.iv_userPic, R.id.iv_setting_twocode, R.id.ll_setting_personuserinfo, R.id.ll_setting_unit, R.id.ll_setting_team, R.id.ll_setting_post, R.id.ll_setting_general_setting, R.id.ll_setting_safe_setting, R.id.ll_setting_help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_userPic:
                Utils.intoCimera(this,0);
                break;
            case R.id.iv_setting_twocode:
                CareateQcr(Conversion.getInstance().getHeadUrl(),Conversion.getInstance().getNickName());
                break;
            //进入个人资料界面
            case R.id.ll_setting_personuserinfo:
                startActivityForResult(new Intent(SetActivity_set.this, PersonUserinfoActivity.class), 0x16);
                break;
            //进入单位资料界面
            case R.id.ll_setting_unit:
                if (isCompanyExist()) {
                    startActivityForResult(new Intent(SetActivity_set.this, UnitUserinfoActivity.class), 0x16);
                }
                break;
            //进入部门资料界面
            case R.id.ll_setting_team:
                if (isCompanyExist()) {
                    startActivityForResult(new Intent(SetActivity_set.this, TearmUserinfoActivity.class), 0x16);
                }
                break;
            //进入签名资料界面
            case R.id.ll_setting_post:
                Intent intent=new Intent(SetActivity_set.this, PostUserinfoActivity.class);
                intent.putExtra("sign",llSettingPost.getMiddleText());
                intent.putExtra("mood",Integer.parseInt(mood));
                startActivityForResult(intent, 0x16);
                break;
            case R.id.ll_setting_general_setting:
                break;
            case R.id.ll_setting_safe_setting:
                startActivity(new Intent(SetActivity_set.this,SafeSet.class));
                break;
            case R.id.ll_setting_help:
                break;

        }
    }


    boolean flag = true;

    /**
     * 公司不存在时询问是否创建
     */
    public boolean isCompanyExist() {
        if (TextUtils.isEmpty(companyName)) {
            flag = false;
            new AlertDialog.Builder(this).setMessage("你还未创建或加入公司,现在是否创建或加入？").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(SetActivity_set.this, RegisterPage3Activity.class);
                    intent.putExtra("type", "1");
                    startActivity(intent);
                    finish();
                }
            }).setNeutralButton("取消", null).show();
        }
        return flag;
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
