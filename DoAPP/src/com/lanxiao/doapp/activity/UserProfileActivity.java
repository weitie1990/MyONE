package com.lanxiao.doapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.example.doapp.R;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.myView.CircularImage;
import com.lanxiao.doapp.myView.UserInfoItemView;
import com.lanxiao.doapp.untils.util.Utils;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UserProfileActivity extends BaseActivity {

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
    @InjectView(R.id.ll_usrinfo_name)
    UserInfoItemView llUsrinfoName;
    @InjectView(R.id.ll_usrinfo_unit)
    UserInfoItemView llUsrinfoUnit;
    @InjectView(R.id.ll_usrinfo_team)
    UserInfoItemView llUsrinfoTeam;
    @InjectView(R.id.ll_usrinfo_address)
    UserInfoItemView ll_usrinfo_address;
    @InjectView(R.id.ll_usrinfo_sign)
    TextView llUsrinfoSign;
    String username = null;
    String userpic = null;
    String userName = null;
    @InjectView(R.id.ll_usrinfo_phone)
    TextView llUsrinfoPhone;
    @InjectView(R.id.title)
    RelativeLayout title;
    @InjectView(R.id.iv_userinfo_logout)
    Button iv_userinfo_logout;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        getUserInfofromServer();
    }


    @OnClick({R.id.iv_setting_twocode, R.id.img_usrinfo_callphone,R.id.iv_userinfo_logout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting_twocode:
                CareateQcr(userpic, userName);
                break;
            case R.id.img_usrinfo_callphone:
                playPhone();
                break;
            case R.id.iv_userinfo_logout:
                showMyDaiog();
                break;
        }
    }
    private void playPhone(){
        PackageManager pm = getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.CALL_PHONE", "com.example.doapp"));
        if (permission) {
            if (!TextUtils.isEmpty(llUsrinfoPhone.getText())) {
                if(!Utils.isMobileNO(llUsrinfoPhone.getText().toString())){
                    Utils.showToast("不是正确的手机号码格式", UserProfileActivity.this);
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +
                        llUsrinfoPhone.getText()));
                startActivity(intent);

            }else {
                Utils.showToast("暂无号码", UserProfileActivity.this);
            }
        } else {
            Utils.showToast("你还未开启电话权限", UserProfileActivity.this);
        }
    }
    private void showMyDaiog() {
        new AlertDialog.Builder(UserProfileActivity.this).setItems(new String[]{"聊天", "语音电话", "打电话"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        startActivity(new Intent(UserProfileActivity.this, ChatActivity.class).putExtra("userId", username));
                        break;
                    case 1:
                        startVoiceCall();
                        break;
                    case 2:
                        playPhone();
                        break;
                }
            }
        }).show();
    }

    /**
     * 从服务器上面获取用户信息
     */
    private void getUserInfofromServer() {
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("userid", username);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.FRIENDUSERINFO, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                    ivUserinfoId.setText(username);
                    Utils.setAver(jb.optString("userhead"), ivUserPic);
                    userpic = jb.getString("userhead");
                    llUsrinfoName.setMiddleText(jb.optString("sex"));
                    tvUsersetDosum.setText(jb.optString("todo"));
                    tvUsersetFriendsum.setText(jb.optString("friends"));
                    tvUsersetAttentionsum.setText(jb.optString("follow"));
                    tvUsersetFuns.setText(jb.optString("fans"));
                    userName = jb.optString("LastName");
                    ivUserinfoName.setText(jb.optString("LastName"));
                    llUsrinfoUnit.setMiddleText(jb.optString("CompanyName"));
                    llUsrinfoTeam.setMiddleText(jb.optString("Department"));
                    ll_usrinfo_address.setMiddleText(jb.optString("city"));
                    llUsrinfoSign.setText(jb.optString("sign"));
                    llUsrinfoPhone.setText(jb.optString("cellphonenumber"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Utils.showToast("请求数据失败", UserProfileActivity.this);
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

    /**
     * 拨打视频电话
     */
    protected void startVideoCall() {
        if (!EMChatManager.getInstance().isConnected())
            Toast.makeText(UserProfileActivity.this, R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        else {
            startActivity(new Intent(UserProfileActivity.this, VideoCallActivity.class).putExtra("username", username)
                    .putExtra("isComingCall", false));
        }
    }
    /**
     * 拨打语音电话
     */
    protected void startVoiceCall() {
        if (!EMChatManager.getInstance().isConnected()) {
            Toast.makeText(UserProfileActivity.this, R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(UserProfileActivity.this, VoiceCallActivity.class).putExtra("username", username)
                    .putExtra("isComingCall", false));
        }
    }
}





