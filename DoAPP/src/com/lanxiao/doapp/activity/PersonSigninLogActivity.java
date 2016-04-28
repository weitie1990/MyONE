package com.lanxiao.doapp.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.doapp.R;
import com.lanxiao.doapp.adapter.PersonSignInLogAdapter;
import com.lanxiao.doapp.entity.PersonSignInLogItem;
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

public class PersonSigninLogActivity extends BaseActivity {

    @InjectView(R.id.signin_log_person_back)
    ImageView signinLogPersonBack;
    @InjectView(R.id.signin_log_person_statistic)
    TextView signinLogPersonStatistic;
    @InjectView(R.id.signin_log_person_time)
    TextView signinLogPersonTime;
    @InjectView(R.id.signin_log_person_selecttime)
    TextView signinLogPersonSelecttime;
    @InjectView(R.id.lv_signinLog_person)
    ListView lvSigninLogPerson;
    List<PersonSignInLogItem> list;
    PersonSignInLogAdapter personSignInLogAdapter;
    String data=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_signin_log);
        ButterKnife.inject(this);
        init();
    }

    private void init() {
        data=DateUntils.getDate(new Date());
        String week=DateUntils.getWeekOfDate(new Date());
        signinLogPersonTime.setText(week+" "+data);
        list=new ArrayList<>();
        PersonSignInLogItem personSignInLogItem1=new PersonSignInLogItem();
        personSignInLogItem1.setSignaddress("上海市杨浦区68号");
        personSignInLogItem1.setSignbeiju("加急急");
        personSignInLogItem1.setSingintime("14:16");
        personSignInLogItem1.setUserName("魏铁");
        PersonSignInLogItem personSignInLogItem2=new PersonSignInLogItem();
        personSignInLogItem2.setSignaddress("上海市浦东新区龙东大道948号");
        personSignInLogItem2.setSignbeiju("今天在这");
        personSignInLogItem2.setSingintime("14:20");
        personSignInLogItem2.setUserName("张三");
        list.add(personSignInLogItem1);
        list.add(personSignInLogItem2);
        personSignInLogAdapter=new PersonSignInLogAdapter(this,list);
        lvSigninLogPerson.setAdapter(personSignInLogAdapter);
    }

    @OnClick({R.id.signin_log_person_back, R.id.signin_log_person_statistic, R.id.signin_log_person_selecttime})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signin_log_person_back:
                finish();
                break;
            case R.id.signin_log_person_statistic:

                break;
            case R.id.signin_log_person_selecttime:
                DateDiogAlret(signinLogPersonTime);
                break;
        }
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
