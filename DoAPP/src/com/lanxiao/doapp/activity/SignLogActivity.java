package com.lanxiao.doapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.doapp.R;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SignLogActivity extends Activity {

    @InjectView(R.id.signin_log_back)
    ImageView signinLogBack;
    @InjectView(R.id.ll_signinLog_team)
    LinearLayout llSigninLogTeam;
    @InjectView(R.id.ll_signinLog_my)
    LinearLayout llSigninLogMy;
    @InjectView(R.id.tv_signinLog_tv1)
    TextView tvSigninLogTv1;
    @InjectView(R.id.tv_signinLog_tv2)
    TextView tvSigninLogTv2;
    @InjectView(R.id.tv_signinLog_tv3)
    TextView tvSigninLogTv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_log);
        ButterKnife.inject(this);
        init();
    }

    private void init() {
        String html="电脑访问:\t";
        html+="www.yunzhijia.com";
        SpannableString ss1=new SpannableString(html);
        SpannableString ss2=new SpannableString("进入应用-签到统计");
        ss1.setSpan(new TextAppearanceSpan(this,R.style.style1),0,4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss1.setSpan(new TextAppearanceSpan(this,R.style.style0),5,ss1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(new TextAppearanceSpan(this, R.style.style1), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(new TextAppearanceSpan(this, R.style.style0), 2, ss2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSigninLogTv1.setAutoLinkMask(Linkify.ALL);
        tvSigninLogTv1.setMovementMethod(LinkMovementMethod.getInstance());
        tvSigninLogTv1.setText(ss1);
        tvSigninLogTv2.setAutoLinkMask(Linkify.ALL);
        tvSigninLogTv2.setMovementMethod(LinkMovementMethod.getInstance());
        tvSigninLogTv2.setText(ss2);
    }

    @OnClick({R.id.signin_log_back, R.id.ll_signinLog_team, R.id.ll_signinLog_my})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signin_log_back:
                finish();
                break;
            case R.id.ll_signinLog_team:
                startActivity(new Intent(SignLogActivity.this,SinginLogInfoActivity.class));
                break;
            case R.id.ll_signinLog_my:
                startActivity(new Intent(SignLogActivity.this,PersonSigninLogActivity.class));
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
