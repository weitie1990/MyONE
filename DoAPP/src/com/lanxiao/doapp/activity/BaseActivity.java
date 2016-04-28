package com.lanxiao.doapp.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.easeui.Conversion;
import com.example.doapp.R;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.PersonInfo;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.myView.MainSetItemView;
import com.lanxiao.doapp.untils.DateUntils;
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
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.zhy.autolayout.AutoLayoutActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Thinkpad on 2015/11/9.
 */
public class BaseActivity extends AutoLayoutActivity {
    public SharedPreferences sp;
    //输入界面跳转到到联系人界面请求码
    public static final int REQUST_INPUT_FOR_CONTENT = 0x11;
    //获取系统资源的请求码
    public static final int REQUST_OPEN_SYSTEM_PIC = 0x12;
    //进入系统相机的请求码
    public static final int REQUST_OPEN_SYSTEM_CINEMA = 0x13;

    //本地定位信息返回码
    public static final int RESULT_LOCATION_CURRENT = 0X14;
    //本地搜索附近信息返回码
    public static final int RESULT_LOCATION_SEARCH = 0X15;
    //本索附近信息返回请求码
    public static final int REQUSET_LOCATION_CURRENT = 0X16;
    //评论界面返回我选择的文件请求码
    public static final int REQUSET_FILE_BACK = 0X17;
    //返回我选择的文件结果码
    public static final int RESULT_FILE_BACK = 0X18;
    //输入界面返回我选择的文件请求码
    public static final int REQUSET_INUPUT_FILE_BACK = 0x19;
    public static final int LOCATION_RESULT_SEND = 0x20;
    /**
     * 联系人显示名称
     **/
    public static final int PHONES_DISPLAY_NAME_INDEX = 0;
    /**
     * 电话号码
     **/
    public static final int PHONES_NUMBER_INDEX = 1;
    /**
     * 联系人的ID
     **/
    public static final int PHONES_SORT_KEY_PRIMARY = 2;

