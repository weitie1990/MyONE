package com.lanxiao.doapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.easemob.easeui.Conversion;
import com.example.doapp.R;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.untils.CinemaUtil;
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

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SignInActivity extends BaseActivity {

    @InjectView(R.id.signin_back)
    ImageView signinBack;
    @InjectView(R.id.currentlocation_item_icon)
    ImageView currentlocationItemIcon;
    @InjectView(R.id.currentlocation_item_name)
    TextView currentlocationItemName;
    @InjectView(R.id.currentlocation_item_count)
    TextView currentlocationItemCount;
    @InjectView(R.id.iv_signin_cinema)
    ImageView ivSigninCinema;
    @InjectView(R.id.iv_signin_userpic)
    ImageView iv_signin_userpic;
    @InjectView(R.id.et_signin_beiju)
    EditText etSigninBeiju;
    @InjectView(R.id.rb_signin_client)
    RadioButton rbSigninClient;
    @InjectView(R.id.rb_signin_work)
    RadioButton rbSigninWork;
    @InjectView(R.id.btn_sigin_ok)
    Button btnSiginOk;
    private String lastImaegePath=null;
    double latitude;
    double longitude;
    String address=null;
    File file=null;
    private String type=null;
    int signinType=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.inject(this);
        PoiInfo poiInfo=getIntent().getParcelableExtra("poiInfo");
        address= poiInfo.address;
        String name=poiInfo.name;
        latitude= poiInfo.location.latitude;
        longitude=poiInfo.location.longitude;
        currentlocationItemName.setText(name);
        currentlocationItemCount.setText(address);
        rbSigninWork.setChecked(true);
    }

    @OnClick({R.id.signin_back, R.id.iv_signin_cinema, R.id.rb_signin_client, R.id.rb_signin_work, R.id.btn_sigin_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signin_back:
                finish();
                break;
            case R.id.iv_signin_cinema:
                Intent intent1 = CinemaUtil.IntoSystemPic(this);//进入系统拍照
                startActivityForResult(intent1, REQUST_OPEN_SYSTEM_CINEMA);
                break;
            case R.id.rb_signin_client:
                signinType=1;
                rbSigninClient.setChecked(true);
                break;
            case R.id.rb_signin_work:
                signinType=2;
                rbSigninWork.setChecked(true);
                break;
            case R.id.btn_sigin_ok:
                //startActivity(new Intent(SignInActivity.this,SignLogActivity.class));
                doToServer();
                break;
        }
    }
    /**
     * 提交
     */
    private void doToServer() {
        HttpUtils httpUtils=new HttpUtils("UTF-8");
        RequestParams rp=new RequestParams();
        rp.addBodyParameter("username", Conversion.getInstance().getNickName());
        rp.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
        rp.addBodyParameter("geopos", longitude + "," + latitude);
        rp.addBodyParameter("geoaddress", address);
        if(file!=null){
            rp.addBodyParameter("imageslist",file);
        }
        rp.addBodyParameter("signtype",signinType+"");
        rp.addBodyParameter("body",etSigninBeiju.getText().toString());
        pd.setTitle("正在在提交");
        pd.show();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.SIGNIN_SUMMIT, rp, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                pd.dismiss();
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                    if (jb.optString("result").equals("0")) {
                        Utils.showToast("提交成功", getApplication());
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.putExtra("type", 5);
                        startActivity(intent);
                    } else {
                        Utils.showToast("提交失败", getApplication());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                LogUtils.i(s);
                pd.dismiss();
                Utils.showToast("数据访问失败", getApplicationContext());
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //调用系统相机返回的信息
        if(requestCode==REQUST_OPEN_SYSTEM_CINEMA&&resultCode==RESULT_OK){
            file= CinemaUtil.getFile();
            lastImaegePath=file.getPath();
            Bitmap bitmap= CinemaUtil.getBitmap(file.getPath());
            Bitmap bp= CinemaUtil.zoomImg(bitmap, 300, 300);
            iv_signin_userpic.setImageBitmap(bp);
        }
        if(data==null){

            return;
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
