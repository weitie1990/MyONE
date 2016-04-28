package com.lanxiao.doapp.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.doapp.R;
import com.lanxiao.doapp.adapter.SignInLogAdapter;
import com.lanxiao.doapp.entity.SignInLogItem;
import com.lanxiao.doapp.untils.DateUntils;
import com.umeng.analytics.MobclickAgent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SinginLogInfoActivity extends BaseActivity {

    @InjectView(R.id.signin_log_back)
    ImageView signinLogBack;
    @InjectView(R.id.tv_signinLogInfo_time)
    TextView tvSigninLogInfoTime;
    @InjectView(R.id.tv_signinLogInfo_time_back)
    ImageView tvSigninLogInfoTimeBack;
    @InjectView(R.id.tv_signinLogInfo_time_next)
    ImageView tvSigninLogInfoTimeNext;
    @InjectView(R.id.tv_signinLogInfo_map)
    Button tvSigninLogInfoMap;
    @InjectView(R.id.lv_signinLog)
    ListView lvSigninLog;
    List<SignInLogItem> list;
    SignInLogAdapter signInLogAdapter;
    private GestureDetector gestureDetector;
    final int RIGHT = 0;
    final int LEFT = 1;
    String data=null;
    String week=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin_log_info);
        ButterKnife.inject(this);
        init();
    }

    private void init() {
        list=new ArrayList<>();
        SignInLogItem si1=new SignInLogItem();
        si1.setUserId("34343434324");
        si1.setFirstTime("14:18");
        si1.setFirstAddress("上海市川江路569号");
        si1.setTwoSatuas("0");
        SignInLogItem si2=new SignInLogItem();
        si2.setUserId("34343434324");
        si2.setFirstTime("14:18");
        si2.setFirstAddress("上海市川江路胜利村4569号小白路");
        si2.setTwoTime("16:48");
        si2.setTwoAddress("武汉市吴家山江汉路4566号");
        si2.setTwoSatuas("1");
        list.add(si1);
        list.add(si2);
        signInLogAdapter=new SignInLogAdapter(this,list);
        lvSigninLog.setAdapter(signInLogAdapter);
        data=DateUntils.getDate(new Date());
        week=DateUntils.getWeekOfDate(new Date());
                tvSigninLogInfoTime.setText(data + " " +week);
        gestureDetector = new GestureDetector(SinginLogInfoActivity.this,onGestureListener);
    }

    @OnClick({R.id.signin_log_back, R.id.tv_signinLogInfo_time_back, R.id.tv_signinLogInfo_time_next, R.id.tv_signinLogInfo_map,R.id.tv_signinLogInfo_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signin_log_back:
                finish();
                break;
            case R.id.tv_signinLogInfo_time:
                DateDiogAlret(tvSigninLogInfoTime);
                break;
            case R.id.tv_signinLogInfo_time_back:
                break;
            case R.id.tv_signinLogInfo_time_next:
                break;
            case R.id.tv_signinLogInfo_map:
                break;
        }
    }
    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    float x = e2.getX() - e1.getX();
                    float y = e2.getY() - e1.getY();

                    if (x > 0&&x>120&&Math.abs(y)<120) {
                        doResult(RIGHT);
                    } else if (x < 0&&Math.abs(x)>120&&Math.abs(y)<120) {
                        doResult(LEFT);
                    }
                    return true;
                }
            };

    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public void doResult(int action) {

        switch (action) {
            case RIGHT:
                System.out.println("go right");
                data=DateUntils.getDateStr(data, -1);
                break;

            case LEFT:
                System.out.println("go left");
                if(DateUntils.isdaNowtime(DateUntils.getDateStr(data, +1))){
                    return;
                }else {
                    data = DateUntils.getDateStr(data, +1);
                }
                break;

        }
        week=DateUntils.getWeekOfDate(getNowData(data));
        tvSigninLogInfoTime.setText(data + " " +week);

    }
    private Date getNowData(String day){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nowDate;
    }
    //处理所有输入框的diogalrt
    public void DateDiogAlret(final TextView tv) {
        //初始化Calendar日历对象
        final Calendar mycalendar=Calendar.getInstance(Locale.CHINA);
        mycalendar.setTime(getNowData(data));////为Calendar对象设置时间为当前日期
        myear=mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        mmonth=mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        mday=mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天
        DatePickerDialog dp=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Calendar calendar=Calendar.getInstance(Locale.CHINA);
                calendar.set(year,month,day);
                mweek= DateUntils.getWeekOfDate(calendar.getTime());
                myear=year;
                mmonth=month;
                mday=day;
                data=myear+"-"+(mmonth+1)+"-"+mday;
                tv.setText(mweek+" "+data);
            }
        }, myear, mmonth,
                mday);
        dp.show();
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