    /**
     * 获取库Phon表字段
     **/
    public static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY};

    public Drawable[] micImages;
    public ProgressDialog pd;
    String companyName = null;
    android.support.v7.app.AlertDialog.Builder alertDialog;
    ImageView ivQcrUserpic;
    TextView ivQcrUserNick;
    ImageView imgQcr;
    android.support.v7.app.AlertDialog myAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        isNetworkAvailable();
        // 动画资源文件,用于录制语音时
        micImages = new Drawable[]{getResources().getDrawable(R.drawable.ease_record_animate_01),
                getResources().getDrawable(R.drawable.ease_record_animate_02),
                getResources().getDrawable(R.drawable.ease_record_animate_03),
                getResources().getDrawable(R.drawable.ease_record_animate_04),
                getResources().getDrawable(R.drawable.ease_record_animate_05),
                getResources().getDrawable(R.drawable.ease_record_animate_06),
                getResources().getDrawable(R.drawable.ease_record_animate_07),
                getResources().getDrawable(R.drawable.ease_record_animate_08),
                getResources().getDrawable(R.drawable.ease_record_animate_09),
                getResources().getDrawable(R.drawable.ease_record_animate_10),
                getResources().getDrawable(R.drawable.ease_record_animate_11),
                getResources().getDrawable(R.drawable.ease_record_animate_12),
                getResources().getDrawable(R.drawable.ease_record_animate_13),
                getResources().getDrawable(R.drawable.ease_record_animate_14)};

    }

    @Override
    protected void onResume() {
        super.onResume();
        // onresume时，取消notification显示
        DemoHelper.getInstance().getNotifier().reset();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }


    /**
     * 隐藏软键盘
     */
    public void hideKeyboard() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void isNetworkAvailable() {
        if (!Utils.isNetworkAvailable(this)) {
            Toast.makeText(getApplicationContext(), "当前网络不可用！", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * /* * 拦截用户快速点击事件
     *
     * @param ev
     * @return
     *//*
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (Utils.isFastDoubleClick()) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }*/
    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private static final String TAG = "JPush";
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    Log.d(TAG, "Set tags in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (Utils.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

        }

    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (Utils.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

        }

    };

    /**
     * 设置别名
     */
    public void setAlias(String alias) {
        LogUtils.i(alias);
        if (TextUtils.isEmpty(alias)) {
            LogUtils.i("别名为空");
            return;
        }
        if (!Utils.isValidTagAndAlias(alias)) {
            LogUtils.i("格式不对");
            return;
        }
        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    /**
     * 设置TAG
     */
    public void setTag(int sex, String province, String city) {
        Set<String> tagSet = new LinkedHashSet<String>();
        if (sex == 1) {
            tagSet.add("man");
        } else {
            tagSet.add("woman");
        }
        if (!TextUtils.isEmpty(province)) {
            tagSet.add(province);
        }
        if (!TextUtils.isEmpty(city)) {
            tagSet.add(city);
        }

        //调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

    }

    //处理个人用户输入框的diogalrt
    public void MyDiogAlret(final String name, final MainSetItemView tv) {
        final EditText editText = new EditText(this);
        final String filedName = getFieldname(name, editText);
        new AlertDialog.Builder(this).setTitle(name).setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                .setPositiveButton(R.string.dl_ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nickString = editText.getText().toString();
                        if (TextUtils.isEmpty(nickString)) {
                            Toast.makeText(BaseActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        pd.setTitle("更新中....");
                        pd.show();
                        updatainfo(nickString, name, tv, filedName);
                    }
                }).setNegativeButton(R.string.dl_cancel, null).show();
    }

    //处理个人用户输入框的diogalrt
    public void MyDiogAlret(final String name, final MainSetItemView tv, final TextView tv_nickname) {
        final EditText editText = new EditText(this);
        final String filedName = getFieldname(name, editText);
        new AlertDialog.Builder(this).setTitle(name).setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                .setPositiveButton(R.string.dl_ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nickString = editText.getText().toString();
                        if (TextUtils.isEmpty(nickString)) {
                            Toast.makeText(BaseActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        pd.setTitle("更新中....");
                        pd.show();
                        updatainfo(nickString, name, tv, filedName, tv_nickname);
                    }
                }).setNegativeButton(R.string.dl_cancel, null).show();
    }

    int myear = 0;
    int mmonth = 0;
    int mday = 0;
    String mweek = null;

    //处理所有输入框的diogalrt
    public void DateDiogAlret(final String name, final MainSetItemView showdate) {
        final EditText et = new EditText(this);
        //初始化Calendar日历对象
        final Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
        if (TextUtils.isEmpty(showdate.getMiddleText())) {
            Date mydate = new Date(); //获取当前日期Date对象
            mycalendar.setTime(mydate);////为Calendar对象设置时间为当前日期
        } else {
            mycalendar.setTime(DateUntils.getDate(showdate.getMiddleText()));
        }
        myear = mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        mmonth = mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        mday = mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天
        DatePickerDialog dp = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myear = year;
                mmonth = month;
                mday = day;
                pd.setTitle("更新中....");
                pd.show();
                String nickString = myear + "-" + (mmonth + 1) + "-" + mday;
                updatainfo(nickString, name, showdate, getFieldname(name, et));
            }
        }, myear, mmonth,
                mday);
        dp.show();
    }

    /**
     * 更改用户基本信息
     */
    public void updatainfo(final String inputvalue, final String message, final MainSetItemView tv, String fileName, final TextView tv_nickNick) {
        RequestParams params = new RequestParams("UTF-8");
        LogUtils.i(DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("fieldname", fileName);
        params.addBodyParameter("fieldvalue", inputvalue);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.UPDATAUSER, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                pd.dismiss();
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                    if (jb.optString("result").equals("1")) {
                        Utils.showToast("更新成功", getApplicationContext());
                        tv.setMiddleText(inputvalue);
                        tv_nickNick.setText(inputvalue);
                        updataDb(message, inputvalue);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Utils.showToast("无网络，请检查网络设置", BaseActivity.this);
                pd.dismiss();
            }
        });
    }

    /**
     * 更改用户基本信息
     */
    public void updatainfo(final String inputvalue, final String message, final MainSetItemView tv, String fileName) {
        RequestParams params = new RequestParams("UTF-8");
        LogUtils.i(DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("fieldname", fileName);
        params.addBodyParameter("fieldvalue", inputvalue);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.UPDATAUSER, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                pd.dismiss();
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                    if (jb.optString("result").equals("1")) {
                        Utils.showToast("更新成功", getApplicationContext());
                        tv.setMiddleText(inputvalue);
                        updataDb(message, inputvalue);//// TODO: 2016/3/23
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Utils.showToast("无网络，请检查网络设置", BaseActivity.this);
                pd.dismiss();
            }
        });
    }

    //处理公司基本信息的diogalrt
    public void UnitDiogAlret(final String name, final MainSetItemView tv, final String companyid) {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this).setTitle(name).setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                .setPositiveButton(R.string.dl_ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nickString = editText.getText().toString();
                        if (TextUtils.isEmpty(nickString)) {
                            Toast.makeText(BaseActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        pd.setTitle("更新中....");
                        pd.show();
                        updataUnitinfo(nickString, name, tv, companyid, editText);
                    }
                }).setNegativeButton(R.string.dl_cancel, null).show();
    }

    /**
     * 更改公司基本信息
     */
    public void updataUnitinfo(final String inputvalue, final String message, final MainSetItemView tv, String companyid, EditText editText) {
        RequestParams params = new RequestParams("UTF-8");
        LogUtils.i(DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("companyid", companyid);
        params.addBodyParameter("fieldname", getFieldname(message, editText));
        params.addBodyParameter("fieldvalue", inputvalue);
        LogUtils.i("companyid:" + companyid + ",fieldname:" + getFieldname(message, editText) + ",fieldvalue:" + inputvalue);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.UPDATA_COMPANY_INFO, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                pd.dismiss();
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                    if (jb.optString("result").equals("1")) {
                        Utils.showToast("更新成功", getApplicationContext());
                        updataDb(message, inputvalue);
                        tv.setMiddleText(inputvalue);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Utils.showToast("无网络，请检查网络设置", BaseActivity.this);
                pd.dismiss();
            }
        });
    }

    /**
     * 根据相关内容返回相关的提交字段
     *
     * @return
     */
    public String getFieldname(String name, EditText et) {
        String fieldname = null;
        if (name.equals("设置昵称")) {
            fieldname = "nickname";
        }
        if (name.equals("设置出生日期")) {
            fieldname = "BornDate";
        }
        if (name.equals("设置姓名")) {
            fieldname = "LastName";
        }
        if (name.equals("设置性别")) {
            fieldname = "Sex";
        }
        if (name.equals("设置身份证")) {
            fieldname = "CertificateID";
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
            et.setMaxEms(18);
        }
        if (name.equals("设置标签")) {
            fieldname = "tag1";
        }
        if (name.equals("设置职务")) {
            fieldname = "JobTitle";
        }
        if (name.equals("设置手机")) {
            fieldname = "CellPhoneNumber";
            et.setInputType(InputType.TYPE_CLASS_PHONE);
        }
        if (name.equals("设置Email")) {
            et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            fieldname = "MailAddress";
        }
        if (name.equals("设置电话")) {
            fieldname = "PhoneNumber";
            et.setInputType(InputType.TYPE_CLASS_PHONE);
        }
        if (name.equals("设置地区")) {
            fieldname = "Country";
        }

        if (name.equals("设置简称")) {
            fieldname = "ShortName";
        }
        if (name.equals("设置全称")) {
            fieldname = "CompanyName";
        }
        if (name.equals("设置行业")) {
            fieldname = "Industry";
        }
        if (name.equals("设置行业标签")) {
            fieldname = "IndustryTag1";
        }
        if (name.equals("设置国家")) {
            fieldname = "Country";
        }
        if (name.equals("设置省份")) {
            fieldname = "City";
        }
        if (name.equals("设置城市")) {
            fieldname = "City";
        }
        if (name.equals("设置公司地址")) {
            fieldname = "StreetAddress";
        }
        if (name.equals("设置公司邮编")) {
            fieldname = "Zip";
        }
        if (name.equals("设置公司电话")) {
            fieldname = "PhoneNumber";
            et.setInputType(InputType.TYPE_CLASS_PHONE);
        }
        if (name.equals("设置公司传真")) {
            fieldname = "FAXNumber";
        }
        if (name.equals("设置网址")) {
            fieldname = "WebSite";
            et.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        }
        return fieldname;
    }

    /**
     * 根据相关内容返回相关的提交字段
     * TODO 暂时只更新公司名
     *
     * @return
     */
    public void updataDb(String name, String input) {
        try {
            LogUtils.i("updataDb");
            PersonInfo pf = DemoApplication.getInstance().getDb().findFirst(Selector.from(PersonInfo.class).where("userid", "=", DemoHelper.getInstance().getCurrentUsernName()));
            String fieldname = null;
            if (name.equals("设置昵称")) {
                fieldname = "nickname";
                pf.setNickname(input);
                Conversion.getInstance().setNickName(input);
            }
            if (name.equals("设置出生日期")) {
                fieldname = "BornDate";
                pf.setBornDate(input);
            }
            if (name.equals("设置姓名")) {
                fieldname = "LastName";
                pf.setLastName(input);
            }
            if (name.equals("设置性别")) {
                fieldname = "Sex";
                pf.setSex(input);
            }
            if (name.equals("设置身份证")) {
                fieldname = "CertificateID";
                pf.setCertificateID(input);
            }
            if (name.equals("设置标签")) {
                fieldname = "tag1";
                pf.setTag1(input);
            }
            if (name.equals("设置职务")) {
                fieldname = "JobTitle";
                pf.setJobTitle(input);
            }
            if (name.equals("设置手机")) {
                fieldname = "CellPhoneNumber";
                pf.setCellPhoneNumber(input);
            }
            if (name.equals("设置Email")) {
                fieldname = "MailAddress";
                pf.setMailAddress(input);
            }
            if (name.equals("设置电话")) {
                fieldname = "PhoneNumber";
                pf.setPhoneNumber(input);
            }
            if (name.equals("设置地区")) {
                fieldname = "Country";
                pf.setCountry(input);
            }

            if (name.equals("设置简称")) {
                fieldname = "ShortName";

            }
            if (name.equals("设置全称")) {
                fieldname = "CompanyName";
            }
            if (name.equals("设置行业")) {
                fieldname = "Industry";
            }
            if (name.equals("设置行业标签")) {
                fieldname = "IndustryTag1";
            }
            if (name.equals("设置国家")) {
                fieldname = "Country";
            }
            if (name.equals("设置省份")) {
                fieldname = "City";
            }
            if (name.equals("设置城市")) {
                fieldname = "City";
            }
            if (name.equals("设置公司地址")) {
                fieldname = "StreetAddress";
            }
            if (name.equals("设置公司邮编")) {
                fieldname = "Zip";
            }
            if (name.equals("设置公司电话")) {
                fieldname = "PhoneNumber";
            }
            if (name.equals("设置公司传真")) {
                fieldname = "FAXNumber";
            }
            if (name.equals("设置网址")) {
                fieldname = "WebSite";
            }
            DemoApplication.getInstance().getDb().update(pf);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /**
     * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
     *
     * @param flag(0:分享到微信好友，1：分享到微信朋友圈)
     */
    public void wechatShare(String description, int flag, String url) {

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "来自麻绳的任务";
        if (!TextUtils.isEmpty(description)) {
            msg.description = description;
        } else {
            msg.description = "无标题";
        }
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.mashen);
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        DemoApplication.getInstance().getApi().sendReq(req);
    }

    Bitmap bitmap = null;

    public void CareateQcr(String headUrl, String nickName) {
        View view = getLayoutInflater().inflate(R.layout.alert_qcr_show, null);
        ivQcrUserpic = (ImageView) view.findViewById(R.id.iv_qcr_userpic);
        imgQcr = (ImageView) view.findViewById(R.id.img_qcr);
        ivQcrUserNick = (TextView) view.findViewById(R.id.iv_qcr_userNick);
        Utils.setAver(headUrl, ivQcrUserpic);
        ivQcrUserNick.setText(nickName);
        alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialog.setView(view);
        myAlertDialog = alertDialog.create();
        if (myAlertDialog.isShowing()) {
            myAlertDialog.dismiss();
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAlertDialog.dismiss();
            }
        });
        Boolean IsExistQcr = sp.getBoolean("qcr", false);
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
                    if (file.exists()) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DemoApplication.getRefWatcher().watch(this);

    }
}

