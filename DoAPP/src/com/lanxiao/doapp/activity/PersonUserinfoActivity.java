package com.lanxiao.doapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.easeui.Conversion;
import com.example.doapp.R;
import com.lanxiao.doapp.MultiImageSelector.MultiImageSelectorActivity;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.PersonInfo;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.myView.CircularImage;
import com.lanxiao.doapp.myView.MainSetItemView;
import com.lanxiao.doapp.untils.MyStatic;
import com.lanxiao.doapp.untils.util.MyQRCodeUtils;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PersonUserinfoActivity extends BaseActivity {


    @InjectView(R.id.iv_userPic)
    CircularImage ivUserPic;
    @InjectView(R.id.iv_userinfo_name)
    TextView ivUserinfoName;
    @InjectView(R.id.iv_userinfo_id)
    TextView ivUserinfoId;
    @InjectView(R.id.iv_setting_twocode)
    ImageView ivSettingTwocode;
    @InjectView(R.id.ll_person_nickname)
    MainSetItemView llPersonNickname;
    @InjectView(R.id.ll_person_name)
    MainSetItemView llPersonName;
    @InjectView(R.id.ll_person_birth)
    MainSetItemView llPersonBirth;
    @InjectView(R.id.ll_person_sex)
    MainSetItemView llPersonSex;
    @InjectView(R.id.ll_person_idcard)
    MainSetItemView llPersonIdcard;
    @InjectView(R.id.ll_person_lable)
    MainSetItemView llPersonLable;
    @InjectView(R.id.ll_person_post)
    MainSetItemView llPersonPost;
    @InjectView(R.id.ll_person_moblephone)
    MainSetItemView llPersonMoblephone;
    @InjectView(R.id.ll_person_email)
    MainSetItemView llPersonEmail;
    @InjectView(R.id.ll_person_phone)
    MainSetItemView llPersonPhone;
    @InjectView(R.id.ll_person_address)
    MainSetItemView llPersonAddress;
    android.support.v7.app.AlertDialog.Builder alertDialog;
    ImageView ivQcrUserpic;
    TextView ivQcrUserNick;
    ImageView imgQcr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_userinfo);
        ButterKnife.inject(this);
        init();
    }

    private void init() {
        View view = getLayoutInflater().inflate(R.layout.alert_qcr_show, null);
        ivQcrUserpic= (ImageView) view.findViewById(R.id.iv_qcr_userpic);
        imgQcr= (ImageView) view.findViewById(R.id.img_qcr);
        ivQcrUserNick= (TextView) view.findViewById(R.id.iv_qcr_userNick);
        Utils.setAver(Conversion.getInstance().getHeadUrl(), ivQcrUserpic);
        ivQcrUserNick.setText(Conversion.getInstance().getNickName());
        alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialog.setView(view);
        myAlertDialog=alertDialog.create();
        Utils.setAver(Conversion.getInstance().getHeadUrl(), ivUserPic);
        try {
            PersonInfo Parent = DemoApplication.getInstance().getDb().findFirst(Selector.from(PersonInfo.class).where("userid", "=", DemoHelper.getInstance().getCurrentUsernName()));
            if(Parent!=null){
                llPersonName.setMiddleText(Parent.getLastName());
                llPersonBirth.setMiddleText(Parent.getBornDate());
                llPersonSex.setMiddleText(Parent.getSex());
                llPersonIdcard.setMiddleText(Parent.getCertificateID());
                llPersonLable.setMiddleText(Parent.getTag1());
                llPersonPost.setMiddleText(Parent.getJobTitle());
                llPersonMoblephone.setMiddleText(Parent.getCellPhoneNumber());
                llPersonEmail.setMiddleText(Parent.getMailAddress());
                llPersonPhone.setMiddleText(Parent.getPhoneNumber());
                llPersonAddress.setMiddleText(Parent.getCountry());
            }else {
                getUserInfofromServer();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        ivUserinfoName.setText(Conversion.getInstance().getNickName());
        ivUserinfoId.setText("ID号：" + DemoHelper.getInstance().getCurrentUsernName());
        llPersonNickname.setMiddleText(Conversion.getInstance().getNickName());
    }

    public void back(View v) {
        setResult(0x17);
        finish();
    }

    /**
     * 从服务器上面获取用户信息
     */
    private void getUserInfofromServer() {
        RequestParams params = new RequestParams("UTF-8");
        LogUtils.i(DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.USER_PERSON_INFO, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                    llPersonName.setMiddleText(jb.optString("LastName"));
                    llPersonBirth.setMiddleText(jb.optString("BornDate"));
                    llPersonSex.setMiddleText(jb.optString("Sex"));
                    llPersonIdcard.setMiddleText(jb.optString("CertificateID"));
                    llPersonLable.setMiddleText(jb.optString("tag1"));
                    llPersonPost.setMiddleText(jb.optString("JobTitle"));
                    llPersonMoblephone.setMiddleText(jb.optString("CellPhoneNumber"));
                    llPersonEmail.setMiddleText(jb.optString("MailAddress"));
                    llPersonPhone.setMiddleText(jb.optString("PhoneNumber"));
                    llPersonAddress.setMiddleText(jb.optString("Country"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Utils.showToast("无网络，请检查网络设置", PersonUserinfoActivity.this);
            }
        });
    }

    @OnClick({R.id.iv_userPic,R.id.ll_person_nickname, R.id.ll_person_name, R.id.ll_person_birth, R.id.ll_person_sex, R.id.ll_person_idcard, R.id.ll_person_lable, R.id.ll_person_post, R.id.ll_person_moblephone, R.id.ll_person_email, R.id.ll_person_phone, R.id.ll_person_address,R.id.iv_setting_twocode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_userPic:
                Utils.intoCimera(this,0);
                break;
            case R.id.iv_setting_twocode:
                CareateQcr();
                break;
            case R.id.ll_person_nickname:
                MyDiogAlret("设置昵称", llPersonNickname,ivUserinfoName);
                break;
            case R.id.ll_person_name:
                MyDiogAlret("设置姓名", llPersonName);
                break;
            case R.id.ll_person_birth:
                DateDiogAlret("设置出生日期", llPersonBirth);
                break;
            case R.id.ll_person_sex:
                SexDiogAlret("设置性别", llPersonSex);
                break;
            case R.id.ll_person_idcard:
                MyDiogAlret("设置身份证",llPersonIdcard);
                break;
            case R.id.ll_person_lable:
                MyDiogAlret("设置标签", llPersonLable);
                break;
            case R.id.ll_person_post:
                MyDiogAlret("设置职务",llPersonPost);
                break;
            case R.id.ll_person_moblephone:
                MyDiogAlret("设置手机", llPersonMoblephone);
                break;
            case R.id.ll_person_email:
                MyDiogAlret("设置Email", llPersonEmail);
                break;
            case R.id.ll_person_phone:
                MyDiogAlret("设置电话",llPersonPhone);
                break;
            case R.id.ll_person_address:
                MyDiogAlret("设置地区",llPersonAddress);
                break;
        }
    }
    Bitmap bitmap = null;
    android.support.v7.app.AlertDialog myAlertDialog;

    private void CareateQcr() {
        if(myAlertDialog.isShowing()){
            myAlertDialog.dismiss();
        }
        Boolean IsExistQcr = sp.getBoolean("qcr", false);
        String headUrl = Conversion.getInstance().getHeadUrl();

        if (TextUtils.isEmpty(headUrl)) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ease_default_avatar);
        } else {
            Picasso.with(this).load(headUrl).placeholder(R.drawable.ease_default_avatar).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap1, Picasso.LoadedFrom loadedFrom) {
                    bitmap=bitmap1;
                }

                @Override
                public void onBitmapFailed(Drawable drawable) {

                }

                @Override
                public void onPrepareLoad(Drawable drawable) {

                }
            });

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
    }
    int item = 0;

    //设置性别的diogalrt
    public void SexDiogAlret(final String name, final MainSetItemView tv) {
        String[] sex = {"男", "女"};
        int checked=0;
        if(!TextUtils.isEmpty(tv.getMiddleText())){
            if(tv.getMiddleText().equals("男")){
                checked=0;
            }else {
                checked=1;
            }
        }
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this).setTitle(name).setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(sex, checked, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        item = which;
                    }
                }).setPositiveButton(R.string.dl_ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nickString = null;
                if (item == 0) {
                    nickString = "男";
                } else {
                    nickString = "女";
                }
                pd.setTitle("更新中....");
                pd.show();
                updatainfo(nickString, "设置性别", tv, getFieldname("设置性别", editText));
            }
        }).setNegativeButton(R.string.dl_cancel, null).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //系统相机返回信息
        if(requestCode==0x00&&resultCode==RESULT_OK){
            List<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            for(int i=0;i<mSelectPath.size();i++){
                String imagePath=mSelectPath.get(i);
                Utils.updateAver(this, imagePath, pd, ivUserPic);
            }
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
