package com.lanxiao.doapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.doapp.R;
import com.umeng.analytics.MobclickAgent;

/**
 * h5
 * Created by Thinkpad on 2015/10/28.
 */
public class WorkWebActivity extends BaseActivity{
    ProgressBar progressBar;
    String url;
    LinearLayout btn_press_to_submit;
    ValueCallback<Uri> mUploadMessage;
    private String TAG="WorkWebActivity";
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_work);
        url=getIntent().getStringExtra("result");
        Log.i("weitie:", "url2:" + url);
        System.out.print(url);
        webView= (WebView) findViewById(R.id.webView);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        //支持javascipt
        webView.getSettings().setJavaScriptEnabled(true);
        //设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        //显示缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.loadUrl("http://www.dosns.net/js2android.html");



        btn_press_to_submit= (LinearLayout) findViewById(R.id.btn_press_to_submit);
        btn_press_to_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

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
