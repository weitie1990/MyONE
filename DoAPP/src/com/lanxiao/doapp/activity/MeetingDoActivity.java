package com.lanxiao.doapp.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.core.PoiInfo;
import com.easemob.easeui.Conversion;
import com.example.doapp.R;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.untils.DateTimePickDialogUtil;
import com.lanxiao.doapp.untils.DateUntils;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.utils.ScreenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Thinkpad on 2016/2/27.
 */
public class MeetingDoActivity extends BaseActivity {
    @InjectView(R.id.message_back)
    ImageView messageBack;
    @InjectView(R.id.message_title)
    TextView messageTitle;
    @InjectView(R.id.et_meeting_title)
    EditText etMeetingTitle;
    @InjectView(R.id.iv_meeting_time)
    TextView ivMeetingTime;
    @InjectView(R.id.ll_meeting_time)
    LinearLayout llMeetingTime;
    @InjectView(R.id.iv_meeting_location)
    TextView ivMeetingLocation;
    @InjectView(R.id.ll_meeting_location)
    LinearLayout llMeetingLocation;
    @InjectView(R.id.iv_meeting_jion)
    TextView ivMeetingJion;
    @InjectView(R.id.ll_meeting_jion)
    LinearLayout llMeetingJion;
    @InjectView(R.id.iv_meeting_clock)
    TextView ivMeetingClock;
    @InjectView(R.id.ll_meeting_clock)
    LinearLayout llMeetingClock;
    @InjectView(R.id.et_meeting_beiju)
    EditText etMeetingBeiju;
    @InjectView(R.id.btn_meeting_do)
    Button btnMeetingDo;
    String handlerid=null;
    String targetusername=null;
    String data=null;
    private String name=null;
    private double latitude;
    private double longitude;
    private String address=null;
    private PoiInfo poiInfo;
    private Boolean isFirst=true;//是否第一次选择地点
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_do);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.message_back, R.id.ll_meeting_time, R.id.ll_meeting_location, R.id.ll_meeting_jion, R.id.ll_meeting_clock, R.id.btn_meeting_do, R.id.message_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.message_back:
                finish();
                break;
            case R.id.ll_meeting_time:
                if(TextUtils.isEmpty(ivMeetingTime.getText())){
                    data= DateUntils.getChineseDate(new Date());
                }else {
                    Date date=DateUntils.getEndDate(ivMeetingTime.getText().toString());
                    data=DateUntils.getChineseDate(date);
                }
                showtimeView(data,ivMeetingTime);
                break;
            case R.id.ll_meeting_location:
                Intent intent4=new Intent(MeetingDoActivity.this,CurrentLocationActivity.class);
                if(!isFirst){
                    Bundle bundle=new Bundle();
                    bundle.putParcelable("poiInfo", poiInfo);
                    intent4.putExtras(bundle);
                }
                startActivityForResult(intent4, REQUSET_LOCATION_CURRENT);
                break;
            case R.id.ll_meeting_jion:
                Intent intent = new Intent(MeetingDoActivity.this, LocatContentActivity.class);
                intent.putExtra("type","4");
                startActivityForResult(intent, REQUST_INPUT_FOR_CONTENT);
                break;
            case R.id.ll_meeting_clock:
                //showtimeView(data,ivMeetingClock);
                selectTiDiaog();
                break;
            case R.id.btn_meeting_do:
                doToServer();
                break;
            case R.id.message_search:
                Intent intent1=new Intent(MeetingDoActivity.this,MeetingWebActivity.class);
                intent1.putExtra("result", Api.MEETING_SEARCH+ DemoHelper.getInstance().getCurrentUsernName());
                intent1.putExtra("type","1");
                startActivity(intent1);
                break;
        }
    }
    /**
     * 显示选择日期对话框
     * @param time1
     */
    private void showtimeView(String time1,TextView tv){
        DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                MeetingDoActivity.this,time1);
        dateTimePicKDialog.dateTimePicKDialog(tv);
    }

    /**
     * 提交
     */
    private void doToServer() {
        HttpUtils httpUtils=new HttpUtils("UTF-8");
        RequestParams rp=new RequestParams();
        rp.addBodyParameter("username", Conversion.getInstance().getNickName());
        rp.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
        if(!TextUtils.isEmpty(ivMeetingTime.getText().toString())){
            rp.addBodyParameter("meetingtime",ivMeetingTime.getText().toString());
        }else {
            Utils.showToast("会议时间不能为空", getApplicationContext());
            return;
        }
        if(!TextUtils.isEmpty(ivMeetingLocation.getText().toString())){
            rp.addBodyParameter("geopos",longitude+","+latitude);
            rp.addBodyParameter("geoaddress",address);
        }else {
            Utils.showToast("会议地点不能为空", getApplicationContext());
            return;
        }
        if(!TextUtils.isEmpty(ivMeetingJion.getText().toString())){
            rp.addBodyParameter("attendant",targetusername);
            rp.addBodyParameter("attendantid",handlerid);
        }else {
            Utils.showToast("会议参加者不能为空", getApplicationContext());
            return;
        }
        if(!TextUtils.isEmpty(ivMeetingClock.getText().toString())){
            rp.addBodyParameter("alerttime",ivMeetingClock.getText().toString());
        }else {
            Utils.showToast("会议提醒时间不能为空", getApplicationContext());
            return;
        }
        if(!TextUtils.isEmpty(etMeetingTitle.getText().toString())){
            rp.addBodyParameter("title",etMeetingTitle.getText().toString());
        }else {
            Utils.showToast("会议标题不能为空", getApplicationContext());
            return;
        }
        if(!TextUtils.isEmpty(etMeetingBeiju.getText().toString())){
            rp.addBodyParameter("body",etMeetingBeiju.getText().toString());
        }
        else {
            Utils.showToast("备注不能为空", getApplicationContext());
            return;
        }
        pd.setTitle("正在在提交");
        pd.show();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.MEETING_SUMMIT, rp, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
              LogUtils.i(responseInfo.result);
                pd.dismiss();
                try {
                    JSONObject jb=new JSONObject(responseInfo.result);
                    if(jb.optString("result").equals("0")){
                        Utils.showToast("提交成功", getApplication());
                        Intent intent=new Intent(MeetingDoActivity.this,MainActivity.class);
                        intent.putExtra("type",4);
                        startActivity(intent);
                    }else {
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

    //处理所有输入框的diogalrt
    public void DateDiogAlret(final TextView tv) {
        //初始化Calendar日历对象
        final Calendar mycalendar=Calendar.getInstance(Locale.CHINA);
        Date mydate=new Date(); //获取当前日期Date对象
        mycalendar.setTime(mydate);////为Calendar对象设置时间为当前日期
        myear=mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        mmonth=mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        mday=mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天
        DatePickerDialog dp=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myear=year;
                mmonth=month;
                mday=day;
                String nickString=myear+"-"+(mmonth+1)+"-"+mday;
                tv.setText(nickString);
            }
        }, myear, mmonth,
                mday);
        dp.show();
    }
    //处理个人用户输入框的diogalrt
    public void MyDiogAlret(String name,final TextView tv){
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this).setTitle(name).setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                .setPositiveButton(R.string.dl_ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nickString = editText.getText().toString();
                        if (TextUtils.isEmpty(nickString)) {
                            Toast.makeText(MeetingDoActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        tv.setText(nickString);
                    }
                }).setNegativeButton(R.string.dl_cancel, null).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUST_INPUT_FOR_CONTENT&&resultCode==LOCATION_RESULT_SEND){
            //返回选择的参加者
            if(data!=null){
                handlerid=data.getStringExtra("handlerid");
                targetusername=data.getStringExtra("targetusername");
                ivMeetingJion.setText(targetusername);
                LogUtils.i(handlerid + "," + targetusername);
            }
        }
        //定位返回位置名
        if(requestCode==REQUSET_LOCATION_CURRENT&&resultCode==RESULT_LOCATION_CURRENT){
            poiInfo=data.getParcelableExtra("poiInfo");
           /* name=data.getStringExtra("name");
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);
            address=data.getStringExtra("address");*/
            if(poiInfo!=null) {
                isFirst=false;
                ivMeetingLocation.setText(poiInfo.name);
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
    int choice=0;
    String strTime;
    String alertTime=null;
    public void selectTiDiaog(){
        if(TextUtils.isEmpty(ivMeetingTime.getText())){
            Utils.showToast("请先选择会议时间",MeetingDoActivity.this);
            return;
        }
        strTime=ivMeetingTime.getText().toString();
        final String [] tiems={"开始时间","1分钟前","5分钟前","10分钟前","15分钟前","20分钟前","25分钟前","30分钟前","45分钟前",
        "1小时","2小时","3小时","12小时","24小时前"
        };
        AlertDialog.Builder ab=new AlertDialog.Builder(MeetingDoActivity.this,android.R.style.Theme_Holo_Light_Dialog).setTitle("添加提醒").setSingleChoiceItems(tiems, choice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choice = which;
                switch (which) {
                    case 0:
                        alertTime = strTime;
                        break;
                    case 1:
                        alertTime = DateUntils.getEnglishDate(new Date(DateUntils.getEndDate(strTime).getTime() - 1 * 60 * 1000));
                        break;
                    case 2:
                        alertTime = DateUntils.getEnglishDate(new Date(DateUntils.getEndDate(strTime).getTime() - 5 * 60 * 1000));
                        break;
                    case 3:
                        alertTime = DateUntils.getEnglishDate(new Date(DateUntils.getEndDate(strTime).getTime()-10 * 60 * 1000));
                        break;
                    case 4:
                        alertTime = DateUntils.getEnglishDate(new Date(DateUntils.getEndDate(strTime).getTime()- 15 * 60 * 1000));
                        break;
                    case 5:
                        alertTime = DateUntils.getEnglishDate(new Date(DateUntils.getEndDate(strTime).getTime() - 20 * 60 * 1000));
                        break;
                    case 6:
                        alertTime = DateUntils.getEnglishDate(new Date(DateUntils.getEndDate(strTime).getTime() - 25 * 60 * 1000));
                        break;
                    case 7:
                        alertTime = DateUntils.getEnglishDate(new Date(DateUntils.getEndDate(strTime).getTime() - 30 * 60 * 1000));
                        break;
                    case 8:
                        alertTime = DateUntils.getEnglishDate(new Date(DateUntils.getEndDate(strTime).getTime() - 45 * 60 * 1000));
                        break;
                    case 9:
                        alertTime = DateUntils.getEnglishDate(new Date(DateUntils.getEndDate(strTime).getTime() - 1 * 60 * 60 * 1000));
                        break;
                    case 10:
                        alertTime = DateUntils.getEnglishDate(new Date(DateUntils.getEndDate(strTime).getTime() - 2 * 60 * 60 * 1000));
                        break;
                    case 11:
                        alertTime = DateUntils.getEnglishDate(new Date(DateUntils.getEndDate(strTime).getTime() - 3 * 60 * 60 * 1000));
                        break;
                    case 12:
                        alertTime = DateUntils.getEnglishDate(new Date(DateUntils.getEndDate(strTime).getTime() - 12 * 60 * 60 * 1000));
                        break;
                    case 13:
                        alertTime = DateUntils.getEnglishDate(new Date(DateUntils.getEndDate(strTime).getTime() - 24 * 60 * 60 * 1000));
                        break;
                }
            }
        }).setNeutralButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ivMeetingClock.setText(alertTime);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = ab.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
        alertDialog.getWindow().setLayout(ScreenUtils.getScreenWidth(getApplicationContext()) * 5 / 6, ScreenUtils.getScreenHeight(getApplicationContext()) * 2 / 3);

    }
}
