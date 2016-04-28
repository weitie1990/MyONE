package com.lanxiao.doapp.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doapp.R;
import com.umeng.analytics.MobclickAgent;

public class MeetingWebActivity extends BaseActivity {
    WebView webView;
    ProgressBar progressBar;
    String url;
    Button btn;
    TextView message_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_web);
        message_title= (TextView) findViewById(R.id.message_title);
        url = getIntent().getStringExtra("result");
        String type=getIntent().getStringExtra("type");
        message_title.setText(type);
        Log.i("weitie:", "url2:" + url);
        System.out.print(url);
        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btn = (Button) findViewById(R.id.btn_web_ok);
        //支持javascipt
        webView.getSettings().setJavaScriptEnabled(true);
        //设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        //显示缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        //webView.loadUrl(url);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            //网页加载开始时调用，显示加载提示旋转进度条
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            //网页加载完成时调用，隐藏加载提示旋转进度条
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }


    public void back(View v) {
        finish();
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
